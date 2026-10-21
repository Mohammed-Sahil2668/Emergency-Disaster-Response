package com.emergency.response.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.activities.OfficerDetailsActivity;
import com.emergency.response.adapters.OfficerAdapter;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.Officer;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class OfficerManagementFragment extends Fragment {

    private TextInputEditText etSearchOfficer;
    private Chip chipDeptAll, chipDeptPolice, chipDeptFire, chipDeptMedical, chipDeptDisaster;
    private Chip chipAvailAll, chipAvailAvailable;
    private RecyclerView rvOfficersList;
    private LinearLayout layoutOfficersEmpty;
    private TextView tvOfficersSubtitle;

    private OfficerAdapter adapter;
    private String selectedDepartment = "All";
    private boolean availableOnly = false;
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_officer_management, container, false);
        initViews(view);
        setupListeners();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyFilters();
    }

    private void initViews(View view) {
        etSearchOfficer = view.findViewById(R.id.etSearchOfficer);
        chipDeptAll = view.findViewById(R.id.chipDeptAll);
        chipDeptPolice = view.findViewById(R.id.chipDeptPolice);
        chipDeptFire = view.findViewById(R.id.chipDeptFire);
        chipDeptMedical = view.findViewById(R.id.chipDeptMedical);
        chipDeptDisaster = view.findViewById(R.id.chipDeptDisaster);
        chipAvailAll = view.findViewById(R.id.chipAvailAll);
        chipAvailAvailable = view.findViewById(R.id.chipAvailAvailable);
        rvOfficersList = view.findViewById(R.id.rvOfficersList);
        layoutOfficersEmpty = view.findViewById(R.id.layoutOfficersEmpty);
        tvOfficersSubtitle = view.findViewById(R.id.tvOfficersSubtitle);
    }

    private void setupListeners() {
        chipDeptAll.setOnClickListener(v -> { selectedDepartment = "All"; applyFilters(); });
        chipDeptPolice.setOnClickListener(v -> { selectedDepartment = "Police"; applyFilters(); });
        chipDeptFire.setOnClickListener(v -> { selectedDepartment = "Fire"; applyFilters(); });
        chipDeptMedical.setOnClickListener(v -> { selectedDepartment = "Medical"; applyFilters(); });
        chipDeptDisaster.setOnClickListener(v -> { selectedDepartment = "Disaster"; applyFilters(); });

        chipAvailAll.setOnClickListener(v -> { availableOnly = false; applyFilters(); });
        chipAvailAvailable.setOnClickListener(v -> { availableOnly = true; applyFilters(); });

        etSearchOfficer.addTextChangedListener(new TextWatcher() {
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
        adapter = new OfficerAdapter(requireContext(), list, new OfficerAdapter.OnOfficerActionListener() {
            @Override
            public void onViewProfile(Officer officer) {
                Intent intent = new Intent(requireContext(), OfficerDetailsActivity.class);
                intent.putExtra("officer_id", officer.getId());
                startActivity(intent);
            }

            @Override
            public void onAssign(Officer officer) {
                Toast.makeText(requireContext(), "To assign " + officer.getName() + ", select an emergency from the Registry or Notification.", Toast.LENGTH_LONG).show();
            }
        });
        rvOfficersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOfficersList.setAdapter(adapter);
    }

    private void applyFilters() {
        List<Officer> all = EmergencyDataRepository.getInstance().getAllOfficers();
        List<Officer> filtered = new ArrayList<>();

        for (Officer o : all) {
            boolean matchesDept = "All".equalsIgnoreCase(selectedDepartment) ||
                    (o.getDepartment() != null && o.getDepartment().toLowerCase().contains(selectedDepartment.toLowerCase()));

            boolean matchesAvail = !availableOnly || "Available".equalsIgnoreCase(o.getAvailability());

            boolean matchesSearch = searchQuery.isEmpty() ||
                    o.getName().toLowerCase().contains(searchQuery) ||
                    o.getEmployeeId().toLowerCase().contains(searchQuery) ||
                    o.getDesignation().toLowerCase().contains(searchQuery);

            if (matchesDept && matchesAvail && matchesSearch) {
                filtered.add(o);
            }
        }

        if (adapter != null) {
            adapter.updateData(filtered);
        }

        if (filtered.isEmpty()) {
            layoutOfficersEmpty.setVisibility(View.VISIBLE);
            rvOfficersList.setVisibility(View.GONE);
        } else {
            layoutOfficersEmpty.setVisibility(View.GONE);
            rvOfficersList.setVisibility(View.VISIBLE);
        }

        tvOfficersSubtitle.setText("Showing " + filtered.size() + " of " + all.size() + " registered field officers");
    }
}
