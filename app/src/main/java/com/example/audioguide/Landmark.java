package com.example.audioguide;

public class Landmark {
    private final String id;
    private final int nameResId;
    private final int shortDescriptionResId;
    private final int fullDescriptionResId;
    private final int imageResId;
    private final double latitude;
    private final double longitude;

    public Landmark(String id, int nameResId, int shortDescriptionResId, int fullDescriptionResId, int imageResId, double latitude, double longitude) {
        this.id = id;
        this.nameResId = nameResId;
        this.shortDescriptionResId = shortDescriptionResId;
        this.fullDescriptionResId = fullDescriptionResId;
        this.imageResId = imageResId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
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

    public int getImageResId() {
        return imageResId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
} 