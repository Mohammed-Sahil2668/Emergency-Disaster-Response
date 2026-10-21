package com.emergency.response.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.emergency.response.R;
import com.emergency.response.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class AuthorityProfileFragment extends Fragment {

    private MaterialButton btnAuthorityLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authority_profile, container, false);
        btnAuthorityLogout = view.findViewById(R.id.btnAuthorityLogout);

        btnAuthorityLogout.setOnClickListener(v -> showLogoutDialog());
        return view;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout Authority Session")
                .setMessage("Are you sure you want to end your Emergency Operations Authority session?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
