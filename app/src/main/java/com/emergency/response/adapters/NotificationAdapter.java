package com.emergency.response.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.response.R;
import com.emergency.response.models.AppNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(AppNotification notification);
    }

    private final Context context;
    private List<AppNotification> notificationList;
    private final OnNotificationClickListener listener;

    public NotificationAdapter(Context context, List<AppNotification> notificationList, OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList != null ? notificationList : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<AppNotification> newList) {
        this.notificationList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        AppNotification notif = notificationList.get(position);

        holder.tvNotifTitle.setText(notif.getTitle());
        holder.tvNotifMessage.setText(notif.getMessage());
        holder.tvNotifTimestamp.setText(notif.getTimestamp());

        holder.viewUnreadDot.setVisibility(notif.isRead() ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onNotificationClick(notif);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotifIcon;
        TextView tvNotifTitle, tvNotifMessage, tvNotifTimestamp;
        View viewUnreadDot;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotifIcon = itemView.findViewById(R.id.ivNotifIcon);
            tvNotifTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvNotifMessage = itemView.findViewById(R.id.tvNotifMessage);
            tvNotifTimestamp = itemView.findViewById(R.id.tvNotifTimestamp);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
        }
    }
}
