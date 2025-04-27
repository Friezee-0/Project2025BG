package com.example.audioguide;

import java.util.ArrayList;
import java.util.List;

public class LandmarkData {
    public static List<Landmark> getLandmarks() {
        List<Landmark> landmarks = new ArrayList<>();
        
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