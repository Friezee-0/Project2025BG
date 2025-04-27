package com.example.audioguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class SettingsManager {
    private static final String PREFS_NAME = "AudioGuidePrefs";
    private static final String KEY_APP_LANGUAGE = "app_language";
    private static final String KEY_TTS_LANGUAGE = "tts_language";
    private static final String KEY_THEME = "theme";
    private static final String KEY_VOICE = "voice";
    private static final String KEY_IS_GUEST = "is_guest";
    
    private static SettingsManager instance;
    private final SharedPreferences prefs;
    private final Context context;
    private TextToSpeech textToSpeech;

    public SettingsManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        initTextToSpeech();
    }

    public static SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                setTtsLanguage(getTTSLanguage());
            }
        });
    }

    public void setAppLanguage(String language) {
        prefs.edit().putString(KEY_APP_LANGUAGE, language).apply();
        updateLocale(language);
    }

    public String getAppLanguage() {
        return prefs.getString(KEY_APP_LANGUAGE, "ru");
    }

    public void setTtsLanguage(String language) {
        prefs.edit().putString(KEY_TTS_LANGUAGE, language).apply();
        if (textToSpeech != null) {
            Locale locale = language.equals("ru") ? new Locale("ru") : Locale.ENGLISH;
            textToSpeech.setLanguage(locale);
        }
    }

    public String getTTSLanguage() {
        return prefs.getString(KEY_TTS_LANGUAGE, "ru");
    }

    private void updateLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        
        context.createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public void setTheme(String theme) {
        prefs.edit().putString(KEY_THEME, theme).apply();
    }

    public String getTheme() {
        return prefs.getString(KEY_THEME, "light");
    }

    public boolean isDarkTheme() {
        return getTheme().equals("dark");
    }

    public void setVoice(String voice) {
        prefs.edit().putString(KEY_VOICE, voice).apply();
    }

    public String getVoice() {
        return prefs.getString(KEY_VOICE, "default");
    }

    public void setGuestMode(boolean isGuest) {
        prefs.edit().putBoolean(KEY_IS_GUEST, isGuest).apply();
    }

    public boolean isGuest() {
        return prefs.getBoolean(KEY_IS_GUEST, false);
    }

    public void speak(String text) {
        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
} 