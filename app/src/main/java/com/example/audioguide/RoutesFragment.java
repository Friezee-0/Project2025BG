package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoutesFragment extends Fragment implements RoutesAdapter.OnRouteClickListener {
    private RecyclerView recyclerView;
    private RoutesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        recyclerView = view.findViewById(R.id.routes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<Route> routes = RouteData.getRoutes();
        adapter = new RoutesAdapter(routes, this);
        recyclerView.setAdapter(adapter);
        
        return view;
    }

    @Override
    public void onRouteClick(Route route) {
        RouteDetailDialog dialog = RouteDetailDialog.newInstance(
            route.getName(),
            route.getShortDescription(),
            route.getFullDescription(),
            route.getDuration(),
            route.getDistance(),
            route.getTips()
        );
        dialog.show(getParentFragmentManager(), "route_detail");
    }
} 