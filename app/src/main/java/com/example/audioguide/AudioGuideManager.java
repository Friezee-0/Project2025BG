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
        this.settingsManager = SettingsManager.getInstance(context);
        this.routes = new ArrayList<>();

        initializeLandmarks();
        initializeRoutes();
        initializeTTS();
    }

    private void initializeLandmarks() {
        landmarks.put("red_square", new Landmark(
            R.string.red_square,
            R.string.red_square_description,
            R.string.red_square_short_description,
            "https://example.com/red_square.jpg",
            55.7539, 37.6208
        ));

        landmarks.put("saint_basil", new Landmark(
            R.string.saint_basil,
            R.string.saint_basil_description,
            R.string.saint_basil_short_description,
            "https://example.com/saint_basil.jpg",
            55.7525, 37.6231
        ));

        landmarks.put("kremlin", new Landmark(
            R.string.kremlin,
            R.string.kremlin_description,
            R.string.kremlin_short_description,
            "https://example.com/kremlin.jpg",
            55.7520, 37.6175
        ));

        landmarks.put("tretyakov", new Landmark(
            R.string.tretyakov,
            R.string.tretyakov_description,
            R.string.tretyakov_short_description,
            "https://example.com/tretyakov.jpg",
            55.7415, 37.6208
        ));

        landmarks.put("bolshoi", new Landmark(
            R.string.bolshoi,
            R.string.bolshoi_description,
            R.string.bolshoi_short_description,
            "https://example.com/bolshoi.jpg",
            55.7602, 37.6186
        ));

        // Новые достопримечательности
        landmarks.put("gum", new Landmark(
            R.string.gum,
            R.string.gum_description,
            R.string.gum_short_description,
            "https://example.com/gum.jpg",
            55.7544, 37.6217
        ));

        landmarks.put("lenin_mausoleum", new Landmark(
            R.string.lenin_mausoleum,
            R.string.lenin_mausoleum_description,
            R.string.lenin_mausoleum_short_description,
            "https://example.com/lenin_mausoleum.jpg",
            55.7539, 37.6218
        ));

        landmarks.put("alexander_garden", new Landmark(
            R.string.alexander_garden,
            R.string.alexander_garden_description,
            R.string.alexander_garden_short_description,
            "https://example.com/alexander_garden.jpg",
            55.7528, 37.6136
        ));

        landmarks.put("manege", new Landmark(
            R.string.manege,
            R.string.manege_description,
            R.string.manege_short_description,
            "https://example.com/manege.jpg",
            55.7558, 37.6139
        ));

        landmarks.put("historical_museum", new Landmark(
            R.string.historical_museum,
            R.string.historical_museum_description,
            R.string.historical_museum_short_description,
            "https://example.com/historical_museum.jpg",
            55.7558, 37.6189
        ));

        landmarks.put("kazan_cathedral", new Landmark(
            R.string.kazan_cathedral,
            R.string.kazan_cathedral_description,
            R.string.kazan_cathedral_short_description,
            "https://example.com/kazan_cathedral.jpg",
            55.7547, 37.6197
        ));
    }

    private void initializeRoutes() {
        try {
            // Исторический центр
            List<String> historicalCenterLandmarks = new ArrayList<>(Arrays.asList(
                "red_square",
                "saint_basil",
                "kremlin",
                "gum",
                "lenin_mausoleum",
                "alexander_garden"
            ));
            
            // Проверяем существование достопримечательностей
            historicalCenterLandmarks.removeIf(id -> !landmarks.containsKey(id));
            
            List<String> historicalCenterTips = new ArrayList<>(Arrays.asList(
                "Начните с Красной площади",
                "Посетите Мавзолей Ленина в первой половине дня",
                "Забронируйте билеты в Кремль заранее"
            ));
            
            if (!historicalCenterLandmarks.isEmpty()) {
                routes.add(new Route(
                    R.string.historical_center_route,
                    R.string.historical_center_description,
                    R.string.historical_center_duration,
                    R.string.historical_center_distance,
                    historicalCenterLandmarks,
                    historicalCenterTips
                ));
            }
            
            // Арбат
            List<String> arbatLandmarks = new ArrayList<>(Arrays.asList(
                "manege",
                "historical_museum",
                "kazan_cathedral"
            ));
            
            // Проверяем существование достопримечательностей
            arbatLandmarks.removeIf(id -> !landmarks.containsKey(id));
            
            List<String> arbatTips = new ArrayList<>(Arrays.asList(
                "Лучшее время для посещения - вечер",
                "Попробуйте местные кафе и рестораны",
                "Обратите внимание на уличных музыкантов"
            ));
            
            if (!arbatLandmarks.isEmpty()) {
                routes.add(new Route(
                    R.string.arbat_route,
                    R.string.arbat_description,
                    R.string.arbat_duration,
                    R.string.arbat_distance,
                    arbatLandmarks,
                    arbatTips
                ));
            }
            
            // Золотое кольцо
            List<String> goldenRingLandmarks = new ArrayList<>(Arrays.asList(
                "saint_basil",
                "kazan_cathedral",
                "tretyakov"
            ));
            
            // Проверяем существование достопримечательностей
            goldenRingLandmarks.removeIf(id -> !landmarks.containsKey(id));
            
            List<String> goldenRingTips = new ArrayList<>(Arrays.asList(
                "Планируйте поездку на несколько дней",
                "Возьмите с собой теплую одежду",
                "Попробуйте местную кухню"
            ));
            
            if (!goldenRingLandmarks.isEmpty()) {
                routes.add(new Route(
                    R.string.golden_ring_route,
                    R.string.golden_ring_description,
                    R.string.golden_ring_duration,
                    R.string.golden_ring_distance,
                    goldenRingLandmarks,
                    goldenRingTips
                ));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing routes", e);
        }
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

    public Map<String, Landmark> getLandmarks() {
        return landmarks;
    }
} 