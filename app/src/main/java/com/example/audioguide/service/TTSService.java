package com.example.audioguide.service;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class TTSService {
    private static final String TAG = "TTSService";
    private static TTSService instance;
    private TextToSpeech textToSpeech;
    private boolean isInitialized = false;
    private String currentLandmarkId = null;
    private final ConcurrentHashMap<String, String> landmarkTexts = new ConcurrentHashMap<>();
    private Context context;

    private TTSService(Context context) {
        this.context = context.getApplicationContext();
        initTextToSpeech();
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true;
                // Устанавливаем язык по умолчанию
                Locale locale = new Locale("ru");
                int result = textToSpeech.setLanguage(locale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language not supported, falling back to default");
                    textToSpeech.setLanguage(Locale.getDefault());
                }
                setupUtteranceListener();
                Log.d(TAG, "TTS initialized successfully");
            } else {
                Log.e(TAG, "TTS initialization failed with status: " + status);
            }
        });
    }

    public static synchronized TTSService getInstance(Context context) {
        if (instance == null) {
            instance = new TTSService(context.getApplicationContext());
        }
        return instance;
    }

    private void setupUtteranceListener() {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d(TAG, "Started speaking: " + utteranceId);
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d(TAG, "Finished speaking: " + utteranceId);
                if (utteranceId.equals(currentLandmarkId)) {
                    currentLandmarkId = null;
                }
            }

            @Override
            public void onError(String utteranceId) {
                Log.e(TAG, "Error speaking: " + utteranceId);
                currentLandmarkId = null;
            }
        });
    }

    public void setLanguage(Locale locale) {
        if (isInitialized && textToSpeech != null) {
            int result = textToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language not supported: " + locale);
                // Пробуем установить язык по умолчанию
                textToSpeech.setLanguage(Locale.getDefault());
            } else {
                Log.d(TAG, "Language set successfully: " + locale);
            }
        } else {
            Log.e(TAG, "TTS not initialized");
        }
    }

    public void updateLanguage(String languageCode) {
        Locale locale;
        if (languageCode.equals("ru")) {
            locale = new Locale("ru");
        } else if (languageCode.equals("en")) {
            locale = Locale.ENGLISH;
        } else {
            locale = Locale.getDefault();
        }
        setLanguage(locale);
    }

    public void addLandmarkText(String landmarkId, String text) {
        if (text != null && !text.isEmpty()) {
            landmarkTexts.put(landmarkId, text);
            Log.d(TAG, "Added text for landmark: " + landmarkId);
        } else {
            Log.w(TAG, "Attempted to add empty text for landmark: " + landmarkId);
        }
    }

    public void removeLandmarkText(String landmarkId) {
        landmarkTexts.remove(landmarkId);
        Log.d(TAG, "Removed text for landmark: " + landmarkId);
    }

    public void speakLandmark(String landmarkId) {
        if (!isInitialized || textToSpeech == null) {
            Log.e(TAG, "TTS not initialized");
            return;
        }

        String text = landmarkTexts.get(landmarkId);
        if (text != null && !landmarkId.equals(currentLandmarkId)) {
            stopSpeaking();
            currentLandmarkId = landmarkId;
            int result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, landmarkId);
            if (result == TextToSpeech.ERROR) {
                Log.e(TAG, "Error speaking text for landmark: " + landmarkId);
            } else {
                Log.d(TAG, "Started speaking for landmark: " + landmarkId);
            }
        } else {
            Log.w(TAG, "No text available for landmark: " + landmarkId);
        }
    }

    public void stopSpeaking() {
        if (isInitialized && textToSpeech != null) {
            textToSpeech.stop();
            currentLandmarkId = null;
            Log.d(TAG, "Stopped speaking");
        }
    }

    public boolean isSpeaking() {
        return currentLandmarkId != null;
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            isInitialized = false;
            textToSpeech = null;
            Log.d(TAG, "TTS service shut down");
        }
    }
} 