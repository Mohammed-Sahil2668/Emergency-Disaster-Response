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
import com.emergency.response.models.EmergencyCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(EmergencyCategory category);
    }

    private final Context context;
    private final List<EmergencyCategory> categoryList;
    private final OnCategoryClickListener listener;

    public CategoryAdapter(Context context, List<EmergencyCategory> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emergency_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        EmergencyCategory category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());
        holder.ivCategoryIcon.setImageResource(category.getIconResId());

        int accentColor = ContextCompat.getColor(context, category.getAccentColorResId());
        int bgColor = ContextCompat.getColor(context, category.getBgColorResId());

        holder.flIconContainer.setBackgroundTintList(ColorStateList.valueOf(bgColor));
        holder.ivCategoryIcon.setImageTintList(ColorStateList.valueOf(accentColor));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flIconContainer;
        ImageView ivCategoryIcon;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            flIconContainer = itemView.findViewById(R.id.flIconContainer);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
