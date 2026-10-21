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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.activities.OfficerTaskDetailsActivity;
import com.emergency.response.adapters.NotificationAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.AppNotification;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.models.Officer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class OfficerDashboardFragment extends Fragment {

    private ImageView ivOfficerHeaderAvatar;
    private TextView tvOfficerHeaderGreeting, tvOfficerHeaderRole, tvOfficerHeaderAvailBadge;
    private SwitchMaterial switchOfficerDuty;
    private TextView tvToggleStatusSub;
    private TextView tvOfficerTotalTasks, tvOfficerNewTasks, tvOfficerActiveTasks, tvOfficerCompletedTasks;
    private MaterialCardView cardActiveDispatchBanner;
    private TextView tvBannerIncidentTitle, tvBannerIncidentStatus, tvBannerIncidentLocation;
    private MaterialButton btnOpenActiveTaskDetails;
    private RecyclerView rvOfficerRecentNotifs;

    private NotificationAdapter notificationAdapter;
    private Officer currentOfficer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_officer_dashboard, container, false);
        initViews(view);
        setupListeners();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshOfficerDashboard();
    }

    private void initViews(View view) {
        ivOfficerHeaderAvatar = view.findViewById(R.id.ivOfficerHeaderAvatar);
        tvOfficerHeaderGreeting = view.findViewById(R.id.tvOfficerHeaderGreeting);
        tvOfficerHeaderRole = view.findViewById(R.id.tvOfficerHeaderRole);
        tvOfficerHeaderAvailBadge = view.findViewById(R.id.tvOfficerHeaderAvailBadge);
        switchOfficerDuty = view.findViewById(R.id.switchOfficerDuty);
        tvToggleStatusSub = view.findViewById(R.id.tvToggleStatusSub);

        tvOfficerTotalTasks = view.findViewById(R.id.tvOfficerTotalTasks);
        tvOfficerNewTasks = view.findViewById(R.id.tvOfficerNewTasks);
        tvOfficerActiveTasks = view.findViewById(R.id.tvOfficerActiveTasks);
        tvOfficerCompletedTasks = view.findViewById(R.id.tvOfficerCompletedTasks);

        cardActiveDispatchBanner = view.findViewById(R.id.cardActiveDispatchBanner);
        tvBannerIncidentTitle = view.findViewById(R.id.tvBannerIncidentTitle);
        tvBannerIncidentStatus = view.findViewById(R.id.tvBannerIncidentStatus);
        tvBannerIncidentLocation = view.findViewById(R.id.tvBannerIncidentLocation);
        btnOpenActiveTaskDetails = view.findViewById(R.id.btnOpenActiveTaskDetails);

        rvOfficerRecentNotifs = view.findViewById(R.id.rvOfficerRecentNotifs);
    }

    private void setupListeners() {
        switchOfficerDuty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentOfficer != null) {
                String newStatus = isChecked ? "On Duty" : "Off Duty";
                currentOfficer.setAvailability(newStatus);
                EmergencyDataRepository.getInstance().updateOfficer(currentOfficer);
                tvOfficerHeaderAvailBadge.setText(newStatus.toUpperCase());
                tvToggleStatusSub.setText(isChecked ? "Active for incoming dispatch assignments" : "Off duty. Not accepting dispatches");
                Toast.makeText(requireContext(), "Duty status changed to " + newStatus, Toast.LENGTH_SHORT).show();
            }
        });

        btnOpenActiveTaskDetails.setOnClickListener(v -> {
            List<EmergencyReport> myTasks = EmergencyDataRepository.getInstance().getTasksForOfficer(currentOfficer != null ? currentOfficer.getId() : "OFF-4092");
            if (!myTasks.isEmpty()) {
                Intent intent = new Intent(requireContext(), OfficerTaskDetailsActivity.class);
                intent.putExtra("report_id", myTasks.get(0).getId());
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        List<AppNotification> notifs = EmergencyDataRepository.getInstance().getNotificationsForRole("OFFICER");
        notificationAdapter = new NotificationAdapter(requireContext(), notifs, notif -> {
            EmergencyDataRepository.getInstance().markNotificationRead(notif.getId());
            Intent intent = new Intent(requireContext(), OfficerTaskDetailsActivity.class);
            intent.putExtra("report_id", notif.getReportId());
            startActivity(intent);
        });
        rvOfficerRecentNotifs.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOfficerRecentNotifs.setAdapter(notificationAdapter);
    }

    private void refreshOfficerDashboard() {
        currentOfficer = EmergencyDataRepository.getInstance().getCurrentOfficerProfile();
        if (currentOfficer == null) {
            currentOfficer = EmergencyDataRepository.getInstance().getOfficerById("OFF-4092");
        }

        if (currentOfficer != null) {
            tvOfficerHeaderGreeting.setText(currentOfficer.getName());
            tvOfficerHeaderRole.setText(currentOfficer.getDesignation() + " • " + currentOfficer.getEmployeeId());
            tvOfficerHeaderAvailBadge.setText(currentOfficer.getAvailability().toUpperCase());
            switchOfficerDuty.setChecked(!"Off Duty".equalsIgnoreCase(currentOfficer.getAvailability()));
        }

        // Tasks calculation
        List<EmergencyReport> myTasks = EmergencyDataRepository.getInstance().getTasksForOfficer(currentOfficer != null ? currentOfficer.getId() : "OFF-4092");
        int total = myTasks.size();
        int assigned = 0;
        int active = 0;
        int resolved = 0;

        for (EmergencyReport r : myTasks) {
            if ("Assigned".equalsIgnoreCase(r.getStatus())) assigned++;
            else if ("Responding".equalsIgnoreCase(r.getStatus())) active++;
            else if ("Resolved".equalsIgnoreCase(r.getStatus())) resolved++;
        }

        tvOfficerTotalTasks.setText(String.valueOf(total));
        tvOfficerNewTasks.setText(String.valueOf(assigned));
        tvOfficerActiveTasks.setText(String.valueOf(active));
        tvOfficerCompletedTasks.setText(String.valueOf(resolved));

        // Active banner
        if (!myTasks.isEmpty()) {
            cardActiveDispatchBanner.setVisibility(View.VISIBLE);
            EmergencyReport primary = myTasks.get(0);
            tvBannerIncidentTitle.setText(primary.getTitle());
            tvBannerIncidentStatus.setText(primary.getStatus());
            tvBannerIncidentLocation.setText("📍 " + primary.getLocation());
        } else {
            cardActiveDispatchBanner.setVisibility(View.GONE);
        }

        // Notifications
        List<AppNotification> notifs = EmergencyDataRepository.getInstance().getNotificationsForRole("OFFICER");
        if (notificationAdapter != null) {
            notificationAdapter.updateData(notifs);
        }
    }
}
