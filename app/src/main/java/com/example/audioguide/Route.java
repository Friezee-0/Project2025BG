package com.example.audioguide;

public class Route {
    private String id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String duration;
    private String distance;
    private String tips;

    public Route(String id, String name, String shortDescription, String fullDescription,
                String duration, String distance, String tips) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.duration = duration;
        this.distance = distance;
        this.tips = tips;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getShortDescription() { return shortDescription; }
    public String getFullDescription() { return fullDescription; }
    public String getDuration() { return duration; }
    public String getDistance() { return distance; }
    public String getTips() { return tips; }
} 