package com.example.audioguide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteData {
    private static final List<Route> routes = new ArrayList<>();

    static {
        initializeRoutes();
    }

    private static void initializeRoutes() {
        // Исторический центр Москвы
        List<String> historicalCenterLandmarks = Arrays.asList(
            "red_square",
            "saint_basil",
            "kremlin",
            "gum",
            "lenin_mausoleum",
            "alexander_garden",
            "manege",
            "historical_museum",
            "kazan_cathedral"
        );
        
        List<String> historicalCenterTips = Arrays.asList(
            "route_tip_morning",
            "route_tip_tickets",
            "route_tip_comfort",
            "route_tip_weather",
            "route_tip_breaks",
            "route_tip_transport",
            "route_tip_guide",
            "route_tip_photos",
            "route_tip_water"
        );
        
        routes.add(new Route(
            "historical_center",
            R.string.historical_center_route,
            R.string.historical_center_description,
            R.string.historical_center_duration,
            R.string.historical_center_distance,
            historicalCenterLandmarks,
            historicalCenterTips
        ));
        
        // Культурные места
        List<String> culturalLandmarks = Arrays.asList(
            "tretyakov",
            "bolshoi",
            "manege"
        );
        
        List<String> culturalTips = Arrays.asList(
            "route_tip_morning",
            "route_tip_tickets",
            "route_tip_comfort",
            "route_tip_weather",
            "route_tip_breaks",
            "route_tip_transport",
            "route_tip_guide",
            "route_tip_photos",
            "route_tip_water"
        );
        
        routes.add(new Route(
            "cultural_places",
            R.string.cultural_places,
            R.string.cultural_places_description,
            R.string.cultural_places_duration,
            R.string.cultural_places_distance,
            culturalLandmarks,
            culturalTips
        ));
        
        // Музеи
        List<String> museumLandmarks = Arrays.asList(
            "tretyakov",
            "historical_museum",
            "manege"
        );
        
        List<String> museumTips = Arrays.asList(
            "route_tip_morning",
            "route_tip_tickets",
            "route_tip_comfort",
            "route_tip_weather",
            "route_tip_breaks",
            "route_tip_transport",
            "route_tip_guide",
            "route_tip_photos",
            "route_tip_water"
        );
        
        routes.add(new Route(
            "museums",
            R.string.museums,
            R.string.museums_description,
            R.string.museums_duration,
            R.string.museums_distance,
            museumLandmarks,
            museumTips
        ));
    }

    public static List<Route> getRoutes() {
        return routes;
    }

    public static Route getRouteById(String id) {
        for (Route route : routes) {
            if (route.getId().equals(id)) {
                return route;
            }
        }
        return null;
    }
} 