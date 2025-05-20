package com.example.audioguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;
import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.example.audioguide.service.TTSService;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnRouteMapClickListener, MapEventsReceiver {
    private static final String TAG = "MapFragment";
    private static final float PROXIMITY_THRESHOLD = 50.0f; // 50 meters
    private static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 seconds
    
    private MapView mapView;
    private IMapController mapController;
    private Marker currentLocationMarker;
    private Route currentRoute;
    private List<Marker> routeMarkers;
    private List<Landmark> landmarks;
    private Polyline currentRoutePolyline;
    private MyLocationNewOverlay myLocationOverlay;
    private TTSService ttsService;
    private Handler locationUpdateHandler;
    private Map<String, String> landmarkDescriptions;
    private Map<String, GeoPoint> landmarkLocations;
    private String currentSpeakingLandmark = null;

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
        
        // Инициализация оверлея для отображения текущего местоположения
        myLocationOverlay = new MyLocationNewOverlay(mapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
        mapView.getOverlays().add(myLocationOverlay);
        
        // Настройка кнопки центрирования карты
        view.findViewById(R.id.fab_my_location).setOnClickListener(v -> {
            if (myLocationOverlay != null && myLocationOverlay.getMyLocation() != null) {
                mapController.animateTo(myLocationOverlay.getMyLocation());
                myLocationOverlay.enableFollowLocation();
            } else {
                Toast.makeText(requireContext(), R.string.location_not_available, Toast.LENGTH_SHORT).show();
            }
        });
        
        routeMarkers = new ArrayList<>();
        landmarks = LandmarkData.getLandmarks();
        addLandmarksToMap();
        
        // Добавляем обработчик кликов по карте
        mapView.getOverlays().add(new org.osmdroid.views.overlay.MapEventsOverlay(this));
        
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsService = TTSService.getInstance(requireContext());
        locationUpdateHandler = new Handler(Looper.getMainLooper());
        landmarkDescriptions = new HashMap<>();
        landmarkLocations = new HashMap<>();
        landmarks = LandmarkData.getLandmarks();
        if (landmarks != null) {
            initializeLandmarkDescriptions();
            initializeLandmarkLocations();
        }
    }

    private void initializeLandmarkDescriptions() {
        landmarkDescriptions.put("red_square", getString(R.string.red_square_full_description));
        landmarkDescriptions.put("saint_basil", getString(R.string.saint_basil_full_description));
        landmarkDescriptions.put("kremlin", getString(R.string.kremlin_full_description));
        landmarkDescriptions.put("tretyakov", getString(R.string.tretyakov_full_description));
        landmarkDescriptions.put("bolshoi", getString(R.string.bolshoi_full_description));
        landmarkDescriptions.put("gum", getString(R.string.gum_full_description));
        landmarkDescriptions.put("lenin_mausoleum", getString(R.string.lenin_mausoleum_full_description));
        landmarkDescriptions.put("alexander_garden", getString(R.string.alexander_garden_full_description));
        landmarkDescriptions.put("manege", getString(R.string.manege_full_description));
        landmarkDescriptions.put("historical_museum", getString(R.string.historical_museum_full_description));
        landmarkDescriptions.put("kazan_cathedral", getString(R.string.kazan_cathedral_full_description));
    }

    private void initializeLandmarkLocations() {
        if (landmarks != null) {
            for (Landmark landmark : landmarks) {
                if (landmark != null) {
                    landmarkLocations.put(landmark.getId(), new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
                }
            }
        }
    }

    private void addLandmarksToMap() {
        try {
            if (landmarks == null || mapView == null) return;
            
            Drawable markerIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_pin);
            
            for (Landmark landmark : landmarks) {
                if (landmark == null) continue;
                
                try {
                    Marker marker = new Marker(mapView);
                    marker.setPosition(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
                    marker.setTitle(getString(landmark.getNameResId()));
                    
                    // Добавляем краткое описание для отображения при клике
                    int descriptionResId = getResources().getIdentifier(
                        landmark.getId() + "_marker_description",
                        "string",
                        requireContext().getPackageName()
                    );
                    
                    // Проверяем, существует ли ресурс
                    String markerDescription;
                    if (descriptionResId != 0) {
                        markerDescription = getString(descriptionResId);
                    } else {
                        // Если ресурс не найден, используем короткое описание
                        markerDescription = getString(landmark.getShortDescriptionResId());
                        Log.w(TAG, "Marker description resource not found for: " + landmark.getId());
                    }
                    
                    marker.setSnippet(markerDescription);
                    
                    if (markerIcon != null) {
                        marker.setIcon(markerIcon);
                    }
                    
                    marker.setAnchor(0.5f, 1.0f);
                    
                    // Добавляем полное описание в TTSService
                    String fullDescription = getString(landmark.getFullDescriptionResId());
                    ttsService.addLandmarkText(landmark.getId(), fullDescription);
                    
                    // Добавляем обработчик клика по метке
                    marker.setOnMarkerClickListener((marker1, mapView) -> {
                        try {
                            marker1.showInfoWindow();
                            // Воспроизводим аудио при клике на маркер
                            ttsService.speakLandmark(landmark.getId());
                        } catch (Exception e) {
                            Log.e(TAG, "Error showing info window for marker: " + landmark.getId(), e);
                        }
                        return true;
                    });
                    
                    mapView.getOverlays().add(marker);
                } catch (Exception e) {
                    Log.e(TAG, "Error adding marker for landmark: " + landmark.getId(), e);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding landmarks to map", e);
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        if (getActivity() instanceof OnRouteMapClickListener) {
            ((OnRouteMapClickListener) getActivity()).onRouteMapClick(p);
        }
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public void onRouteMapClick(GeoPoint position) {
        if (currentRoute != null) {
            showRouteOnMap(currentRoute);
        }
    }

    @Override
    public void onRouteMarkerClick(String routeId) {
        // Обработка клика по маркеру маршрута
        Route route = RouteData.getRouteById(routeId);
        if (route != null) {
            showRouteOnMap(route);
        }
    }

    @Override
    public void onRoutePolylineClick(String routeId) {
        // Обработка клика по линии маршрута
        Route route = RouteData.getRouteById(routeId);
        if (route != null) {
            showRouteOnMap(route);
        }
    }

    private void showRouteOnMap(Route route) {
        clearRouteMarkers();
        currentRoute = route;
        
        if (route != null) {
            // Добавляем маркеры для точек маршрута
            for (Landmark landmark : route.getLandmarks()) {
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
                marker.setTitle(getString(landmark.getNameResId()));
                marker.setSnippet(getString(landmark.getShortDescriptionResId()));
                routeMarkers.add(marker);
                mapView.getOverlays().add(marker);
            }
            
            // Добавляем линию маршрута
            if (currentRoutePolyline != null) {
                mapView.getOverlays().remove(currentRoutePolyline);
            }
            
            currentRoutePolyline = new Polyline(mapView);
            List<GeoPoint> routePoints = new ArrayList<>();
            for (Landmark landmark : route.getLandmarks()) {
                routePoints.add(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
            }
            currentRoutePolyline.setPoints(routePoints);
            mapView.getOverlays().add(currentRoutePolyline);
            
            // Центрируем карту на первой точке маршрута
            if (!route.getLandmarks().isEmpty()) {
                Landmark firstLandmark = route.getLandmarks().get(0);
                mapController.animateTo(new GeoPoint(firstLandmark.getLatitude(), firstLandmark.getLongitude()));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (myLocationOverlay != null) {
            myLocationOverlay.enableMyLocation();
            myLocationOverlay.enableFollowLocation();
        }
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (myLocationOverlay != null) {
            myLocationOverlay.disableMyLocation();
            myLocationOverlay.disableFollowLocation();
        }
        stopLocationUpdates();
        ttsService.stopSpeaking();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearRouteMarkers();
        if (currentRoutePolyline != null) {
            mapView.getOverlays().remove(currentRoutePolyline);
        }
    }

    private void clearRouteMarkers() {
        for (Marker marker : routeMarkers) {
            mapView.getOverlays().remove(marker);
        }
        routeMarkers.clear();
    }

    public void showLandmarkOnMap(Landmark landmark) {
        try {
            if (landmark != null && mapView != null) {
                GeoPoint point = new GeoPoint(landmark.getLatitude(), landmark.getLongitude());
                mapView.getController().animateTo(point);
                mapView.getController().setZoom(18.0);
                
                Marker marker = new Marker(mapView);
                marker.setPosition(point);
                marker.setTitle(getString(landmark.getNameResId()));
                marker.setSnippet(getString(landmark.getShortDescriptionResId()));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                
                mapView.getOverlays().add(marker);
                mapView.invalidate();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error showing landmark on map: " + e.getMessage());
            Toast.makeText(requireContext(), R.string.error_showing_landmark, Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationUpdates() {
        locationUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myLocationOverlay != null && myLocationOverlay.getMyLocation() != null) {
                    Location userLocation = new Location("");
                    GeoPoint geoPoint = myLocationOverlay.getMyLocation();
                    userLocation.setLatitude(geoPoint.getLatitude());
                    userLocation.setLongitude(geoPoint.getLongitude());
                    checkProximityToLandmarks(userLocation);
                }
                locationUpdateHandler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates() {
        locationUpdateHandler.removeCallbacksAndMessages(null);
    }

    private void checkProximityToLandmarks(Location userLocation) {
        if (userLocation == null) return;

        String nearestLandmark = null;
        float minDistance = Float.MAX_VALUE;

        for (Map.Entry<String, GeoPoint> entry : landmarkLocations.entrySet()) {
            Location landmarkLocation = new Location("");
            landmarkLocation.setLatitude(entry.getValue().getLatitude());
            landmarkLocation.setLongitude(entry.getValue().getLongitude());

            float distance = userLocation.distanceTo(landmarkLocation);
            if (distance < PROXIMITY_THRESHOLD && distance < minDistance) {
                minDistance = distance;
                nearestLandmark = entry.getKey();
            }
        }

        if (nearestLandmark != null && !nearestLandmark.equals(currentSpeakingLandmark)) {
            currentSpeakingLandmark = nearestLandmark;
            String description = landmarkDescriptions.get(nearestLandmark);
            if (description != null) {
                ttsService.addLandmarkText(nearestLandmark, description);
                ttsService.speakLandmark(nearestLandmark);
            }
        } else if (nearestLandmark == null && currentSpeakingLandmark != null) {
            ttsService.stopSpeaking();
            currentSpeakingLandmark = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsService.shutdown();
    }
} 