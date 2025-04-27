package com.example.audioguide;

public class Landmark {
    private final int nameResId;
    private final int shortDescriptionResId;
    private final int fullDescriptionResId;
    private final String imageUrl;
    private final double latitude;
    private final double longitude;

    public Landmark(int nameResId, int shortDescriptionResId, int fullDescriptionResId, String imageUrl, double latitude, double longitude) {
        this.nameResId = nameResId;
        this.shortDescriptionResId = shortDescriptionResId;
        this.fullDescriptionResId = fullDescriptionResId;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
} 