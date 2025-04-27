package com.example.audioguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoutesFragment extends Fragment implements 
    RoutesAdapter.OnRouteClickListener,
    OnRouteMapClickListener {
    
    private RecyclerView recyclerView;
    private RoutesAdapter adapter;
    private static final String TAG = "RoutesFragment";
    private Route currentRoute;

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
        currentRoute = route;
        showRouteDetails(route);
    }

    private void showRouteDetails(Route route) {
        try {
            RouteDetailsDialog dialog = RouteDetailsDialog.newInstance(
                route.getNameResId(),
                route.getDescriptionResId(),
                route.getDurationResId(),
                route.getDistanceResId(),
                route.getLandmarkIds(),
                route.getTips()
            );
            dialog.show(getChildFragmentManager(), "RouteDetailsDialog");
        } catch (Exception e) {
            Log.e("RoutesFragment", "Error showing route details", e);
            Toast.makeText(requireContext(), R.string.error_loading_route, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRouteMapClick() {
        if (currentRoute != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showRouteOnMap(currentRoute);
        }
    }
} 