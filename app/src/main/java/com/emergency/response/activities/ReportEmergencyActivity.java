package com.emergency.response.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyCategory;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.utils.DateTimeUtils;
import com.emergency.response.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

public class ReportEmergencyActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PICK = 101;

    private Toolbar toolbarReport;
    private Spinner spinnerCategory;
    private TextInputLayout tilIncidentTitle, tilIncidentDesc, tilIncidentLocation;
    private TextInputEditText etIncidentTitle, etIncidentDesc, etIncidentLocation;
    private MaterialButton btnUseCurrentLocation, btnPickOnMap, btnSubmitReport;
    private View cardPhotoUpload;
    private TextView tvPhotoFileName, tvIncidentDateTime;
    private ImageView ivUploadedPhoto;

    private double currentLatitude = 12.9716;
    private double currentLongitude = 77.5946;
    private String selectedPhotoName = "incident_evidence_01.jpg";
    private Calendar incidentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_emergency);

        incidentCalendar = Calendar.getInstance();

        initViews();
        setupCategorySpinner();
        setupListeners();
    }

    private void initViews() {
        toolbarReport = findViewById(R.id.toolbarReport);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        tilIncidentTitle = findViewById(R.id.tilIncidentTitle);
        tilIncidentDesc = findViewById(R.id.tilIncidentDesc);
        tilIncidentLocation = findViewById(R.id.tilIncidentLocation);

        etIncidentTitle = findViewById(R.id.etIncidentTitle);
        etIncidentDesc = findViewById(R.id.etIncidentDesc);
        etIncidentLocation = findViewById(R.id.etIncidentLocation);

        btnUseCurrentLocation = findViewById(R.id.btnUseCurrentLocation);
        btnPickOnMap = findViewById(R.id.btnPickOnMap);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);

        cardPhotoUpload = findViewById(R.id.cardPhotoUpload);
        tvPhotoFileName = findViewById(R.id.tvPhotoFileName);
        tvIncidentDateTime = findViewById(R.id.tvIncidentDateTime);
        ivUploadedPhoto = findViewById(R.id.ivUploadedPhoto);

        tvIncidentDateTime.setText("Incident Timestamp: " + DateTimeUtils.getCurrentFormattedDateTime());
    }

    private void setupCategorySpinner() {
        List<EmergencyCategory> categories = EmergencyDataRepository.getInstance().getCategories();
        ArrayAdapter<EmergencyCategory> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spinnerCategory.setAdapter(adapter);

        // Pre-select if passed from Home category cards
        String preSelected = getIntent().getStringExtra("selected_category");
        if (preSelected != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getName().equalsIgnoreCase(preSelected)) {
                    spinnerCategory.setSelection(i);
                    etIncidentTitle.setText(preSelected + " Emergency at ");
                    etIncidentDesc.setText(categories.get(i).getDefaultDescription() + " - Immediate response requested.");
                    break;
                }
            }
        }
    }

    private void setupListeners() {
        toolbarReport.setNavigationOnClickListener(v -> finish());

        btnUseCurrentLocation.setOnClickListener(v -> {
            etIncidentLocation.setText("100 Feet Rd, Indiranagar, Bengaluru");
            currentLatitude = 12.9716;
            currentLongitude = 77.5946;
            Toast.makeText(this, "Current GPS location applied (12.9716° N, 77.5946° E)", Toast.LENGTH_SHORT).show();
        });

        btnPickOnMap.setOnClickListener(v -> {
            Intent intent = new Intent(ReportEmergencyActivity.this, EmergencyLocationActivity.class);
            String currentLoc = etIncidentLocation.getText() != null ? etIncidentLocation.getText().toString().trim() : "";
            intent.putExtra(EmergencyLocationActivity.EXTRA_ADDRESS, currentLoc);
            intent.putExtra(EmergencyLocationActivity.EXTRA_LAT, currentLatitude);
            intent.putExtra(EmergencyLocationActivity.EXTRA_LNG, currentLongitude);
            startActivityForResult(intent, REQUEST_LOCATION_PICK);
        });

        cardPhotoUpload.setOnClickListener(v -> {
            // Toggle or simulate photo upload
            if (selectedPhotoName.contains("evidence_01")) {
                selectedPhotoName = "damage_photo_captured.jpg (3.4 MB)";
                tvPhotoFileName.setText(selectedPhotoName);
                Toast.makeText(this, "Incident photo attached successfully", Toast.LENGTH_SHORT).show();
            } else {
                selectedPhotoName = "incident_evidence_01.jpg (2.1 MB)";
                tvPhotoFileName.setText(selectedPhotoName);
                Toast.makeText(this, "Photo updated", Toast.LENGTH_SHORT).show();
            }
        });

        tvIncidentDateTime.setOnClickListener(v -> showDateTimePicker());

        btnSubmitReport.setOnClickListener(v -> submitEmergencyReport());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_PICK && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra(EmergencyLocationActivity.EXTRA_ADDRESS);
            currentLatitude = data.getDoubleExtra(EmergencyLocationActivity.EXTRA_LAT, 12.9716);
            currentLongitude = data.getDoubleExtra(EmergencyLocationActivity.EXTRA_LNG, 77.5946);

            if (address != null && !address.isEmpty()) {
                etIncidentLocation.setText(address);
                Toast.makeText(this, "Coordinates updated from map pinpoint", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            incidentCalendar.set(Calendar.YEAR, year);
            incidentCalendar.set(Calendar.MONTH, month);
            incidentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                incidentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                incidentCalendar.set(Calendar.MINUTE, minute);
                tvIncidentDateTime.setText("Incident Timestamp: " + DateTimeUtils.getCurrentFormattedDateTime());
            }, incidentCalendar.get(Calendar.HOUR_OF_DAY), incidentCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, incidentCalendar.get(Calendar.YEAR), incidentCalendar.get(Calendar.MONTH), incidentCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void submitEmergencyReport() {
        String title = etIncidentTitle.getText() != null ? etIncidentTitle.getText().toString().trim() : "";
        String desc = etIncidentDesc.getText() != null ? etIncidentDesc.getText().toString().trim() : "";
        String location = etIncidentLocation.getText() != null ? etIncidentLocation.getText().toString().trim() : "";

        tilIncidentTitle.setError(null);
        tilIncidentDesc.setError(null);
        tilIncidentLocation.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(title)) {
            tilIncidentTitle.setError("Incident title is required");
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(desc)) {
            tilIncidentDesc.setError("Detailed description is required");
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(location)) {
            tilIncidentLocation.setError("Incident location is required");
            isValid = false;
        }

        if (isValid) {
            EmergencyCategory selectedCat = (EmergencyCategory) spinnerCategory.getSelectedItem();
            String categoryName = selectedCat != null ? selectedCat.getName() : "General Emergency";
            int iconRes = selectedCat != null ? selectedCat.getIconResId() : R.drawable.ic_nav_report;

            String newReportId = "EMG-2026-" + (int)(1000 + Math.random() * 9000);

            EmergencyReport report = new EmergencyReport(
                    newReportId,
                    title,
                    categoryName,
                    iconRes,
                    desc,
                    location,
                    currentLatitude,
                    currentLongitude,
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    "Reported",
                    "Disaster Response Quick Dispatch #3",
                    "Assigned to Station Watch Officer",
                    "Triage Evaluation",
                    selectedPhotoName
            );

            EmergencyDataRepository.getInstance().addReport(report);

            new AlertDialog.Builder(this)
                    .setTitle("Emergency Report Submitted")
                    .setMessage("Incident logged with ID " + newReportId + ".\n\nYour report has been queued for immediate verification and emergency unit dispatch.")
                    .setPositiveButton("View Live Status", (dialog, which) -> {
                        Intent intent = new Intent(ReportEmergencyActivity.this, EmergencyReportDetailsActivity.class);
                        intent.putExtra("report_id", newReportId);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Done", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        }
    }
}
