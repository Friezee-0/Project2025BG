package com.example.audioguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteViewHolder> {
    private final List<Route> routes;
    private final Context context;
    private final OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    public RoutesAdapter(Context context, List<Route> routes, OnRouteClickListener listener) {
        this.context = context;
        this.routes = routes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.titleTextView.setText(context.getString(route.getNameResId()));
        holder.descriptionTextView.setText(context.getString(route.getDescriptionResId()));
        
        holder.itemView.setOnClickListener(v -> listener.onRouteClick(route));
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView descriptionTextView;

        RouteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.route_title);
            descriptionTextView = itemView.findViewById(R.id.route_description);
        }
    }
} 