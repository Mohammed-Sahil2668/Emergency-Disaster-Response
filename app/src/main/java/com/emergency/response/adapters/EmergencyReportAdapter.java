package com.emergency.response.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.models.EmergencyReport;

import java.util.List;

public class EmergencyReportAdapter extends RecyclerView.Adapter<EmergencyReportAdapter.ReportViewHolder> {

    public interface OnReportClickListener {
        void onReportClick(EmergencyReport report);
    }

    private final Context context;
    private List<EmergencyReport> reportList;
    private final OnReportClickListener listener;

    public EmergencyReportAdapter(Context context, List<EmergencyReport> reportList, OnReportClickListener listener) {
        this.context = context;
        this.reportList = reportList;
        this.listener = listener;
    }

    public void updateData(List<EmergencyReport> newReports) {
        this.reportList = newReports;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emergency_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        EmergencyReport report = reportList.get(position);

        holder.tvReportCategory.setText(report.getCategory());
        holder.ivReportCategoryIcon.setImageResource(report.getCategoryIconRes());
        holder.tvReportTitle.setText(report.getTitle());
        holder.tvReportDesc.setText(report.getDescription());
        holder.tvReportLocation.setText(report.getLocation());
        holder.tvReportDate.setText(report.getReportedDate());

        // Status Styling
        String status = report.getStatus();
        holder.tvReportStatus.setText(status);

        int textColor;
        int bgColor;

        switch (status.toLowerCase()) {
            case "reported":
                textColor = ContextCompat.getColor(context, R.color.status_reported);
                bgColor = ContextCompat.getColor(context, R.color.status_reported_bg);
                break;
            case "verified":
                textColor = ContextCompat.getColor(context, R.color.status_verified);
                bgColor = ContextCompat.getColor(context, R.color.status_verified_bg);
                break;
            case "assigned":
                textColor = ContextCompat.getColor(context, R.color.status_assigned);
                bgColor = ContextCompat.getColor(context, R.color.status_assigned_bg);
                break;
            case "responding":
                textColor = ContextCompat.getColor(context, R.color.status_responding);
                bgColor = ContextCompat.getColor(context, R.color.status_responding_bg);
                break;
            case "resolved":
            default:
                textColor = ContextCompat.getColor(context, R.color.status_resolved);
                bgColor = ContextCompat.getColor(context, R.color.status_resolved_bg);
                break;
        }

        holder.tvReportStatus.setTextColor(textColor);
        holder.tvReportStatus.setBackgroundTintList(ColorStateList.valueOf(bgColor));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReportClick(report);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList == null ? 0 : reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReportCategoryIcon;
        TextView tvReportCategory;
        TextView tvReportStatus;
        TextView tvReportTitle;
        TextView tvReportDesc;
        TextView tvReportLocation;
        TextView tvReportDate;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReportCategoryIcon = itemView.findViewById(R.id.ivReportCategoryIcon);
            tvReportCategory = itemView.findViewById(R.id.tvReportCategory);
            tvReportStatus = itemView.findViewById(R.id.tvReportStatus);
            tvReportTitle = itemView.findViewById(R.id.tvReportTitle);
            tvReportDesc = itemView.findViewById(R.id.tvReportDesc);
            tvReportLocation = itemView.findViewById(R.id.tvReportLocation);
            tvReportDate = itemView.findViewById(R.id.tvReportDate);
        }
    }
}
