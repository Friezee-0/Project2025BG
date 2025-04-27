package com.example.audioguide;

import java.util.ArrayList;
import java.util.List;

public class LandmarkData {
    public static List<Landmark> getLandmarks() {
        List<Landmark> landmarks = new ArrayList<>();
        
        // Красная площадь и окрестности
        landmarks.add(new Landmark(
            R.string.red_square,
            R.string.red_square_short_description,
            R.string.red_square_description,
            "https://example.com/red_square.jpg",
            55.7539,
            37.6208
        ));
        
        landmarks.add(new Landmark(
            R.string.saint_basil,
            R.string.saint_basil_short_description,
            R.string.saint_basil_description,
            "https://example.com/saint_basil.jpg",
            55.7525,
            37.6231
        ));
        
        landmarks.add(new Landmark(
            R.string.kremlin,
            R.string.kremlin_short_description,
            R.string.kremlin_description,
            "https://example.com/kremlin.jpg",
            55.7520,
            37.6175
        ));
        
        landmarks.add(new Landmark(
            R.string.gum,
            R.string.gum_short_description,
            R.string.gum_description,
            "https://example.com/gum.jpg",
            55.7544,
            37.6217
        ));
        
        landmarks.add(new Landmark(
            R.string.lenin_mausoleum,
            R.string.lenin_mausoleum_short_description,
            R.string.lenin_mausoleum_description,
            "https://example.com/lenin_mausoleum.jpg",
            55.7539,
            37.6218
        ));
        
        landmarks.add(new Landmark(
            R.string.alexander_garden,
            R.string.alexander_garden_short_description,
            R.string.alexander_garden_description,
            "https://example.com/alexander_garden.jpg",
            55.7528,
            37.6136
        ));
        
        landmarks.add(new Landmark(
            R.string.manege,
            R.string.manege_short_description,
            R.string.manege_description,
            "https://example.com/manege.jpg",
            55.7558,
            37.6139
        ));
        
        landmarks.add(new Landmark(
            R.string.historical_museum,
            R.string.historical_museum_short_description,
            R.string.historical_museum_description,
            "https://example.com/historical_museum.jpg",
            55.7558,
            37.6189
        ));
        
        landmarks.add(new Landmark(
            R.string.kazan_cathedral,
            R.string.kazan_cathedral_short_description,
            R.string.kazan_cathedral_description,
            "https://example.com/kazan_cathedral.jpg",
            55.7547,
            37.6197
        ));
        
        // Культурные места
        landmarks.add(new Landmark(
            R.string.tretyakov,
            R.string.tretyakov_short_description,
            R.string.tretyakov_description,
            "https://example.com/tretyakov.jpg",
            55.7415,
            37.6208
        ));
        
        landmarks.add(new Landmark(
            R.string.bolshoi,
            R.string.bolshoi_short_description,
            R.string.bolshoi_description,
            "https://example.com/bolshoi.jpg",
            55.7602,
            37.6186
        ));
        
        return landmarks;
    }
} 