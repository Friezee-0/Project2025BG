package com.example.audioguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private final List<Route> routes;
    private MediaPlayer mediaPlayer;
    private boolean isSpeaking = false;
    private boolean isInitialized = false;

    public AudioGuideManager(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.landmarks = new HashMap<>();
        this.playedLandmarks = new HashMap<>();
        this.settingsManager = new SettingsManager(context);
        this.routes = new ArrayList<>();

        initializeLandmarks();
        initializeRoutes();
        initializeTTS();
    }

    private void initializeLandmarks() {
        landmarks.put("red_square", new Landmark(
            R.string.red_square,
            R.string.red_square_short_description,
            R.string.red_square_description,
            "https://example.com/red_square.jpg",
            55.7539,
            37.6208
        ));

        landmarks.put("saint_basil", new Landmark(
            R.string.saint_basil,
            R.string.saint_basil_short_description,
            R.string.saint_basil_description,
            "https://example.com/saint_basil.jpg",
            55.7525,
            37.6231
        ));

        landmarks.put("kremlin", new Landmark(
            R.string.kremlin,
            R.string.kremlin_short_description,
            R.string.kremlin_description,
            "https://example.com/kremlin.jpg",
            55.7520,
            37.6175
        ));

        landmarks.put("tretyakov", new Landmark(
            R.string.tretyakov,
            R.string.tretyakov_short_description,
            R.string.tretyakov_description,
            "https://example.com/tretyakov.jpg",
            55.7316,
            37.6205
        ));

        landmarks.put("bolshoi", new Landmark(
            R.string.bolshoi,
            R.string.bolshoi_short_description,
            R.string.bolshoi_description,
            "https://example.com/bolshoi.jpg",
            55.7601,
            37.6186
        ));
    }

    private void initializeRoutes() {
        List<Landmark> historicalCenterLandmarks = Arrays.asList(
            landmarks.get("red_square"),
            landmarks.get("saint_basil"),
            landmarks.get("kremlin"),
            landmarks.get("tretyakov"),
            landmarks.get("bolshoi")
        );

        routes.add(new Route(
            R.string.historical_center_route,
            R.string.historical_center_description,
            R.string.historical_center_description,
            historicalCenterLandmarks,
            55.7539, 37.6208, // Красная площадь
            55.7601, 37.6186  // Большой театр
        ));

        List<Landmark> arbatLandmarks = Arrays.asList(
            landmarks.get("tretyakov"),
            landmarks.get("bolshoi")
        );

        routes.add(new Route(
            R.string.arbat_route,
            R.string.arbat_route_description,
            R.string.arbat_route_description,
            arbatLandmarks,
            55.7316, 37.6205, // Третьяковская галерея
            55.7601, 37.6186  // Большой театр
        ));

        List<Landmark> goldenRingLandmarks = Arrays.asList(
            landmarks.get("saint_basil"),
            landmarks.get("kremlin")
        );

        routes.add(new Route(
            R.string.golden_ring_route,
            R.string.golden_ring_route_description,
            R.string.golden_ring_route_description,
            goldenRingLandmarks,
            55.7525, 37.6231, // Собор Василия Блаженного
            55.7520, 37.6175  // Кремль
        ));
    }

    public List<Route> getRoutes() {
        return routes;
    }

    private void initializeTTS() {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true;
                setupTTS();
            } else {
                Log.e(TAG, "TextToSpeech initialization failed");
            }
        });
    }

    private void setupTTS() {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Обработка начала воспроизведения
            }

            @Override
            public void onDone(String utteranceId) {
                // Обработка завершения воспроизведения
            }

            @Override
            public void onError(String utteranceId) {
                // Обработка ошибки воспроизведения
            }
        });
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
                startGuide(landmark);
                playedLandmarks.put(entry.getKey(), true);
            } else if (distance[0] > PROXIMITY_RADIUS) {
                playedLandmarks.remove(entry.getKey());
            }
        }
    }

    public void startGuide(Landmark landmark) {
        if (isSpeaking) {
            stopGuide();
        }

        String text = context.getString(landmark.getNameResId()) + ". " + 
                     context.getString(landmark.getShortDescriptionResId());
        
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "landmark_guide");
        isSpeaking = true;
    }

    public void stopGuide() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            isSpeaking = false;
        }
    }

    public void playAudio(String landmarkId) {
        stopGuide();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        int resourceId = context.getResources().getIdentifier(
            "audio_" + landmarkId,
            "raw",
            context.getPackageName()
        );

        if (resourceId != 0) {
            mediaPlayer = MediaPlayer.create(context, resourceId);
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
            mediaPlayer.start();
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void shutdown() {
        stopGuide();
        stopAudio();
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