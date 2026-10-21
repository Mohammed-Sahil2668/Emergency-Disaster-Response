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
import com.emergency.response.activities.EmergencyReportDetailsActivity;
import com.emergency.response.adapters.EmergencyReportAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MyReportsFragment extends Fragment {

    private TextView tvReportsSummary;
    private ChipGroup chipGroupFilter;
    private Chip chipAll, chipResponding, chipAssigned, chipVerified, chipReported, chipResolved;
    private RecyclerView rvMyReports;
    private LinearLayout layoutEmptyReports;

    private EmergencyReportAdapter adapter;
    private String currentFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reports, container, false);
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
        tvReportsSummary = view.findViewById(R.id.tvReportsSummary);
        chipGroupFilter = view.findViewById(R.id.chipGroupFilter);
        chipAll = view.findViewById(R.id.chipAll);
        chipResponding = view.findViewById(R.id.chipResponding);
        chipAssigned = view.findViewById(R.id.chipAssigned);
        chipVerified = view.findViewById(R.id.chipVerified);
        chipReported = view.findViewById(R.id.chipReported);
        chipResolved = view.findViewById(R.id.chipResolved);
        rvMyReports = view.findViewById(R.id.rvMyReports);
        layoutEmptyReports = view.findViewById(R.id.layoutEmptyReports);
    }

    private void setupListeners() {
        chipAll.setOnClickListener(v -> applyFilter("All"));
        chipResponding.setOnClickListener(v -> applyFilter("Responding"));
        chipAssigned.setOnClickListener(v -> applyFilter("Assigned"));
        chipVerified.setOnClickListener(v -> applyFilter("Verified"));
        chipReported.setOnClickListener(v -> applyFilter("Reported"));
        chipResolved.setOnClickListener(v -> applyFilter("Resolved"));
    }

    private void setupRecyclerView() {
        List<EmergencyReport> reports = EmergencyDataRepository.getInstance().getAllReports();
        adapter = new EmergencyReportAdapter(requireContext(), reports, report -> {
            Intent intent = new Intent(requireContext(), EmergencyReportDetailsActivity.class);
            intent.putExtra("report_id", report.getId());
            startActivity(intent);
        });
        rvMyReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMyReports.setAdapter(adapter);
    }

    private void applyFilter(String filter) {
        this.currentFilter = filter;
        List<EmergencyReport> filteredReports = EmergencyDataRepository.getInstance().getReportsByStatus(filter);

        if (adapter != null) {
            adapter.updateData(filteredReports);
        }

        if (filteredReports.isEmpty()) {
            layoutEmptyReports.setVisibility(View.VISIBLE);
            rvMyReports.setVisibility(View.GONE);
        } else {
            layoutEmptyReports.setVisibility(View.GONE);
            rvMyReports.setVisibility(View.VISIBLE);
        }

        int total = EmergencyDataRepository.getInstance().getAllReports().size();
        chipAll.setText("All (" + total + ")");
        tvReportsSummary.setText("Showing " + filteredReports.size() + " incidents in network");
    }
}
