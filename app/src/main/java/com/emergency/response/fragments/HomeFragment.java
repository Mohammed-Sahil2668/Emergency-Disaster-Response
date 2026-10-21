package com.emergency.response.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.activities.EmergencyReportDetailsActivity;
import com.emergency.response.activities.MainActivity;
import com.emergency.response.activities.ReportEmergencyActivity;
import com.emergency.response.adapters.CategoryAdapter;
import com.emergency.response.adapters.EmergencyReportAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyCategory;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.models.UserProfile;
import com.emergency.response.utils.DateTimeUtils;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvGreeting, tvCurrentLocationText, tvCurrentCoordinates;
    private ImageView btnRefreshLocation, ivHeaderProfile;
    private FrameLayout btnSos;
    private MaterialButton btnReportEmergencyAction;
    private View cardWeather;
    private TextView tvActiveCount, tvRespondingCount, tvResolvedCount, tvViewAllReports;
    private RecyclerView rvCategories, rvRecentReports;

    private CategoryAdapter categoryAdapter;
    private EmergencyReportAdapter recentReportsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupListeners();
        setupRecyclerViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDashboardData();
    }

    private void initViews(View view) {
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvCurrentLocationText = view.findViewById(R.id.tvCurrentLocationText);
        tvCurrentCoordinates = view.findViewById(R.id.tvCurrentCoordinates);
        btnRefreshLocation = view.findViewById(R.id.btnRefreshLocation);
        ivHeaderProfile = view.findViewById(R.id.ivHeaderProfile);
        btnSos = view.findViewById(R.id.btnSos);
        btnReportEmergencyAction = view.findViewById(R.id.btnReportEmergencyAction);
        cardWeather = view.findViewById(R.id.cardWeather);
        tvActiveCount = view.findViewById(R.id.tvActiveCount);
        tvRespondingCount = view.findViewById(R.id.tvRespondingCount);
        tvResolvedCount = view.findViewById(R.id.tvResolvedCount);
        tvViewAllReports = view.findViewById(R.id.tvViewAllReports);
        rvCategories = view.findViewById(R.id.rvCategories);
        rvRecentReports = view.findViewById(R.id.rvRecentReports);
    }

    private void setupListeners() {
        // SOS Button
        btnSos.setOnClickListener(v -> showSosConfirmDialog());

        // Report Emergency Action Button
        btnReportEmergencyAction.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ReportEmergencyActivity.class);
            startActivity(intent);
        });

        // Refresh Location Button
        btnRefreshLocation.setOnClickListener(v -> {
            tvCurrentLocationText.setText("100 Feet Rd, HAL 2nd Stage, Indiranagar, Bengaluru");
            tvCurrentCoordinates.setText("12.9716° N, 77.5946° E (GPS Locked ±3m)");
            Toast.makeText(requireContext(), "GPS coordinates refreshed successfully", Toast.LENGTH_SHORT).show();
        });

        // Profile shortcut in header
        ivHeaderProfile.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_profile);
            }
        });

        // Weather card click
        cardWeather.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Weather Advisory: Monsoon depression active. Rain intensity high.", Toast.LENGTH_LONG).show();
        });

        // View All Reports link
        tvViewAllReports.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_my_reports);
            }
        });
    }

    private void setupRecyclerViews() {
        // 9 Categories in 3 Columns
        List<EmergencyCategory> categories = EmergencyDataRepository.getInstance().getCategories();
        categoryAdapter = new CategoryAdapter(requireContext(), categories, category -> {
            Intent intent = new Intent(requireContext(), ReportEmergencyActivity.class);
            intent.putExtra("selected_category", category.getName());
            startActivity(intent);
        });
        rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvCategories.setAdapter(categoryAdapter);

        // Recent Reports List
        List<EmergencyReport> recentReports = EmergencyDataRepository.getInstance().getRecentReports();
        recentReportsAdapter = new EmergencyReportAdapter(requireContext(), recentReports, report -> {
            Intent intent = new Intent(requireContext(), EmergencyReportDetailsActivity.class);
            intent.putExtra("report_id", report.getId());
            startActivity(intent);
        });
        rvRecentReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRecentReports.setAdapter(recentReportsAdapter);
    }

    private void refreshDashboardData() {
        UserProfile profile = EmergencyDataRepository.getInstance().getUserProfile();
        if (profile != null && profile.getName() != null) {
            tvGreeting.setText("Welcome, " + profile.getName());
        }

        EmergencyDataRepository repo = EmergencyDataRepository.getInstance();
        tvActiveCount.setText(String.valueOf(repo.getActiveCount()));
        tvRespondingCount.setText(String.valueOf(repo.getRespondingCount()));
        tvResolvedCount.setText(String.valueOf(repo.getResolvedCount()));

        if (recentReportsAdapter != null) {
            recentReportsAdapter.updateData(repo.getRecentReports());
        }
    }

    private void showSosConfirmDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_emergency_sos);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        RadioGroup rgSosType = dialog.findViewById(R.id.rgSosType);
        MaterialButton btnCancel = dialog.findViewById(R.id.btnCancelSos);
        MaterialButton btnConfirm = dialog.findViewById(R.id.btnConfirmSos);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();

            String sosCategory = "Medical Emergencies";
            int iconRes = R.drawable.ic_medical_emergency;
            int selectedId = rgSosType.getCheckedRadioButtonId();

            if (selectedId == R.id.rbFire) {
                sosCategory = "Fire Accidents";
                iconRes = R.drawable.ic_fire_accident;
            } else if (selectedId == R.id.rbDisaster) {
                sosCategory = "Flood";
                iconRes = R.drawable.ic_flood;
            }

            // Create High Priority SOS Report in repository
            String reportId = "SOS-" + System.currentTimeMillis() % 10000;
            EmergencyReport sosReport = new EmergencyReport(
                    reportId,
                    "URGENT CITIZEN SOS DISTRESS SIGNAL",
                    sosCategory,
                    iconRes,
                    "Automated high-priority citizen SOS broadcast initiated from mobile coordinates. Immediate dispatch requested.",
                    "100 Feet Rd, Indiranagar, Bengaluru",
                    12.9716, 77.5946,
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    "Responding",
                    "Disaster Quick Response Unit #1 (Fast Dispatch)",
                    "Officer In-Charge Dispatched",
                    "4–6 Minutes (ETA)",
                    "sos_signal.png"
            );

            EmergencyDataRepository.getInstance().addReport(sosReport);
            refreshDashboardData();

            Toast.makeText(requireContext(), "EMERGENCY SOS BROADCASTED TO COMMAND CENTER!", Toast.LENGTH_LONG).show();

            // Open details immediately
            Intent intent = new Intent(requireContext(), EmergencyReportDetailsActivity.class);
            intent.putExtra("report_id", reportId);
            startActivity(intent);
        });

        dialog.show();
    }
}
