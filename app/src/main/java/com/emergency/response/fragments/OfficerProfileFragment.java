package com.emergency.response.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.emergency.response.R;
import com.emergency.response.activities.LoginActivity;
import com.emergency.response.activities.OfficerEditProfileActivity;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.Officer;
import com.google.android.material.button.MaterialButton;

public class OfficerProfileFragment extends Fragment {

    private ImageView ivOfficerProfileAvatar, ivOfficerProfileGender;
    private TextView tvOfficerProfileName, tvOfficerProfileDesignation, tvOfficerProfileDepartment, tvOfficerProfileAvailabilityBadge;
    private TextView tvOfficerProfilePhone, tvOfficerProfileEmail, tvOfficerProfileExperience;
    private MaterialButton btnEditOfficerProfile, btnOfficerLogout;

    private Officer currentOfficer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_officer_profile, container, false);
        initViews(view);
        setupListeners();
        loadProfileData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileData();
    }

    private void initViews(View view) {
        ivOfficerProfileAvatar = view.findViewById(R.id.ivOfficerProfileAvatar);
        ivOfficerProfileGender = view.findViewById(R.id.ivOfficerProfileGender);
        tvOfficerProfileName = view.findViewById(R.id.tvOfficerProfileName);
        tvOfficerProfileDesignation = view.findViewById(R.id.tvOfficerProfileDesignation);
        tvOfficerProfileDepartment = view.findViewById(R.id.tvOfficerProfileDepartment);
        tvOfficerProfileAvailabilityBadge = view.findViewById(R.id.tvOfficerProfileAvailabilityBadge);

        tvOfficerProfilePhone = view.findViewById(R.id.tvOfficerProfilePhone);
        tvOfficerProfileEmail = view.findViewById(R.id.tvOfficerProfileEmail);
        tvOfficerProfileExperience = view.findViewById(R.id.tvOfficerProfileExperience);

        btnEditOfficerProfile = view.findViewById(R.id.btnEditOfficerProfile);
        btnOfficerLogout = view.findViewById(R.id.btnOfficerLogout);
    }

    private void setupListeners() {
        btnEditOfficerProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), OfficerEditProfileActivity.class);
            startActivity(intent);
        });

        btnOfficerLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void loadProfileData() {
        currentOfficer = EmergencyDataRepository.getInstance().getCurrentOfficerProfile();
        if (currentOfficer == null) {
            currentOfficer = EmergencyDataRepository.getInstance().getOfficerById("OFF-4092");
        }

        if (currentOfficer != null) {
            tvOfficerProfileName.setText(currentOfficer.getName());
            tvOfficerProfileDesignation.setText(currentOfficer.getDesignation() + " • " + currentOfficer.getEmployeeId());
            tvOfficerProfileDepartment.setText(currentOfficer.getDepartment());
            tvOfficerProfileAvailabilityBadge.setText("STATUS: " + currentOfficer.getAvailability().toUpperCase());

            ivOfficerProfileGender.setImageResource(currentOfficer.getGenderIconRes());

            tvOfficerProfilePhone.setText(currentOfficer.getPhone());
            tvOfficerProfileEmail.setText(currentOfficer.getEmail());
            tvOfficerProfileExperience.setText(currentOfficer.getExperience() + " Active Service");
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout Officer Session")
                .setMessage("Are you sure you want to end your Field Response Officer shift?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    Toast.makeText(requireContext(), "Officer logged out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
