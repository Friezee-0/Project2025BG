package com.example.audioguide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {
    private final List<Route> routes;
    private final OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    public RoutesAdapter(List<Route> routes, OnRouteClickListener listener) {
        this.routes = routes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.titleTextView.setText(route.getName());
        holder.descriptionTextView.setText(route.getShortDescription());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRouteClick(route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.route_title);
            descriptionTextView = view.findViewById(R.id.route_description);
        }
    }
} 