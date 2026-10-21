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
import com.emergency.response.adapters.NotificationAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.AppNotification;

import java.util.List;

public class AuthorityNotificationsFragment extends Fragment {

    private TextView tvAuthNotifsSubtitle;
    private RecyclerView rvAuthorityNotifsList;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authority_notifications, container, false);
        tvAuthNotifsSubtitle = view.findViewById(R.id.tvAuthNotifsSubtitle);
        rvAuthorityNotifsList = view.findViewById(R.id.rvAuthorityNotifsList);

        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNotifications();
    }

    private void setupRecyclerView() {
        List<AppNotification> list = EmergencyDataRepository.getInstance().getNotificationsForRole("AUTHORITY");
        adapter = new NotificationAdapter(requireContext(), list, notif -> {
            EmergencyDataRepository.getInstance().markNotificationRead(notif.getId());
            Intent intent = new Intent(requireContext(), AuthorityEmergencyDetailsActivity.class);
            intent.putExtra("report_id", notif.getReportId());
            startActivity(intent);
        });
        rvAuthorityNotifsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAuthorityNotifsList.setAdapter(adapter);
    }

    private void refreshNotifications() {
        List<AppNotification> list = EmergencyDataRepository.getInstance().getNotificationsForRole("AUTHORITY");
        if (adapter != null) {
            adapter.updateData(list);
        }
        tvAuthNotifsSubtitle.setText("Queue: " + list.size() + " active incoming incidents");
    }
}
