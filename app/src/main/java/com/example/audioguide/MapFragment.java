package com.example.audioguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

public class MapFragment extends Fragment implements OnRouteMapClickListener {
    private static final String TAG = "MapFragment";
    private MapView mapView;
    private IMapController mapController;
    private Marker currentLocationMarker;
    private Route currentRoute;
    private List<Marker> routeMarkers;
    private List<Landmark> landmarks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        
        Configuration.getInstance().load(requireContext(), 
            requireContext().getSharedPreferences("osmdroid", 0));
        
        mapView = view.findViewById(R.id.map_view);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        mapController = mapView.getController();
        mapController.setZoom(15.0);
        
        // Установка начальной позиции на Москву
        GeoPoint startPoint = new GeoPoint(55.7539, 37.6208);
        mapController.setCenter(startPoint);
        
        routeMarkers = new ArrayList<>();
        landmarks = LandmarkData.getLandmarks();
        addLandmarksToMap();
        
        return view;
    }

    private void addLandmarksToMap() {
        try {
            // Получаем иконку маркера из ресурсов
            Drawable markerIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_pin);
            
            for (Landmark landmark : landmarks) {
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
                marker.setTitle(getString(landmark.getNameResId()));
                marker.setSnippet(getString(landmark.getShortDescriptionResId()));
                
                // Устанавливаем иконку маркера
                if (markerIcon != null) {
                    marker.setIcon(markerIcon);
                }
                
                // Устанавливаем точку привязки маркера (нижняя точка иглы)
                marker.setAnchor(0.5f, 1.0f);
                mapView.getOverlays().add(marker);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding landmarks to map", e);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearRouteMarkers();
    }

    private void clearRouteMarkers() {
        for (Marker marker : routeMarkers) {
            mapView.getOverlays().remove(marker);
        }
        routeMarkers.clear();
    }

    @Override
    public void onRouteMapClick() {
        if (currentRoute != null) {
            showRouteOnMap(currentRoute);
        }
    }

    private void showRouteOnMap(Route route) {
        clearRouteMarkers();
        currentRoute = route;
        
        // TODO: Implement route display on map
        Toast.makeText(requireContext(), R.string.map_navigation_coming_soon, Toast.LENGTH_SHORT).show();
    }
} 