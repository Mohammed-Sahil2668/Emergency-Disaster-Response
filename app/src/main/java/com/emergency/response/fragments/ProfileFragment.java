package com.emergency.response.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.emergency.response.R;
import com.emergency.response.activities.LoginActivity;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.UserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileEmail, tvProfileMobile;
    private TextView tvProfileEmergencyContact, tvProfileBloodGroup, btnEditProfile;
    private SwitchCompat switchNotifications, switchSiren, switchGps;
    private MaterialButton btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setupListeners();
        loadProfileData();
        return view;
    }

    private void initViews(View view) {
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfileMobile = view.findViewById(R.id.tvProfileMobile);
        tvProfileEmergencyContact = view.findViewById(R.id.tvProfileEmergencyContact);
        tvProfileBloodGroup = view.findViewById(R.id.tvProfileBloodGroup);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        switchNotifications = view.findViewById(R.id.switchNotifications);
        switchSiren = view.findViewById(R.id.switchSiren);
        switchGps = view.findViewById(R.id.switchGps);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String state = isChecked ? "enabled" : "muted";
            Toast.makeText(requireContext(), "Disaster notifications " + state, Toast.LENGTH_SHORT).show();
        });

        switchSiren.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String state = isChecked ? "enabled" : "disabled";
            Toast.makeText(requireContext(), "Emergency SOS Siren " + state, Toast.LENGTH_SHORT).show();
        });

        switchGps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String state = isChecked ? "High Accuracy" : "Battery Saving";
            Toast.makeText(requireContext(), "GPS Mode set to " + state, Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void loadProfileData() {
        UserProfile profile = EmergencyDataRepository.getInstance().getUserProfile();
        if (profile != null) {
            tvProfileName.setText(profile.getName());
            tvProfileEmail.setText(profile.getEmail());
            tvProfileMobile.setText(profile.getPhone());
            tvProfileEmergencyContact.setText(profile.getEmergencyContact());
            tvProfileBloodGroup.setText(profile.getBloodGroup());
        }
    }

    private void showEditProfileDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_profile);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextInputEditText etEditName = dialog.findViewById(R.id.etEditName);
        TextInputEditText etEditMobile = dialog.findViewById(R.id.etEditMobile);
        TextInputEditText etEditEmergencyContact = dialog.findViewById(R.id.etEditEmergencyContact);
        TextInputEditText etEditBloodGroup = dialog.findViewById(R.id.etEditBloodGroup);
        MaterialButton btnCancelEdit = dialog.findViewById(R.id.btnCancelEdit);
        MaterialButton btnSaveProfile = dialog.findViewById(R.id.btnSaveProfile);

        UserProfile current = EmergencyDataRepository.getInstance().getUserProfile();
        if (current != null) {
            etEditName.setText(current.getName());
            etEditMobile.setText(current.getPhone());
            etEditEmergencyContact.setText(current.getEmergencyContact());
            etEditBloodGroup.setText(current.getBloodGroup());
        }

        btnCancelEdit.setOnClickListener(v -> dialog.dismiss());

        btnSaveProfile.setOnClickListener(v -> {
            String newName = etEditName.getText() != null ? etEditName.getText().toString().trim() : "";
            String newMobile = etEditMobile.getText() != null ? etEditMobile.getText().toString().trim() : "";
            String newEmergency = etEditEmergencyContact.getText() != null ? etEditEmergencyContact.getText().toString().trim() : "";
            String newBlood = etEditBloodGroup.getText() != null ? etEditBloodGroup.getText().toString().trim() : "";

            if (newName.isEmpty()) {
                etEditName.setError("Name cannot be empty");
                return;
            }

            UserProfile updated = new UserProfile(
                    newName,
                    current != null ? current.getEmail() : "sahil.sharma@example.com",
                    newMobile,
                    newEmergency,
                    newBlood,
                    current != null ? current.getAddress() : "Bengaluru"
            );

            EmergencyDataRepository.getInstance().updateUserProfile(updated);
            loadProfileData();
            dialog.dismiss();
            Toast.makeText(requireContext(), "Profile details updated successfully", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to sign out from the Emergency Response platform?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    SharedPreferences prefs = requireContext().getSharedPreferences("emergency_prefs", Context.MODE_PRIVATE);
                    prefs.edit().clear().apply();

                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
