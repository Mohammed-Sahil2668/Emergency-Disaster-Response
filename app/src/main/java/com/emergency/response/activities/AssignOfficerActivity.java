package com.emergency.response.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.adapters.OfficerAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.models.Officer;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AssignOfficerActivity extends AppCompatActivity {

    private ImageView btnBackAssignOfficer;
    private TextView tvTargetReportInfo, tvAssignOfficerCountPrompt;
    private TextInputEditText etAssignSearch;
    private Chip chipAssignDeptAll, chipAssignDeptPolice, chipAssignDeptFire, chipAssignDeptMedical, chipAssignDeptDisaster;
    private RecyclerView rvAssignOfficersList;

    private OfficerAdapter adapter;
    private String reportId;
    private EmergencyReport targetReport;
    private String selectedDepartment = "All";
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_officer);

        reportId = getIntent().getStringExtra("report_id");
        if (reportId == null) reportId = "EMG-2026-8942";
        targetReport = EmergencyDataRepository.getInstance().getReportById(reportId);

        initViews();
        setupListeners();
        setupRecyclerView();
        applyFilters();
    }

    private void initViews() {
        btnBackAssignOfficer = findViewById(R.id.btnBackAssignOfficer);
        tvTargetReportInfo = findViewById(R.id.tvTargetReportInfo);
        tvAssignOfficerCountPrompt = findViewById(R.id.tvAssignOfficerCountPrompt);
        etAssignSearch = findViewById(R.id.etAssignSearch);
        chipAssignDeptAll = findViewById(R.id.chipAssignDeptAll);
        chipAssignDeptPolice = findViewById(R.id.chipAssignDeptPolice);
        chipAssignDeptFire = findViewById(R.id.chipAssignDeptFire);
        chipAssignDeptMedical = findViewById(R.id.chipAssignDeptMedical);
        chipAssignDeptDisaster = findViewById(R.id.chipAssignDeptDisaster);
        rvAssignOfficersList = findViewById(R.id.rvAssignOfficersList);

        if (targetReport != null) {
            tvTargetReportInfo.setText("Dispatching to: " + targetReport.getTitle() + " (" + targetReport.getId() + ")");
        }
    }

    private void setupListeners() {
        btnBackAssignOfficer.setOnClickListener(v -> finish());

        chipAssignDeptAll.setOnClickListener(v -> { selectedDepartment = "All"; applyFilters(); });
        chipAssignDeptPolice.setOnClickListener(v -> { selectedDepartment = "Police"; applyFilters(); });
        chipAssignDeptFire.setOnClickListener(v -> { selectedDepartment = "Fire"; applyFilters(); });
        chipAssignDeptMedical.setOnClickListener(v -> { selectedDepartment = "Medical"; applyFilters(); });
        chipAssignDeptDisaster.setOnClickListener(v -> { selectedDepartment = "Disaster"; applyFilters(); });

        etAssignSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s != null ? s.toString().trim().toLowerCase() : "";
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        List<Officer> list = EmergencyDataRepository.getInstance().getAllOfficers();
        adapter = new OfficerAdapter(this, list, new OfficerAdapter.OnOfficerActionListener() {
            @Override
            public void onViewProfile(Officer officer) {
                Intent intent = new Intent(AssignOfficerActivity.this, OfficerDetailsActivity.class);
                intent.putExtra("officer_id", officer.getId());
                startActivity(intent);
            }

            @Override
            public void onAssign(Officer officer) {
                showConfirmAssignDialog(officer);
            }
        });
        rvAssignOfficersList.setLayoutManager(new LinearLayoutManager(this));
        rvAssignOfficersList.setAdapter(adapter);
    }

    private void applyFilters() {
        List<Officer> all = EmergencyDataRepository.getInstance().getAllOfficers();
        List<Officer> filtered = new ArrayList<>();

        for (Officer o : all) {
            boolean matchesDept = "All".equalsIgnoreCase(selectedDepartment) ||
                    (o.getDepartment() != null && o.getDepartment().toLowerCase().contains(selectedDepartment.toLowerCase()));

            boolean matchesSearch = searchQuery.isEmpty() ||
                    o.getName().toLowerCase().contains(searchQuery) ||
                    o.getEmployeeId().toLowerCase().contains(searchQuery) ||
                    o.getDesignation().toLowerCase().contains(searchQuery);

            if (matchesDept && matchesSearch) {
                filtered.add(o);
            }
        }

        if (adapter != null) {
            adapter.updateData(filtered);
        }

        tvAssignOfficerCountPrompt.setText("Showing " + filtered.size() + " available/eligible officers");
    }

    private void showConfirmAssignDialog(Officer officer) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Officer Assignment")
                .setMessage("Are you sure you want to assign " + officer.getName() + " (" + officer.getDesignation() + " - " + officer.getDepartment() + ") to incident " + reportId + "?\n\nThis will update officer status to 'On Duty' and trigger a high-priority dispatch notification.")
                .setPositiveButton("Confirm Dispatch", (dialog, which) -> {
                    boolean success = EmergencyDataRepository.getInstance().assignOfficerToReport(reportId, officer.getId());
                    if (success) {
                        Toast.makeText(this, officer.getName() + " successfully assigned to " + reportId + "!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to assign officer.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
