package com.emergency.response.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.activities.AuthorityEmergencyDetailsActivity;
import com.emergency.response.activities.AuthorityMainActivity;
import com.emergency.response.adapters.NotificationAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.AppNotification;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AuthorityDashboardFragment extends Fragment {

    private TextView tvKpiTotal, tvKpiNew, tvKpiVerified, tvKpiAssigned, tvKpiResponding, tvKpiResolved;
    private TextView tvAvailableOfficersCount;
    private MaterialButton btnManageOfficers;
    private MaterialCardView cardAuthorityAlertRibbon;
    private TextView tvRecentAlertTitle, tvRecentAlertSub;
    private TextView tvViewAllAuthorityNotifs;
    private RecyclerView rvAuthorityRecentNotifs;

    private NotificationAdapter notificationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authority_dashboard, container, false);
        initViews(view);
        setupListeners();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshKpis();
    }

    private void initViews(View view) {
        tvKpiTotal = view.findViewById(R.id.tvKpiTotal);
        tvKpiNew = view.findViewById(R.id.tvKpiNew);
        tvKpiVerified = view.findViewById(R.id.tvKpiVerified);
        tvKpiAssigned = view.findViewById(R.id.tvKpiAssigned);
        tvKpiResponding = view.findViewById(R.id.tvKpiResponding);
        tvKpiResolved = view.findViewById(R.id.tvKpiResolved);
        tvAvailableOfficersCount = view.findViewById(R.id.tvAvailableOfficersCount);
        btnManageOfficers = view.findViewById(R.id.btnManageOfficers);
        cardAuthorityAlertRibbon = view.findViewById(R.id.cardAuthorityAlertRibbon);
        tvRecentAlertTitle = view.findViewById(R.id.tvRecentAlertTitle);
        tvRecentAlertSub = view.findViewById(R.id.tvRecentAlertSub);
        tvViewAllAuthorityNotifs = view.findViewById(R.id.tvViewAllAuthorityNotifs);
        rvAuthorityRecentNotifs = view.findViewById(R.id.rvAuthorityRecentNotifs);
    }

    private void setupListeners() {
        btnManageOfficers.setOnClickListener(v -> {
            if (getActivity() instanceof AuthorityMainActivity) {
                ((AuthorityMainActivity) getActivity()).navigateToTab(R.id.nav_auth_officers);
            }
        });

        tvViewAllAuthorityNotifs.setOnClickListener(v -> {
            if (getActivity() instanceof AuthorityMainActivity) {
                ((AuthorityMainActivity) getActivity()).navigateToTab(R.id.nav_auth_notifications);
            }
        });

        cardAuthorityAlertRibbon.setOnClickListener(v -> {
            List<AppNotification> notifs = EmergencyDataRepository.getInstance().getNotificationsForRole("AUTHORITY");
            if (!notifs.isEmpty()) {
                Intent intent = new Intent(requireContext(), AuthorityEmergencyDetailsActivity.class);
                intent.putExtra("report_id", notifs.get(0).getReportId());
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        List<AppNotification> notifs = EmergencyDataRepository.getInstance().getNotificationsForRole("AUTHORITY");
        int limit = Math.min(notifs.size(), 3);
        List<AppNotification> sublist = notifs.subList(0, limit);

        notificationAdapter = new NotificationAdapter(requireContext(), sublist, notif -> {
            EmergencyDataRepository.getInstance().markNotificationRead(notif.getId());
            Intent intent = new Intent(requireContext(), AuthorityEmergencyDetailsActivity.class);
            intent.putExtra("report_id", notif.getReportId());
            startActivity(intent);
        });

        rvAuthorityRecentNotifs.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAuthorityRecentNotifs.setAdapter(notificationAdapter);
    }

    private void refreshKpis() {
        EmergencyDataRepository repo = EmergencyDataRepository.getInstance();
        tvKpiTotal.setText(String.valueOf(repo.getTotalCount()));
        tvKpiNew.setText(String.valueOf(repo.getNewReportedCount()));
        tvKpiVerified.setText(String.valueOf(repo.getVerifiedCount()));
        tvKpiAssigned.setText(String.valueOf(repo.getAssignedCount()));
        tvKpiResponding.setText(String.valueOf(repo.getRespondingCount()));
        tvKpiResolved.setText(String.valueOf(repo.getResolvedCount()));

        int available = repo.getAvailableOfficers().size();
        tvAvailableOfficersCount.setText(available + " Officers Available For Dispatch");

        List<AppNotification> notifs = repo.getNotificationsForRole("AUTHORITY");
        if (!notifs.isEmpty()) {
            tvRecentAlertTitle.setText(notifs.get(0).getTitle());
            tvRecentAlertSub.setText(notifs.get(0).getMessage());
            int limit = Math.min(notifs.size(), 3);
            if (notificationAdapter != null) {
                notificationAdapter.updateData(notifs.subList(0, limit));
            }
        }
    }
}
