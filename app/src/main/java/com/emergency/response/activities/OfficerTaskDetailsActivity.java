package com.emergency.response.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.models.Officer;
import com.google.android.material.button.MaterialButton;

public class OfficerTaskDetailsActivity extends AppCompatActivity {

    private ImageView btnBackOfficerTaskDetails;
    private TextView tvOfficerTaskId, tvOfficerTaskStatusBadge;
    private TextView tvTaskCategoryTag, tvTaskTitle, tvTaskDescription;
    private TextView tvTaskLocation, tvTaskCoordinates;
    private TextView tvTaskReporterName, tvTaskReporterPhone;
    private MaterialButton btnOfficerOpenMap, btnCallReporter;
    private MaterialButton btnOfficerAcceptTask, btnOfficerStartResponse, btnOfficerUpdateProgress, btnOfficerMarkCompleted;

    private String reportId;
    private EmergencyReport currentReport;
    private Officer currentOfficer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_task_details);

        reportId = getIntent().getStringExtra("report_id");
        if (reportId == null) reportId = "EMG-2026-8942";

        currentOfficer = EmergencyDataRepository.getInstance().getCurrentOfficerProfile();
        if (currentOfficer == null) {
            currentOfficer = EmergencyDataRepository.getInstance().getOfficerById("OFF-4092");
        }

        initViews();
        setupListeners();
        loadTaskDetails();
    }

    private void initViews() {
        btnBackOfficerTaskDetails = findViewById(R.id.btnBackOfficerTaskDetails);
        tvOfficerTaskId = findViewById(R.id.tvOfficerTaskId);
        tvOfficerTaskStatusBadge = findViewById(R.id.tvOfficerTaskStatusBadge);

        tvTaskCategoryTag = findViewById(R.id.tvTaskCategoryTag);
        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDescription = findViewById(R.id.tvTaskDescription);
        tvTaskLocation = findViewById(R.id.tvTaskLocation);
        tvTaskCoordinates = findViewById(R.id.tvTaskCoordinates);

        tvTaskReporterName = findViewById(R.id.tvTaskReporterName);
        tvTaskReporterPhone = findViewById(R.id.tvTaskReporterPhone);

        btnOfficerOpenMap = findViewById(R.id.btnOfficerOpenMap);
        btnCallReporter = findViewById(R.id.btnCallReporter);

        btnOfficerAcceptTask = findViewById(R.id.btnOfficerAcceptTask);
        btnOfficerStartResponse = findViewById(R.id.btnOfficerStartResponse);
        btnOfficerUpdateProgress = findViewById(R.id.btnOfficerUpdateProgress);
        btnOfficerMarkCompleted = findViewById(R.id.btnOfficerMarkCompleted);
    }

    private void setupListeners() {
        btnBackOfficerTaskDetails.setOnClickListener(v -> finish());

        btnCallReporter.setOnClickListener(v -> {
            if (currentReport != null && currentReport.getReporterPhone() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentReport.getReporterPhone()));
                startActivity(dialIntent);
            }
        });

        btnOfficerOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(OfficerTaskDetailsActivity.this, EmergencyLocationActivity.class);
            startActivity(intent);
        });

        btnOfficerAcceptTask.setOnClickListener(v -> {
            Toast.makeText(this, "Task Assignment Confirmed by Officer.", Toast.LENGTH_SHORT).show();
            btnOfficerAcceptTask.setEnabled(false);
            btnOfficerAcceptTask.setText("✓ Task Accepted");
        });

        btnOfficerStartResponse.setOnClickListener(v -> {
            String offId = currentOfficer != null ? currentOfficer.getId() : "OFF-4092";
            boolean success = EmergencyDataRepository.getInstance().startOfficerResponse(reportId, offId);
            if (success) {
                Toast.makeText(this, "Response Initiated! Central Control & Citizen notified.", Toast.LENGTH_SHORT).show();
                loadTaskDetails();
            }
        });

        btnOfficerUpdateProgress.setOnClickListener(v -> showUpdateProgressDialog());

        btnOfficerMarkCompleted.setOnClickListener(v -> showCompleteTaskDialog());
    }

    private void loadTaskDetails() {
        currentReport = EmergencyDataRepository.getInstance().getReportById(reportId);
        if (currentReport == null) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvOfficerTaskId.setText(currentReport.getId());
        tvOfficerTaskStatusBadge.setText(currentReport.getStatus());
        applyStatusBadgeColor(currentReport.getStatus());

        tvTaskCategoryTag.setText(currentReport.getCategory().toUpperCase() + " MISSION");
        tvTaskTitle.setText(currentReport.getTitle());
        tvTaskDescription.setText(currentReport.getDescription());
        tvTaskLocation.setText(currentReport.getLocation());
        tvTaskCoordinates.setText("GPS Coordinates: " + currentReport.getLatitude() + "° N, " + currentReport.getLongitude() + "° E");

        tvTaskReporterName.setText(currentReport.getReporterName());
        tvTaskReporterPhone.setText(currentReport.getReporterPhone());

        if ("Responding".equalsIgnoreCase(currentReport.getStatus())) {
            btnOfficerStartResponse.setEnabled(false);
            btnOfficerStartResponse.setText("✓ Responders En Route");
        } else if ("Resolved".equalsIgnoreCase(currentReport.getStatus())) {
            btnOfficerAcceptTask.setEnabled(false);
            btnOfficerStartResponse.setEnabled(false);
            btnOfficerUpdateProgress.setEnabled(false);
            btnOfficerMarkCompleted.setEnabled(false);
            btnOfficerMarkCompleted.setText("✓ Mission Completed");
        }
    }

    private void applyStatusBadgeColor(String status) {
        if ("Assigned".equalsIgnoreCase(status)) {
            tvOfficerTaskStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_assigned));
            tvOfficerTaskStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_assigned_bg));
        } else if ("Responding".equalsIgnoreCase(status)) {
            tvOfficerTaskStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_responding));
            tvOfficerTaskStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_responding_bg));
        } else if ("Resolved".equalsIgnoreCase(status)) {
            tvOfficerTaskStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_resolved));
            tvOfficerTaskStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_resolved_bg));
        } else {
            tvOfficerTaskStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.navy_primary));
            tvOfficerTaskStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_light));
        }
    }

    private void showUpdateProgressDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter response notes (e.g. Pump deployed, victims extricated)...");
        input.setText("Field unit on site. Clearing obstruction and providing primary aid.");

        new AlertDialog.Builder(this)
                .setTitle("Update Response Progress")
                .setMessage("Log field operational progress and attach response evidence:")
                .setView(input)
                .setPositiveButton("Submit Update", (dialog, which) -> {
                    String notes = input.getText().toString().trim();
                    if (!notes.isEmpty() && currentReport != null) {
                        currentReport.setDescription(currentReport.getDescription() + "\n\n[Field Update by " + (currentOfficer != null ? currentOfficer.getName() : "Officer") + "]: " + notes);
                        loadTaskDetails();
                    }
                    Toast.makeText(this, "Operational field update recorded with evidence.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCompleteTaskDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Mark Mission Resolved")
                .setMessage("Are you sure you want to mark this incident as RESOLVED?\n\nThis will verify site safety, notify the Citizen, and restore your status to 'Available'.")
                .setPositiveButton("Confirm Resolved", (dialog, which) -> {
                    String offId = currentOfficer != null ? currentOfficer.getId() : "OFF-4092";
                    boolean success = EmergencyDataRepository.getInstance().completeOfficerTask(reportId, offId);
                    if (success) {
                        Toast.makeText(this, "Mission marked RESOLVED! Great work.", Toast.LENGTH_LONG).show();
                        loadTaskDetails();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
