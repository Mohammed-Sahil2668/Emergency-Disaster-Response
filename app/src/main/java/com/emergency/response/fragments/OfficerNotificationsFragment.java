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
import com.emergency.response.activities.OfficerTaskDetailsActivity;
import com.emergency.response.adapters.NotificationAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.AppNotification;

import java.util.List;

public class OfficerNotificationsFragment extends Fragment {

    private TextView tvOfficerNotifsSubtitle;
    private RecyclerView rvOfficerNotifsList;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_officer_notifications, container, false);
        tvOfficerNotifsSubtitle = view.findViewById(R.id.tvOfficerNotifsSubtitle);
        rvOfficerNotifsList = view.findViewById(R.id.rvOfficerNotifsList);

        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNotifications();
    }

    private void setupRecyclerView() {
        List<AppNotification> list = EmergencyDataRepository.getInstance().getNotificationsForRole("OFFICER");
        adapter = new NotificationAdapter(requireContext(), list, notif -> {
            EmergencyDataRepository.getInstance().markNotificationRead(notif.getId());
            Intent intent = new Intent(requireContext(), OfficerTaskDetailsActivity.class);
            intent.putExtra("report_id", notif.getReportId());
            startActivity(intent);
        });
        rvOfficerNotifsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOfficerNotifsList.setAdapter(adapter);
    }

    private void refreshNotifications() {
        List<AppNotification> list = EmergencyDataRepository.getInstance().getNotificationsForRole("OFFICER");
        if (adapter != null) {
            adapter.updateData(list);
        }
        tvOfficerNotifsSubtitle.setText("Directives: " + list.size() + " active task alerts");
    }
}
