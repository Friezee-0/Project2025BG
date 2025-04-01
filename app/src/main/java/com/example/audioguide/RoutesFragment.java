package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RoutesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RoutesAdapter adapter;
    private List<Route> routes;
    private SettingsManager settingsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routes = new ArrayList<>();
        
        // Создаем маршрут по историческому центру
        Route historicalCenter = new Route("Исторический центр Москвы", 
            "Маршрут по историческому центру Москвы включает посещение главных достопримечательностей: Красной площади и Собора Василия Блаженного.");
        
        // Добавляем достопримечательности в маршрут
        historicalCenter.addLandmark(new Landmark("1", "Красная площадь", 
            "Главная площадь Москвы", 55.7539, 37.6208, ""));
        historicalCenter.addLandmark(new Landmark("2", "Собор Василия Блаженного", 
            "Православный храм на Красной площади", 55.7525, 37.6231, ""));
        
        routes.add(historicalCenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesAdapter(routes, settingsManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
} 