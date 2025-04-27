package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoutesFragment extends Fragment implements RoutesAdapter.OnRouteClickListener {
    private RecyclerView recyclerView;
    private RoutesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        
        recyclerView = view.findViewById(R.id.routes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<Route> routes = RouteData.getRoutes();
        adapter = new RoutesAdapter(requireContext(), routes, this);
        recyclerView.setAdapter(adapter);
        
        return view;
    }

    @Override
    public void onRouteClick(Route route) {
        RouteDetailsDialog dialog = RouteDetailsDialog.newInstance(
            route.getNameResId(),
            route.getFullDescriptionResId(),
            route.getLandmarks(),
            route.getStartLatitude(),
            route.getStartLongitude(),
            route.getEndLatitude(),
            route.getEndLongitude()
        );
        dialog.show(getChildFragmentManager(), "route_details");
    }
} 