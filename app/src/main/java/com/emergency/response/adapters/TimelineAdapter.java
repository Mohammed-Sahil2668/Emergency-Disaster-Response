package com.emergency.response.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.models.TimelineStep;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    private final Context context;
    private final List<TimelineStep> stepList;

    public TimelineAdapter(Context context, List<TimelineStep> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_timeline_step, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        TimelineStep step = stepList.get(position);

        holder.tvTimelineTitle.setText(step.getTitle());
        holder.tvTimelineDesc.setText(step.getDescription());
        holder.tvTimelineTime.setText(step.getTime());

        // Indicator styling
        if (step.isCompleted()) {
            holder.flTimelineIndicator.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.status_resolved)));
            holder.ivTimelineCheck.setImageResource(R.drawable.ic_check_circle);
            holder.ivTimelineCheck.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.text_white)));
        } else if (step.isCurrent()) {
            holder.flTimelineIndicator.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.emergency_red)));
            holder.ivTimelineCheck.setImageResource(R.drawable.ic_refresh);
            holder.ivTimelineCheck.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.text_white)));
        } else {
            holder.flTimelineIndicator.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.divider_color)));
            holder.ivTimelineCheck.setImageDrawable(null);
        }

        // Hide vertical connecting line on the last step
        if (position == stepList.size() - 1) {
            holder.vTimelineLine.setVisibility(View.GONE);
        } else {
            holder.vTimelineLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stepList == null ? 0 : stepList.size();
    }

    static class TimelineViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flTimelineIndicator;
        ImageView ivTimelineCheck;
        View vTimelineLine;
        TextView tvTimelineTitle;
        TextView tvTimelineDesc;
        TextView tvTimelineTime;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            flTimelineIndicator = itemView.findViewById(R.id.flTimelineIndicator);
            ivTimelineCheck = itemView.findViewById(R.id.ivTimelineCheck);
            vTimelineLine = itemView.findViewById(R.id.vTimelineLine);
            tvTimelineTitle = itemView.findViewById(R.id.tvTimelineTitle);
            tvTimelineDesc = itemView.findViewById(R.id.tvTimelineDesc);
            tvTimelineTime = itemView.findViewById(R.id.tvTimelineTime);
        }
    }
}
