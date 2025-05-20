package com.example.audioguide;

import org.osmdroid.util.GeoPoint;

public interface OnRouteMapClickListener {
    void onRouteMapClick(GeoPoint position);
    void onRouteMarkerClick(String routeId);
    void onRoutePolylineClick(String routeId);
} 