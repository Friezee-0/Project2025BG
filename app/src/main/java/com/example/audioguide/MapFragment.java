package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.List;

public class MapFragment extends Fragment {
    private MapView mapView;
    private List<Landmark> landmarks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        
        Configuration.getInstance().load(requireContext(), 
            requireContext().getSharedPreferences("osmdroid", 0));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);
        GeoPoint startPoint = new GeoPoint(55.7558, 37.6173);
        mapController.setCenter(startPoint);
        
        landmarks = LandmarkData.getLandmarks();
        addLandmarksToMap();
        
        return view;
    }

    private void addLandmarksToMap() {
        for (Landmark landmark : landmarks) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
            marker.setTitle(getString(landmark.getNameResId()));
            marker.setSnippet(getString(landmark.getShortDescriptionResId()));
            mapView.getOverlays().add(marker);
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
} 