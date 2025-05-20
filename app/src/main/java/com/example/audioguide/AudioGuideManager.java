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
            "red_square",
            R.string.red_square_name,
            R.string.red_square_short_description,
            R.string.red_square_full_description,
            R.drawable.red_square,
            55.7539, 37.6208
        ));

        landmarks.put("saint_basil", new Landmark(
            "saint_basil",
            R.string.saint_basil_name,
            R.string.saint_basil_short_description,
            R.string.saint_basil_full_description,
            R.drawable.saint_basil,
            55.7525, 37.6231
        ));

        landmarks.put("kremlin", new Landmark(
            "kremlin",
            R.string.kremlin_name,
            R.string.kremlin_short_description,
            R.string.kremlin_full_description,
            R.drawable.kremlin,
            55.7520, 37.6175
        ));

        landmarks.put("tretyakov", new Landmark(
            "tretyakov",
            R.string.tretyakov_name,
            R.string.tretyakov_short_description,
            R.string.tretyakov_full_description,
            R.drawable.tretyakov,
            55.7415, 37.6208
        ));

        landmarks.put("bolshoi", new Landmark(
            "bolshoi",
            R.string.bolshoi_name,
            R.string.bolshoi_short_description,
            R.string.bolshoi_full_description,
            R.drawable.bolshoi,
            55.7602, 37.6186
        ));

        landmarks.put("gum", new Landmark(
            "gum",
            R.string.gum_name,
            R.string.gum_short_description,
            R.string.gum_full_description,
            R.drawable.gum,
            55.7547, 37.6214
        ));

        landmarks.put("lenin_mausoleum", new Landmark(
            "lenin_mausoleum",
            R.string.lenin_mausoleum_name,
            R.string.lenin_mausoleum_short_description,
            R.string.lenin_mausoleum_full_description,
            R.drawable.lenin_mausoleum,
            55.7539, 37.6208
        ));

        landmarks.put("alexander_garden", new Landmark(
            "alexander_garden",
            R.string.alexander_garden_name,
            R.string.alexander_garden_short_description,
            R.string.alexander_garden_full_description,
            R.drawable.alexander_garden,
            55.7520, 37.6175
        ));

        landmarks.put("manege", new Landmark(
            "manege",
            R.string.manege_name,
            R.string.manege_short_description,
            R.string.manege_full_description,
            R.drawable.manege,
            55.7520, 37.6175
        ));

        landmarks.put("historical_museum", new Landmark(
            "historical_museum",
            R.string.historical_museum_name,
            R.string.historical_museum_short_description,
            R.string.historical_museum_full_description,
            R.drawable.historical_museum,
            55.7547, 37.6214
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
                    "historical_center",
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
                    "arbat",
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
                    "golden_ring",
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