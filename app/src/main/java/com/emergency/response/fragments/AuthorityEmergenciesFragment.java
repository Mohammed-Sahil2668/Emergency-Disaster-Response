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
import com.emergency.response.activities.AuthorityEmergencyDetailsActivity;
import com.emergency.response.adapters.EmergencyReportAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.google.android.material.chip.Chip;

import java.util.List;

public class AuthorityEmergenciesFragment extends Fragment {

    private TextView tvAuthSummarySubtitle;
    private Chip chipAuthAll, chipAuthNew, chipAuthVerified, chipAuthAssigned, chipAuthResponding, chipAuthResolved, chipAuthRejected;
    private RecyclerView rvAuthReports;
    private LinearLayout layoutAuthEmpty;

    private EmergencyReportAdapter adapter;
    private String currentFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authority_emergencies, container, false);
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
        tvAuthSummarySubtitle = view.findViewById(R.id.tvAuthSummarySubtitle);
        chipAuthAll = view.findViewById(R.id.chipAuthAll);
        chipAuthNew = view.findViewById(R.id.chipAuthNew);
        chipAuthVerified = view.findViewById(R.id.chipAuthVerified);
        chipAuthAssigned = view.findViewById(R.id.chipAuthAssigned);
        chipAuthResponding = view.findViewById(R.id.chipAuthResponding);
        chipAuthResolved = view.findViewById(R.id.chipAuthResolved);
        chipAuthRejected = view.findViewById(R.id.chipAuthRejected);
        rvAuthReports = view.findViewById(R.id.rvAuthReports);
        layoutAuthEmpty = view.findViewById(R.id.layoutAuthEmpty);
    }

    private void setupListeners() {
        chipAuthAll.setOnClickListener(v -> applyFilter("All"));
        chipAuthNew.setOnClickListener(v -> applyFilter("Reported"));
        chipAuthVerified.setOnClickListener(v -> applyFilter("Verified"));
        chipAuthAssigned.setOnClickListener(v -> applyFilter("Assigned"));
        chipAuthResponding.setOnClickListener(v -> applyFilter("Responding"));
        chipAuthResolved.setOnClickListener(v -> applyFilter("Resolved"));
        chipAuthRejected.setOnClickListener(v -> applyFilter("Rejected"));
    }

    private void setupRecyclerView() {
        List<EmergencyReport> reports = EmergencyDataRepository.getInstance().getAllReports();
        adapter = new EmergencyReportAdapter(requireContext(), reports, report -> {
            Intent intent = new Intent(requireContext(), AuthorityEmergencyDetailsActivity.class);
            intent.putExtra("report_id", report.getId());
            startActivity(intent);
        });
        rvAuthReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAuthReports.setAdapter(adapter);
    }

    private void applyFilter(String filter) {
        this.currentFilter = filter;
        List<EmergencyReport> filtered = EmergencyDataRepository.getInstance().getReportsByStatus(filter);

        if (adapter != null) {
            adapter.updateData(filtered);
        }

        if (filtered.isEmpty()) {
            layoutAuthEmpty.setVisibility(View.VISIBLE);
            rvAuthReports.setVisibility(View.GONE);
        } else {
            layoutAuthEmpty.setVisibility(View.GONE);
            rvAuthReports.setVisibility(View.VISIBLE);
        }

        int total = EmergencyDataRepository.getInstance().getAllReports().size();
        tvAuthSummarySubtitle.setText("City-Wide Triage: Showing " + filtered.size() + " of " + total + " incidents");
    }
}
