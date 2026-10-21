package com.emergency.response.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.adapters.TimelineAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.google.android.material.button.MaterialButton;

public class EmergencyReportDetailsActivity extends AppCompatActivity {

    private Toolbar toolbarDetails;
    private ImageView ivDetailCategoryIcon;
    private TextView tvDetailCategory, tvDetailStatus, tvDetailTitle, tvDetailMeta;
    private TextView tvResponderUnit, tvOfficerName, tvEta;
    private TextView tvDetailDescription, tvDetailLocationAddress, tvDetailCoordinates;
    private MaterialButton btnCallResponder, btnShareIncident;
    private RecyclerView rvTimelineSteps;

    private EmergencyReport currentReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_report_details);

        initViews();
        loadReportData();
        setupListeners();
    }

    private void initViews() {
        toolbarDetails = findViewById(R.id.toolbarDetails);
        ivDetailCategoryIcon = findViewById(R.id.ivDetailCategoryIcon);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailMeta = findViewById(R.id.tvDetailMeta);

        tvResponderUnit = findViewById(R.id.tvResponderUnit);
        tvOfficerName = findViewById(R.id.tvOfficerName);
        tvEta = findViewById(R.id.tvEta);

        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailLocationAddress = findViewById(R.id.tvDetailLocationAddress);
        tvDetailCoordinates = findViewById(R.id.tvDetailCoordinates);

        btnCallResponder = findViewById(R.id.btnCallResponder);
        btnShareIncident = findViewById(R.id.btnShareIncident);
        rvTimelineSteps = findViewById(R.id.rvTimelineSteps);
    }

    private void loadReportData() {
        String reportId = getIntent().getStringExtra("report_id");
        if (reportId == null || reportId.isEmpty()) {
            reportId = "EMG-2026-8942"; // fallback to sample
        }

        currentReport = EmergencyDataRepository.getInstance().getReportById(reportId);
        if (currentReport == null) {
            currentReport = EmergencyDataRepository.getInstance().getAllReports().get(0);
        }

        ivDetailCategoryIcon.setImageResource(currentReport.getCategoryIconRes());
        tvDetailCategory.setText(currentReport.getCategory());
        tvDetailTitle.setText(currentReport.getTitle());
        tvDetailMeta.setText("Report ID: #" + currentReport.getId() + " • " + currentReport.getReportedDate());

        tvDetailStatus.setText(currentReport.getStatus());
        applyStatusColor(currentReport.getStatus());

        tvResponderUnit.setText(currentReport.getResponderUnit());
        tvOfficerName.setText(currentReport.getOfficerName());
        tvEta.setText("Status: " + currentReport.getEta());

        tvDetailDescription.setText(currentReport.getDescription());
        tvDetailLocationAddress.setText(currentReport.getLocation());
        tvDetailCoordinates.setText(String.format("GPS Coordinates: %.4f° N, %.4f° E", currentReport.getLatitude(), currentReport.getLongitude()));

        // Setup 5-step progress timeline
        rvTimelineSteps.setLayoutManager(new LinearLayoutManager(this));
        TimelineAdapter timelineAdapter = new TimelineAdapter(this, currentReport.getTimelineSteps());
        rvTimelineSteps.setAdapter(timelineAdapter);
    }

    private void applyStatusColor(String status) {
        int textColor;
        int bgColor;

        switch (status.toLowerCase()) {
            case "reported":
                textColor = ContextCompat.getColor(this, R.color.status_reported);
                bgColor = ContextCompat.getColor(this, R.color.status_reported_bg);
                break;
            case "verified":
                textColor = ContextCompat.getColor(this, R.color.status_verified);
                bgColor = ContextCompat.getColor(this, R.color.status_verified_bg);
                break;
            case "assigned":
                textColor = ContextCompat.getColor(this, R.color.status_assigned);
                bgColor = ContextCompat.getColor(this, R.color.status_assigned_bg);
                break;
            case "responding":
                textColor = ContextCompat.getColor(this, R.color.status_responding);
                bgColor = ContextCompat.getColor(this, R.color.status_responding_bg);
                break;
            case "resolved":
            default:
                textColor = ContextCompat.getColor(this, R.color.status_resolved);
                bgColor = ContextCompat.getColor(this, R.color.status_resolved_bg);
                break;
        }

        tvDetailStatus.setTextColor(textColor);
        tvDetailStatus.setBackgroundTintList(ColorStateList.valueOf(bgColor));
    }

    private void setupListeners() {
        toolbarDetails.setNavigationOnClickListener(v -> finish());

        btnCallResponder.setOnClickListener(v -> {
            try {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:112"));
                startActivity(dialIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Connecting to Emergency Services Dispatcher (112)", Toast.LENGTH_SHORT).show();
            }
        });

        btnShareIncident.setOnClickListener(v -> {
            if (currentReport != null) {
                String shareText = "EMERGENCY INCIDENT ALERT [" + currentReport.getId() + "]\n"
                        + "Category: " + currentReport.getCategory() + "\n"
                        + "Title: " + currentReport.getTitle() + "\n"
                        + "Location: " + currentReport.getLocation() + "\n"
                        + "Status: " + currentReport.getStatus() + "\n"
                        + "Reported via AI-Powered Emergency Response Platform";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Emergency Alert - " + currentReport.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share Emergency Details"));
            }
        });
    }
}
