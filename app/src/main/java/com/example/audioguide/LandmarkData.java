package com.example.audioguide;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class LandmarkData {
    private static final Map<String, Landmark> landmarks = new HashMap<>();

    static {
        // Инициализация достопримечательностей
        landmarks.put("red_square", new Landmark(
            "red_square",
            R.string.red_square_name,
            R.string.red_square_short_description,
            R.string.red_square_full_description,
            R.drawable.red_square,
            55.7539,
            37.6208
        ));
        
        landmarks.put("saint_basil", new Landmark(
            "saint_basil",
            R.string.saint_basil_name,
            R.string.saint_basil_short_description,
            R.string.saint_basil_full_description,
            R.drawable.saint_basil,
            55.7525,
            37.6231
        ));
        
        landmarks.put("kremlin", new Landmark(
            "kremlin",
            R.string.kremlin_name,
            R.string.kremlin_short_description,
            R.string.kremlin_full_description,
            R.drawable.kremlin,
            55.7520,
            37.6175
        ));
        
        landmarks.put("gum", new Landmark(
            "gum",
            R.string.gum_name,
            R.string.gum_short_description,
            R.string.gum_full_description,
            R.drawable.gum,
            55.7547,
            37.6214
        ));
        
        landmarks.put("lenin_mausoleum", new Landmark(
            "lenin_mausoleum",
            R.string.lenin_mausoleum_name,
            R.string.lenin_mausoleum_short_description,
            R.string.lenin_mausoleum_full_description,
            R.drawable.lenin_mausoleum,
            55.7539,
            37.6208
        ));
        
        landmarks.put("alexander_garden", new Landmark(
            "alexander_garden",
            R.string.alexander_garden_name,
            R.string.alexander_garden_short_description,
            R.string.alexander_garden_full_description,
            R.drawable.alexander_garden,
            55.7520,
            37.6175
        ));
        
        landmarks.put("manege", new Landmark(
            "manege",
            R.string.manege_name,
            R.string.manege_short_description,
            R.string.manege_full_description,
            R.drawable.manege,
            55.7520,
            37.6175
        ));
        
        landmarks.put("historical_museum", new Landmark(
            "historical_museum",
            R.string.historical_museum_name,
            R.string.historical_museum_short_description,
            R.string.historical_museum_full_description,
            R.drawable.historical_museum,
            55.7547,
            37.6214
        ));

        landmarks.put("tretyakov", new Landmark(
            "tretyakov",
            R.string.tretyakov_name,
            R.string.tretyakov_short_description,
            R.string.tretyakov_full_description,
            R.drawable.tretyakov,
            55.7415,
            37.6208
        ));
        
        landmarks.put("bolshoi", new Landmark(
            "bolshoi",
            R.string.bolshoi_name,
            R.string.bolshoi_short_description,
            R.string.bolshoi_full_description,
            R.drawable.bolshoi,
            55.7602,
            37.6186
        ));

        landmarks.put("novokosino_memorial", new Landmark(
            "novokosino_memorial",
            R.string.novokosino_memorial_name,
            R.string.novokosino_memorial_short_description,
            R.string.novokosino_memorial_full_description,
            R.drawable.novokosino_memorial,
            55.738917,
            37.882944
        ));
    }

    public static Landmark getLandmarkById(String id) {
        return landmarks.get(id);
    }

    public static List<Landmark> getLandmarks() {
        return new ArrayList<>(landmarks.values());
    }
} 