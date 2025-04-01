package com.example.audioguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AudioGuideManager implements LocationListener {
    private static final String TAG = "AudioGuideManager";
    private static final float PROXIMITY_RADIUS = 100;
    private static final long MIN_TIME = 10000;
    private static final float MIN_DISTANCE = 10;

    private final Context context;
    private final LocationManager locationManager;
    private TextToSpeech textToSpeech;
    private final Map<String, Landmark> landmarks;
    private final Map<String, Boolean> playedLandmarks;
    private final SettingsManager settingsManager;

    public AudioGuideManager(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.landmarks = new HashMap<>();
        this.playedLandmarks = new HashMap<>();
        this.settingsManager = new SettingsManager(context);

        initializeLandmarks();

        TextToSpeech.OnInitListener initListener = status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale(settingsManager.getTTSLanguage()));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language not supported");
                }
            } else {
                Log.e(TAG, "TTS initialization failed");
            }
        };
        
        this.textToSpeech = new TextToSpeech(context, initListener);
    }

    private void initializeLandmarks() {
        landmarks.put("red_square", new Landmark(
            "red_square",
            context.getString(R.string.red_square),
            context.getString(R.string.red_square_description),
            55.7539, 37.6208, ""));

        landmarks.put("saint_basil", new Landmark(
            "saint_basil",
            context.getString(R.string.saint_basil),
            context.getString(R.string.saint_basil_description),
            55.7525, 37.6231, ""));

        landmarks.put("kremlin", new Landmark(
            "kremlin",
            context.getString(R.string.kremlin),
            context.getString(R.string.kremlin_description),
            55.7520, 37.6175, ""));

        landmarks.put("tretyakov", new Landmark(
            "tretyakov",
            context.getString(R.string.tretyakov),
            context.getString(R.string.tretyakov_description),
            55.7415, 37.6208, ""));

        landmarks.put("bolshoi", new Landmark(
            "bolshoi",
            context.getString(R.string.bolshoi),
            context.getString(R.string.bolshoi_description),
            55.7601, 37.6186, ""));
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, 
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                this
            );
        }
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        for (Map.Entry<String, Landmark> entry : landmarks.entrySet()) {
            Landmark landmark = entry.getValue();
            float[] distance = new float[1];
            
            Location.distanceBetween(
                location.getLatitude(), location.getLongitude(),
                landmark.getLatitude(), landmark.getLongitude(),
                distance
            );

            if (distance[0] <= PROXIMITY_RADIUS && !playedLandmarks.containsKey(entry.getKey())) {
                playDescription(landmark);
                playedLandmarks.put(entry.getKey(), true);
            } else if (distance[0] > PROXIMITY_RADIUS) {
                playedLandmarks.remove(entry.getKey());
            }
        }
    }

    private void playDescription(Landmark landmark) {
        if (textToSpeech != null) {
            textToSpeech.speak(
                landmark.getName() + ". " + landmark.getDescription(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                landmark.getName()
            );
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        stopLocationUpdates();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
} 