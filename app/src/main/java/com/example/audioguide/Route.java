package com.example.audioguide;

import java.util.List;
import java.util.ArrayList;

public class Route {
    private String id;
    private int nameResId;
    private int descriptionResId;
    private int durationResId;
    private int distanceResId;
    private List<String> landmarkIds;
    private List<String> tips;

    public Route(String id, int nameResId, int descriptionResId, int durationResId, 
                int distanceResId, List<String> landmarkIds, List<String> tips) {
        this.id = id;
        this.nameResId = nameResId;
        this.descriptionResId = descriptionResId;
        this.durationResId = durationResId;
        this.distanceResId = distanceResId;
        this.landmarkIds = landmarkIds;
        this.tips = tips;
    }

    public String getId() {
        return id;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getDescriptionResId() {
        return descriptionResId;
    }

    public int getDurationResId() {
        return durationResId;
    }

    public int getDistanceResId() {
        return distanceResId;
    }

    public List<String> getLandmarkIds() {
        return landmarkIds;
    }

    public List<String> getTips() {
        return tips;
    }

    public List<Landmark> getLandmarks() {
        List<Landmark> landmarks = new ArrayList<>();
        for (String id : landmarkIds) {
            Landmark landmark = LandmarkData.getLandmarkById(id);
            if (landmark != null) {
                landmarks.add(landmark);
            }
        }
        return landmarks;
    }
} 