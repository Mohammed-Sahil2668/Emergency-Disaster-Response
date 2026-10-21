package com.emergency.response.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.google.android.material.button.MaterialButton;

public class AuthorityEmergencyDetailsActivity extends AppCompatActivity {

    private ImageView btnBackAuthorityDetails, ivEmergencyCategoryIcon;
    private TextView tvHeaderReportId, tvHeaderStatusBadge;
    private TextView tvReporterFullName, tvReporterPhone, tvReporterEmail;
    private TextView tvEmergencyCategoryName, tvEmergencyTitle, tvEmergencyDesc;
    private TextView tvEmergencyDateTime, tvEmergencyLocation, tvEmergencyCoordinates;
    private TextView tvAttachedPhotoName;
    private LinearLayout layoutAssignedOfficerRow;
    private TextView tvAssignedOfficerName, tvRejectionReasonView;

    private MaterialButton btnAuthAssignOfficer, btnAuthVerify, btnAuthReject, btnAuthViewMap;

    private String reportId;
    private EmergencyReport currentReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_emergency_details);

        reportId = getIntent().getStringExtra("report_id");
        if (reportId == null) {
            reportId = "EMG-2026-8942";
        }

        initViews();
        setupListeners();
        loadReportDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReportDetails();
    }

    private void initViews() {
        btnBackAuthorityDetails = findViewById(R.id.btnBackAuthorityDetails);
        ivEmergencyCategoryIcon = findViewById(R.id.ivEmergencyCategoryIcon);
        tvHeaderReportId = findViewById(R.id.tvHeaderReportId);
        tvHeaderStatusBadge = findViewById(R.id.tvHeaderStatusBadge);

        tvReporterFullName = findViewById(R.id.tvReporterFullName);
        tvReporterPhone = findViewById(R.id.tvReporterPhone);
        tvReporterEmail = findViewById(R.id.tvReporterEmail);

        tvEmergencyCategoryName = findViewById(R.id.tvEmergencyCategoryName);
        tvEmergencyTitle = findViewById(R.id.tvEmergencyTitle);
        tvEmergencyDesc = findViewById(R.id.tvEmergencyDesc);
        tvEmergencyDateTime = findViewById(R.id.tvEmergencyDateTime);
        tvEmergencyLocation = findViewById(R.id.tvEmergencyLocation);
        tvEmergencyCoordinates = findViewById(R.id.tvEmergencyCoordinates);
        tvAttachedPhotoName = findViewById(R.id.tvAttachedPhotoName);

        layoutAssignedOfficerRow = findViewById(R.id.layoutAssignedOfficerRow);
        tvAssignedOfficerName = findViewById(R.id.tvAssignedOfficerName);
        tvRejectionReasonView = findViewById(R.id.tvRejectionReasonView);

        btnAuthAssignOfficer = findViewById(R.id.btnAuthAssignOfficer);
        btnAuthVerify = findViewById(R.id.btnAuthVerify);
        btnAuthReject = findViewById(R.id.btnAuthReject);
        btnAuthViewMap = findViewById(R.id.btnAuthViewMap);
    }

    private void setupListeners() {
        btnBackAuthorityDetails.setOnClickListener(v -> finish());

        btnAuthVerify.setOnClickListener(v -> {
            boolean success = EmergencyDataRepository.getInstance().verifyReport(reportId);
            if (success) {
                Toast.makeText(this, "Emergency Incident Verified. Ready for officer assignment.", Toast.LENGTH_SHORT).show();
                loadReportDetails();
            }
        });

        btnAuthReject.setOnClickListener(v -> showRejectDialog());

        btnAuthAssignOfficer.setOnClickListener(v -> {
            Intent intent = new Intent(AuthorityEmergencyDetailsActivity.this, AssignOfficerActivity.class);
            intent.putExtra("report_id", reportId);
            startActivity(intent);
        });

        btnAuthViewMap.setOnClickListener(v -> {
            Intent intent = new Intent(AuthorityEmergencyDetailsActivity.this, EmergencyLocationActivity.class);
            startActivity(intent);
        });
    }

    private void loadReportDetails() {
        currentReport = EmergencyDataRepository.getInstance().getReportById(reportId);
        if (currentReport == null) {
            Toast.makeText(this, "Incident report not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvHeaderReportId.setText(currentReport.getId());
        tvHeaderStatusBadge.setText(currentReport.getStatus());
        applyStatusBadgeColor(currentReport.getStatus());

        // Reporter
        tvReporterFullName.setText(currentReport.getReporterName());
        tvReporterPhone.setText(currentReport.getReporterPhone());
        tvReporterEmail.setText(currentReport.getReporterEmail());

        // Emergency
        tvEmergencyCategoryName.setText(currentReport.getCategory());
        ivEmergencyCategoryIcon.setImageResource(currentReport.getCategoryIconRes());
        tvEmergencyTitle.setText(currentReport.getTitle());
        tvEmergencyDesc.setText(currentReport.getDescription());
        tvEmergencyDateTime.setText("Reported: " + currentReport.getReportedDate());
        tvEmergencyLocation.setText(currentReport.getLocation());
        tvEmergencyCoordinates.setText("GPS: " + currentReport.getLatitude() + "° N, " + currentReport.getLongitude() + "° E");

        if (currentReport.getAttachedPhotoName() != null && !currentReport.getAttachedPhotoName().isEmpty()) {
            tvAttachedPhotoName.setText(currentReport.getAttachedPhotoName());
        }

        // Assigned officer
        if (currentReport.getOfficerName() != null && !currentReport.getOfficerName().isEmpty() && !currentReport.getOfficerName().equalsIgnoreCase("Unassigned")) {
            layoutAssignedOfficerRow.setVisibility(View.VISIBLE);
            tvAssignedOfficerName.setText(currentReport.getOfficerName());
        } else {
            layoutAssignedOfficerRow.setVisibility(View.GONE);
        }

        // Rejection
        if ("Rejected".equalsIgnoreCase(currentReport.getStatus())) {
            tvRejectionReasonView.setVisibility(View.VISIBLE);
            tvRejectionReasonView.setText("Rejection Reason: " + currentReport.getRejectionReason());
            btnAuthVerify.setEnabled(false);
            btnAuthAssignOfficer.setEnabled(false);
            btnAuthReject.setEnabled(false);
        } else {
            tvRejectionReasonView.setVisibility(View.GONE);
        }
    }

    private void applyStatusBadgeColor(String status) {
        if ("Reported".equalsIgnoreCase(status)) {
            tvHeaderStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_reported));
            tvHeaderStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_reported_bg));
        } else if ("Verified".equalsIgnoreCase(status)) {
            tvHeaderStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_verified));
            tvHeaderStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_verified_bg));
        } else if ("Assigned".equalsIgnoreCase(status)) {
            tvHeaderStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_assigned));
            tvHeaderStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_assigned_bg));
        } else if ("Responding".equalsIgnoreCase(status)) {
            tvHeaderStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_responding));
            tvHeaderStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_responding_bg));
        } else if ("Resolved".equalsIgnoreCase(status)) {
            tvHeaderStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_resolved));
            tvHeaderStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_resolved_bg));
        } else if ("Rejected".equalsIgnoreCase(status)) {
            tvHeaderStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.status_rejected));
            tvHeaderStatusBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.status_rejected_bg));
        }
    }

    private void showRejectDialog() {
        final EditText input = new EditText(this);
        input.setHint(R.string.dialog_reject_hint);
        input.setText("Duplicate incident call already addressed by sector unit.");

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_reject_title)
                .setMessage("Please specify an official reason for rejecting this emergency report:")
                .setView(input)
                .setPositiveButton("Confirm Reject", (dialog, which) -> {
                    String reason = input.getText().toString().trim();
                    if (reason.isEmpty()) reason = "Unverified or out of jurisdiction incident.";
                    EmergencyDataRepository.getInstance().rejectReport(reportId, reason);
                    Toast.makeText(this, "Emergency report marked Rejected.", Toast.LENGTH_SHORT).show();
                    loadReportDetails();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
