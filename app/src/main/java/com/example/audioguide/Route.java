package com.example.audioguide;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private String name;
    private String description;
    private List<Landmark> landmarks;

    public Route(String name, String description) {
        this.name = name;
        this.description = description;
        this.landmarks = new ArrayList<>();
    }

    public void addLandmark(Landmark landmark) {
        landmarks.add(landmark);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }
} 