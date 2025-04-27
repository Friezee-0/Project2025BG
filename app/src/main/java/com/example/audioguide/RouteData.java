package com.example.audioguide;

import java.util.ArrayList;
import java.util.List;

public class RouteData {
    private static final List<Route> routes = new ArrayList<>();

    static {
        // Исторический центр Москвы
        List<Landmark> historicalCenterLandmarks = new ArrayList<>();
        historicalCenterLandmarks.add(new Landmark(
            R.string.red_square,
            R.string.red_square_short_description,
            R.string.red_square_description,
            "https://example.com/red_square.jpg",
            55.7539,
            37.6208
        ));
        historicalCenterLandmarks.add(new Landmark(
            R.string.saint_basil,
            R.string.saint_basil_short_description,
            R.string.saint_basil_description,
            "https://example.com/saint_basil.jpg",
            55.7525,
            37.6231
        ));

        routes.add(new Route(
            R.string.historical_center_route,
            R.string.historical_center_description,
            R.string.historical_center_description,
            historicalCenterLandmarks,
            55.7539, 37.6208, // Красная площадь
            55.7525, 37.6231  // Собор Василия Блаженного
        ));

        // Арбат
        List<Landmark> arbatLandmarks = new ArrayList<>();
        arbatLandmarks.add(new Landmark(
            R.string.tretyakov,
            R.string.tretyakov_short_description,
            R.string.tretyakov_description,
            "https://example.com/tretyakov.jpg",
            55.7316,
            37.6205
        ));
        arbatLandmarks.add(new Landmark(
            R.string.bolshoi,
            R.string.bolshoi_short_description,
            R.string.bolshoi_description,
            "https://example.com/bolshoi.jpg",
            55.7601,
            37.6186
        ));

        routes.add(new Route(
            R.string.arbat_route,
            R.string.arbat_route_description,
            R.string.arbat_route_description,
            arbatLandmarks,
            55.7316, 37.6205, // Третьяковская галерея
            55.7601, 37.6186  // Большой театр
        ));

        // Золотое кольцо
        List<Landmark> goldenRingLandmarks = new ArrayList<>();
        goldenRingLandmarks.add(new Landmark(
            R.string.saint_basil,
            R.string.saint_basil_short_description,
            R.string.saint_basil_description,
            "https://example.com/saint_basil.jpg",
            55.7525,
            37.6231
        ));
        goldenRingLandmarks.add(new Landmark(
            R.string.kremlin,
            R.string.kremlin_short_description,
            R.string.kremlin_description,
            "https://example.com/kremlin.jpg",
            55.7520,
            37.6175
        ));

        routes.add(new Route(
            R.string.golden_ring_route,
            R.string.golden_ring_route_description,
            R.string.golden_ring_route_description,
            goldenRingLandmarks,
            55.7525, 37.6231, // Собор Василия Блаженного
            55.7520, 37.6175  // Кремль
        ));
    }

    public static List<Route> getRoutes() {
        return routes;
    }
} 