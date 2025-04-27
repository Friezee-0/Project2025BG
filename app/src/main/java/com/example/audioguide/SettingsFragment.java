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
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int RC_SIGN_IN = 9001;
    private SettingsManager settingsManager;
    private AuthManager authManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        settingsManager = SettingsManager.getInstance(requireContext());
        authManager = new AuthManager(requireContext());

        setupGoogleSignIn();
        setupLanguagePreferences();
        setupThemePreference();
        setupVoicePreference();
        setupAboutSection();
    }

    private void setupGoogleSignIn() {
        Preference signInPref = findPreference("google_sign_in");
        if (signInPref != null) {
            updateSignInPreference(signInPref);
            signInPref.setOnPreferenceClickListener(preference -> {
                if (!authManager.isSignedIn() || authManager.isGuest()) {
                    startSignIn();
                } else {
                    authManager.signOut();
                    updateSignInPreference(signInPref);
                    Toast.makeText(requireContext(), R.string.sign_out_success, Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }
    }

    private void setupLanguagePreferences() {
        ListPreference appLanguagePref = findPreference("app_language");
        if (appLanguagePref != null) {
            appLanguagePref.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            appLanguagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                try {
                    settingsManager.setAppLanguage((String) newValue);
                    // Используем Handler для безопасного пересоздания активности
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (isAdded() && getActivity() != null && !getActivity().isFinishing()) {
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }, 100);
                    return true;
                } catch (Exception e) {
                    Log.e("SettingsFragment", "Error changing language", e);
                    Toast.makeText(requireContext(), "Ошибка при смене языка", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        ListPreference ttsLanguagePref = findPreference("tts_language");
        if (ttsLanguagePref != null) {
            ttsLanguagePref.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            ttsLanguagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                try {
                    settingsManager.setTtsLanguage((String) newValue);
                    return true;
                } catch (Exception e) {
                    Log.e("SettingsFragment", "Error changing TTS language", e);
                    Toast.makeText(requireContext(), "Ошибка при смене языка голоса", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    private void setupThemePreference() {
        ListPreference themePref = findPreference("theme");
        if (themePref != null) {
            themePref.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                settingsManager.setTheme((String) newValue);
                if ("dark".equals(newValue)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (isAdded() && getActivity() != null) {
                        getActivity().recreate();
                    }
                });
                return true;
            });
        }
    }

    private void setupVoicePreference() {
        ListPreference voicePref = findPreference("voice");
        if (voicePref != null) {
            voicePref.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            voicePref.setOnPreferenceChangeListener((preference, newValue) -> {
                settingsManager.setVoice((String) newValue);
                return true;
            });
        }
    }

    private void setupAboutSection() {
        Preference versionPref = findPreference("app_version");
        if (versionPref != null) {
            try {
                String versionName = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0).versionName;
                versionPref.setSummary(versionName);
            } catch (Exception e) {
                Log.e("SettingsFragment", "Error getting version info", e);
            }
        }

        Preference privacyPref = findPreference("privacy_policy");
        if (privacyPref != null) {
            privacyPref.setOnPreferenceClickListener(preference -> {
                // TODO: Открыть политику конфиденциальности
                Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("tts_language")) {
            String language = sharedPreferences.getString(key, "ru");
            settingsManager.setTtsLanguage(language);
        } else if (key.equals("theme")) {
            String theme = sharedPreferences.getString(key, "light");
            settingsManager.setTheme(theme);
            requireActivity().recreate();
        } else if (key.equals("voice")) {
            String voice = sharedPreferences.getString(key, "default");
            settingsManager.setVoice(voice);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settingsManager.shutdown();
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
                Toast.makeText(requireContext(), R.string.sign_in_init_error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.sign_in_processing_error, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsManager = SettingsManager.getInstance(requireContext());
    }
} 