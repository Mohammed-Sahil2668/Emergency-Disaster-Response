package com.emergency.response.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.activities.OfficerTaskDetailsActivity;
import com.emergency.response.adapters.EmergencyReportAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.models.Officer;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class OfficerTasksFragment extends Fragment {

    private TextView tvOfficerTasksSubtitle;
    private Chip chipTaskAll, chipTaskAssigned, chipTaskResponding, chipTaskCompleted;
    private RecyclerView rvOfficerTasksList;
    private LinearLayout layoutOfficerTasksEmpty;

    private EmergencyReportAdapter adapter;
    private String currentFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_officer_tasks, container, false);
        initViews(view);
        setupListeners();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyFilter(currentFilter);
    }

    private void initViews(View view) {
        tvOfficerTasksSubtitle = view.findViewById(R.id.tvOfficerTasksSubtitle);
        chipTaskAll = view.findViewById(R.id.chipTaskAll);
        chipTaskAssigned = view.findViewById(R.id.chipTaskAssigned);
        chipTaskResponding = view.findViewById(R.id.chipTaskResponding);
        chipTaskCompleted = view.findViewById(R.id.chipTaskCompleted);
        rvOfficerTasksList = view.findViewById(R.id.rvOfficerTasksList);
        layoutOfficerTasksEmpty = view.findViewById(R.id.layoutOfficerTasksEmpty);
    }

    private void setupListeners() {
        chipTaskAll.setOnClickListener(v -> applyFilter("All"));
        chipTaskAssigned.setOnClickListener(v -> applyFilter("Assigned"));
        chipTaskResponding.setOnClickListener(v -> applyFilter("Responding"));
        chipTaskCompleted.setOnClickListener(v -> applyFilter("Resolved"));
    }

    private void setupRecyclerView() {
        Officer currentOfficer = EmergencyDataRepository.getInstance().getCurrentOfficerProfile();
        String officerId = currentOfficer != null ? currentOfficer.getId() : "OFF-4092";
        List<EmergencyReport> myTasks = EmergencyDataRepository.getInstance().getTasksForOfficer(officerId);

        adapter = new EmergencyReportAdapter(requireContext(), myTasks, report -> {
            Intent intent = new Intent(requireContext(), OfficerTaskDetailsActivity.class);
            intent.putExtra("report_id", report.getId());
            startActivity(intent);
        });

        rvOfficerTasksList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOfficerTasksList.setAdapter(adapter);
    }

    private void applyFilter(String filter) {
        this.currentFilter = filter;
        Officer currentOfficer = EmergencyDataRepository.getInstance().getCurrentOfficerProfile();
        String officerId = currentOfficer != null ? currentOfficer.getId() : "OFF-4092";
        List<EmergencyReport> myTasks = EmergencyDataRepository.getInstance().getTasksForOfficer(officerId);
        List<EmergencyReport> filtered = new ArrayList<>();

        for (EmergencyReport r : myTasks) {
            if ("All".equalsIgnoreCase(filter) || r.getStatus().equalsIgnoreCase(filter)) {
                filtered.add(r);
            }
        }

        if (adapter != null) {
            adapter.updateData(filtered);
        }

        if (filtered.isEmpty()) {
            layoutOfficerTasksEmpty.setVisibility(View.VISIBLE);
            rvOfficerTasksList.setVisibility(View.GONE);
        } else {
            layoutOfficerTasksEmpty.setVisibility(View.GONE);
            rvOfficerTasksList.setVisibility(View.VISIBLE);
        }

        tvOfficerTasksSubtitle.setText("Showing " + filtered.size() + " active incident tasks");
    }
}
