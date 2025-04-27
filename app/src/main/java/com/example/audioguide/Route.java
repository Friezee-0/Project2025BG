package com.example.audioguide;

import java.util.List;

public class Route {
    private final int nameResId;
    private final int descriptionResId;
    private final int durationResId;
    private final int distanceResId;
    private final List<String> landmarkIds;
    private final List<String> tips;

    public Route(int nameResId, int descriptionResId, int durationResId, int distanceResId,
                List<String> landmarkIds, List<String> tips) {
        this.nameResId = nameResId;
        this.descriptionResId = descriptionResId;
        this.durationResId = durationResId;
        this.distanceResId = distanceResId;
        this.landmarkIds = landmarkIds;
        this.tips = tips;
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
} 