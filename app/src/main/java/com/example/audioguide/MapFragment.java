package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private MapView mapView;
    private List<Landmark> landmarks;
    private SettingsManager settingsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(getContext()));
        landmarks = new ArrayList<>();
        // Добавляем достопримечательности
        landmarks.add(new Landmark("Красная площадь", "Главная площадь Москвы", 55.7539, 37.6208));
        landmarks.add(new Landmark("Собор Василия Блаженного", "Православный храм на Красной площади", 55.7525, 37.6231));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        // Центрируем карту на Москве
        GeoPoint moscow = new GeoPoint(55.7558, 37.6173);
        mapView.getController().setCenter(moscow);
        mapView.getController().setZoom(15.0);

        // Добавляем маркеры для достопримечательностей
        for (Landmark landmark : landmarks) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
            marker.setTitle(landmark.getName());
            marker.setSnippet(landmark.getDescription());
            mapView.getOverlays().add(marker);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
} 