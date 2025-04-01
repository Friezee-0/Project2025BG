package com.example.audioguide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import androidx.annotation.Nullable;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final int RC_SIGN_IN = 9001;
    private SettingsManager settingsManager;
    private AuthManager authManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        settingsManager = new SettingsManager(requireContext());
        authManager = new AuthManager(requireContext());

        // Настройка кнопки входа через Google
        Preference signInPref = findPreference("google_sign_in");
        if (signInPref != null) {
            updateSignInPreference(signInPref);
            signInPref.setOnPreferenceClickListener(preference -> {
                if (!authManager.isSignedIn() || authManager.isGuest()) {
                    startSignIn();
                } else {
                    authManager.signOut();
                    updateSignInPreference(signInPref);
                    Toast.makeText(requireContext(), "Выполнен выход из аккаунта", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }

        // Настройка языка интерфейса
        ListPreference appLanguagePref = findPreference("app_language");
        if (appLanguagePref != null) {
            appLanguagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                settingsManager.setAppLanguage((String) newValue);
                // Перезапускаем активность для применения изменений языка
                requireActivity().recreate();
                return true;
            });
        }

        // Настройка языка озвучки
        ListPreference ttsLanguagePref = findPreference("tts_language");
        if (ttsLanguagePref != null) {
            ttsLanguagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                settingsManager.setTtsLanguage((String) newValue);
                return true;
            });
        }

        // Настройка темы
        ListPreference themePref = findPreference("theme");
        if (themePref != null) {
            themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                settingsManager.setTheme((String) newValue);
                // Применяем тему немедленно
                if ("dark".equals(newValue)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                return true;
            });
        }

        // Настройка голоса
        ListPreference voicePref = findPreference("voice");
        if (voicePref != null) {
            voicePref.setOnPreferenceChangeListener((preference, newValue) -> {
                settingsManager.setVoice((String) newValue);
                return true;
            });
        }
    }

    private void updateSignInPreference(Preference preference) {
        if (authManager.isSignedIn() && !authManager.isGuest()) {
            preference.setTitle(R.string.sign_out);
            preference.setSummary(authManager.getUserEmail());
        } else {
            preference.setTitle(R.string.google_sign_in);
            preference.setSummary(R.string.google_sign_in_summary);
        }
    }

    private void startSignIn() {
        try {
            Intent signInIntent = authManager.getSignInIntent();
            if (signInIntent != null) {
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } else {
                Toast.makeText(requireContext(), "Ошибка инициализации Google Sign-In", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Ошибка при запуске авторизации", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            authManager.handleSignInResult(task);
            updateUI();
        }
    }

    private void updateUI() {
        Preference signInPref = findPreference("google_sign_in");
        if (signInPref != null) {
            updateSignInPreference(signInPref);
        }
    }
} 