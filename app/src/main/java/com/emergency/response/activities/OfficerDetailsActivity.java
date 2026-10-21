package com.emergency.response.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.Officer;
import com.google.android.material.button.MaterialButton;

public class OfficerDetailsActivity extends AppCompatActivity {

    private ImageView btnBackOfficerDetails, ivOfficerDossierPhoto, ivDossierGenderSymbol;
    private TextView tvDossierName, tvDossierDesignation, tvDossierDepartmentTag, tvDossierAvailability;
    private TextView tvFieldEmployeeId, tvFieldAge, tvFieldGender, tvFieldExperience, tvFieldPhone, tvFieldEmail;
    private MaterialButton btnCallOfficerDirect;

    private String officerId;
    private Officer currentOfficer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_details);

        officerId = getIntent().getStringExtra("officer_id");
        if (officerId == null) officerId = "OFF-4092";

        initViews();
        setupListeners();
        loadOfficerProfile();
    }

    private void initViews() {
        btnBackOfficerDetails = findViewById(R.id.btnBackOfficerDetails);
        ivOfficerDossierPhoto = findViewById(R.id.ivOfficerDossierPhoto);
        ivDossierGenderSymbol = findViewById(R.id.ivDossierGenderSymbol);
        tvDossierName = findViewById(R.id.tvDossierName);
        tvDossierDesignation = findViewById(R.id.tvDossierDesignation);
        tvDossierDepartmentTag = findViewById(R.id.tvDossierDepartmentTag);
        tvDossierAvailability = findViewById(R.id.tvDossierAvailability);

        tvFieldEmployeeId = findViewById(R.id.tvFieldEmployeeId);
        tvFieldAge = findViewById(R.id.tvFieldAge);
        tvFieldGender = findViewById(R.id.tvFieldGender);
        tvFieldExperience = findViewById(R.id.tvFieldExperience);
        tvFieldPhone = findViewById(R.id.tvFieldPhone);
        tvFieldEmail = findViewById(R.id.tvFieldEmail);

        btnCallOfficerDirect = findViewById(R.id.btnCallOfficerDirect);
    }

    private void setupListeners() {
        btnBackOfficerDetails.setOnClickListener(v -> finish());

        btnCallOfficerDirect.setOnClickListener(v -> {
            if (currentOfficer != null && currentOfficer.getPhone() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentOfficer.getPhone()));
                startActivity(dialIntent);
            } else {
                Toast.makeText(this, "Officer phone line not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOfficerProfile() {
        currentOfficer = EmergencyDataRepository.getInstance().getOfficerById(officerId);
        if (currentOfficer == null) {
            Toast.makeText(this, "Officer not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvDossierName.setText(currentOfficer.getName());
        tvDossierDesignation.setText(currentOfficer.getDesignation());
        tvDossierDepartmentTag.setText(currentOfficer.getDepartment());

        ivDossierGenderSymbol.setImageResource(currentOfficer.getGenderIconRes());

        tvDossierAvailability.setText("Status: " + currentOfficer.getAvailability());
        if ("Available".equalsIgnoreCase(currentOfficer.getAvailability())) {
            tvDossierAvailability.setTextColor(ContextCompat.getColor(this, R.color.officer_available));
            tvDossierAvailability.setBackgroundColor(ContextCompat.getColor(this, R.color.officer_available_bg));
        } else if ("On Duty".equalsIgnoreCase(currentOfficer.getAvailability())) {
            tvDossierAvailability.setTextColor(ContextCompat.getColor(this, R.color.officer_on_duty));
            tvDossierAvailability.setBackgroundColor(ContextCompat.getColor(this, R.color.officer_on_duty_bg));
        } else {
            tvDossierAvailability.setTextColor(ContextCompat.getColor(this, R.color.officer_off_duty));
            tvDossierAvailability.setBackgroundColor(ContextCompat.getColor(this, R.color.officer_off_duty_bg));
        }

        tvFieldEmployeeId.setText(currentOfficer.getEmployeeId());
        tvFieldAge.setText(currentOfficer.getAge() + " Years Old");
        tvFieldGender.setText(currentOfficer.getGender());
        tvFieldExperience.setText(currentOfficer.getExperience());
        tvFieldPhone.setText(currentOfficer.getPhone());
        tvFieldEmail.setText(currentOfficer.getEmail());
    }
}
