package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import org.osmdroid.api.IMapController;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        
        Configuration.getInstance().load(getContext(), 
            getContext().getSharedPreferences("osmdroid", 0));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);
        GeoPoint startPoint = new GeoPoint(55.7558, 37.6173);
        mapController.setCenter(startPoint);
        
        landmarks = new ArrayList<>();
        landmarks.add(new Landmark(
            "1", 
            "Красная площадь", 
            "Главная площадь Москвы",
            "Красная площадь - главная площадь Москвы, расположенная между Московским Кремлем и Китай-городом.",
            55.7539, 
            37.6208, 
            "https://example.com/red_square.jpg"
        ));
        
        landmarks.add(new Landmark(
            "2", 
            "Собор Василия Блаженного", 
            "Православный храм на Красной площади",
            "Собор Василия Блаженного - православный храм на Красной площади в Москве, памятник русской архитектуры.",
            55.7525, 
            37.6231, 
            "https://example.com/st_basil.jpg"
        ));

        landmarks.add(new Landmark(
            "3",
            "Кремль",
            "Исторический комплекс в центре Москвы",
            "Московский Кремль - древнейшая часть Москвы, главный общественно-политический, историко-художественный и религиозно-церковный комплекс столицы.",
            55.7520,
            37.6175,
            "https://example.com/kremlin.jpg"
        ));

        landmarks.add(new Landmark(
            "4",
            "Третьяковская галерея",
            "Художественный музей в Москве",
            "Государственная Третьяковская галерея - художественный музей в Москве, основанный в 1856 году купцом Павлом Третьяковым.",
            55.7316,
            37.6205,
            "https://example.com/tretyakov.jpg"
        ));

        landmarks.add(new Landmark(
            "5",
            "Большой театр",
            "Один из крупнейших в России театров оперы и балета",
            "Большой театр - один из крупнейших в России и один из самых значительных в мире театров оперы и балета.",
            55.7601,
            37.6186,
            "https://example.com/bolshoi.jpg"
        ));

        landmarks.add(new Landmark(
            "6",
            "Храм Христа Спасителя",
            "Кафедральный собор Русской православной церкви",
            "Храм Христа Спасителя - кафедральный собор Русской православной церкви, расположенный в Москве на левом берегу Москвы-реки.",
            55.7017,
            37.6083,
            "https://example.com/christ_savior.jpg"
        ));

        landmarks.add(new Landmark(
            "7",
            "Государственный исторический музей",
            "Национальный исторический музей России",
            "Государственный исторический музей - национальный исторический музей России, расположенный на северной стороне Красной площади.",
            55.7553,
            37.6179,
            "https://example.com/historical_museum.jpg"
        ));
        
        for (Landmark landmark : landmarks) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(landmark.getLatitude(), landmark.getLongitude()));
            marker.setTitle(landmark.getName());
            marker.setSnippet(landmark.getShortDescription());
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