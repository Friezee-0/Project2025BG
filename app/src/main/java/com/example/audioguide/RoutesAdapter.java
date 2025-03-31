package com.example.audioguide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {
    private List<Route> routes;
    private SettingsManager settingsManager;

    public RoutesAdapter(List<Route> routes, SettingsManager settingsManager) {
        this.routes = routes;
        this.settingsManager = settingsManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.titleView.setText(route.getName());
        holder.descriptionView.setText(route.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (settingsManager != null) {
                settingsManager.speak(route.getDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView descriptionView;

        ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(android.R.id.text1);
            descriptionView = view.findViewById(android.R.id.text2);
        }
    }
} 