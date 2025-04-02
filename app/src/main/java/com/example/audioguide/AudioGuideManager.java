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
    private MediaPlayer mediaPlayer;
    private boolean isSpeaking = false;

    public AudioGuideManager(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.landmarks = new HashMap<>();
        this.playedLandmarks = new HashMap<>();
        this.settingsManager = new SettingsManager(context);

        initializeLandmarks();
        initializeTextToSpeech();
    }

    private void initializeLandmarks() {
        landmarks.put("red_square", new Landmark(
            "1",
            "Красная площадь",
            "Главная площадь Москвы",
            "Красная площадь - главная площадь Москвы, расположенная между Московским Кремлем и Китай-городом. Площадь является одной из самых известных достопримечательностей России и объектом Всемирного наследия ЮНЕСКО.",
            55.7539,
            37.6208,
            "https://example.com/red_square.jpg"
        ));

        landmarks.put("saint_basil", new Landmark(
            "2",
            "Собор Василия Блаженного",
            "Православный храм на Красной площади",
            "Собор Василия Блаженного - православный храм на Красной площади в Москве, памятник русской архитектуры. Храм был построен в 1555-1561 годах по приказу царя Ивана Грозного в память о победе над Казанским ханством.",
            55.7525,
            37.6231,
            "https://example.com/st_basil.jpg"
        ));

        landmarks.put("kremlin", new Landmark(
            "3",
            "Кремль",
            "Исторический комплекс в центре Москвы",
            "Московский Кремль - древнейшая часть Москвы, главный общественно-политический, историко-художественный и религиозно-церковный комплекс столицы, официальная резиденция Президента Российской Федерации.",
            55.7520,
            37.6175,
            "https://example.com/kremlin.jpg"
        ));

        landmarks.put("tretyakov", new Landmark(
            "4",
            "Третьяковская галерея",
            "Художественный музей в Москве",
            "Государственная Третьяковская галерея - художественный музей в Москве, основанный в 1856 году купцом Павлом Третьяковым. Галерея обладает одной из крупнейших в мире коллекций русского изобразительного искусства.",
            55.7316,
            37.6205,
            "https://example.com/tretyakov.jpg"
        ));

        landmarks.put("bolshoi", new Landmark(
            "5",
            "Большой театр",
            "Один из крупнейших в России театров оперы и балета",
            "Большой театр - один из крупнейших в России и один из самых значительных в мире театров оперы и балета. Комплекс зданий театра расположен в центре Москвы, на Театральной площади.",
            55.7601,
            37.6186,
            "https://example.com/bolshoi.jpg"
        ));
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("ru"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language not supported");
                }
            } else {
                Log.e(TAG, "Initialization failed");
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
                landmark.getName() + ". " + landmark.getShortDescription(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                landmark.getName()
            );
        }
    }

    public void speak(String landmarkId) {
        Landmark landmark = landmarks.get(landmarkId);
        if (landmark != null) {
            String text = landmark.getName() + ". " + landmark.getShortDescription();
            if (!isSpeaking) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                isSpeaking = true;
            }
        }
    }

    public void stopSpeaking() {
        if (isSpeaking) {
            textToSpeech.stop();
            isSpeaking = false;
        }
    }

    public void playAudio(String landmarkId) {
        stopSpeaking();
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
        stopSpeaking();
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