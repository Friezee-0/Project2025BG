package com.example.audioguide;

import java.util.List;

public class Route {
    private final int nameResId;
    private final int shortDescriptionResId;
    private final int fullDescriptionResId;
    private final List<Landmark> landmarks;
    private final double startLatitude;
    private final double startLongitude;
    private final double endLatitude;
    private final double endLongitude;

    public Route(int nameResId, int shortDescriptionResId, int fullDescriptionResId,
                List<Landmark> landmarks, double startLatitude, double startLongitude,
                double endLatitude, double endLongitude) {
        this.nameResId = nameResId;
        this.shortDescriptionResId = shortDescriptionResId;
        this.fullDescriptionResId = fullDescriptionResId;
        this.landmarks = landmarks;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getShortDescriptionResId() {
        return shortDescriptionResId;
    }

    public int getFullDescriptionResId() {
        return fullDescriptionResId;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }
} 