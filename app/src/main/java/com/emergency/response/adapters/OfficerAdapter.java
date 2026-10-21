package com.emergency.response.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.models.Officer;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OfficerAdapter extends RecyclerView.Adapter<OfficerAdapter.OfficerViewHolder> {

    public interface OnOfficerActionListener {
        void onViewProfile(Officer officer);
        void onAssign(Officer officer);
    }

    private final Context context;
    private List<Officer> officerList;
    private final OnOfficerActionListener listener;
    private boolean showAssignButton = true;

    public OfficerAdapter(Context context, List<Officer> officerList, OnOfficerActionListener listener) {
        this.context = context;
        this.officerList = officerList != null ? officerList : new ArrayList<>();
        this.listener = listener;
    }

    public void setShowAssignButton(boolean show) {
        this.showAssignButton = show;
        notifyDataSetChanged();
    }

    public void updateData(List<Officer> newList) {
        this.officerList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OfficerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_officer_card, parent, false);
        return new OfficerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficerViewHolder holder, int position) {
        Officer officer = officerList.get(position);

        holder.tvOfficerName.setText(officer.getName());
        holder.tvOfficerDesignation.setText(officer.getDesignation() + " • " + officer.getAge() + " yrs");
        holder.tvOfficerDepartment.setText(officer.getDepartment());
        holder.tvOfficerIdTag.setText("ID: " + officer.getEmployeeId());
        holder.tvOfficerExp.setText("Exp: " + officer.getExperience());

        // Gender Icon
        holder.ivGenderIcon.setImageResource(officer.getGenderIconRes());

        // Availability Badge
        holder.tvOfficerAvailability.setText(officer.getAvailability());
        if ("Available".equalsIgnoreCase(officer.getAvailability())) {
            holder.tvOfficerAvailability.setTextColor(ContextCompat.getColor(context, R.color.officer_available));
            holder.tvOfficerAvailability.setBackgroundColor(ContextCompat.getColor(context, R.color.officer_available_bg));
        } else if ("On Duty".equalsIgnoreCase(officer.getAvailability())) {
            holder.tvOfficerAvailability.setTextColor(ContextCompat.getColor(context, R.color.officer_on_duty));
            holder.tvOfficerAvailability.setBackgroundColor(ContextCompat.getColor(context, R.color.officer_on_duty_bg));
        } else {
            holder.tvOfficerAvailability.setTextColor(ContextCompat.getColor(context, R.color.officer_off_duty));
            holder.tvOfficerAvailability.setBackgroundColor(ContextCompat.getColor(context, R.color.officer_off_duty_bg));
        }

        // Action Buttons
        holder.btnAssignOfficerAction.setVisibility(showAssignButton ? View.VISIBLE : View.GONE);
        holder.btnViewOfficerProfile.setOnClickListener(v -> {
            if (listener != null) listener.onViewProfile(officer);
        });
        holder.btnAssignOfficerAction.setOnClickListener(v -> {
            if (listener != null) listener.onAssign(officer);
        });
    }

    @Override
    public int getItemCount() {
        return officerList.size();
    }

    static class OfficerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOfficerAvatar, ivGenderIcon;
        TextView tvOfficerName, tvOfficerDesignation, tvOfficerAvailability, tvOfficerDepartment, tvOfficerIdTag, tvOfficerExp;
        MaterialButton btnViewOfficerProfile, btnAssignOfficerAction;

        public OfficerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOfficerAvatar = itemView.findViewById(R.id.ivOfficerAvatar);
            ivGenderIcon = itemView.findViewById(R.id.ivGenderIcon);
            tvOfficerName = itemView.findViewById(R.id.tvOfficerName);
            tvOfficerDesignation = itemView.findViewById(R.id.tvOfficerDesignation);
            tvOfficerAvailability = itemView.findViewById(R.id.tvOfficerAvailability);
            tvOfficerDepartment = itemView.findViewById(R.id.tvOfficerDepartment);
            tvOfficerIdTag = itemView.findViewById(R.id.tvOfficerIdTag);
            tvOfficerExp = itemView.findViewById(R.id.tvOfficerExp);
            btnViewOfficerProfile = itemView.findViewById(R.id.btnViewOfficerProfile);
            btnAssignOfficerAction = itemView.findViewById(R.id.btnAssignOfficerAction);
        }
    }
}
