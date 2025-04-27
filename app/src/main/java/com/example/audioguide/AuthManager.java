package com.example.audioguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class AuthManager {
    private static final String TAG = "AuthManager";
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_GUEST = "is_guest";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    
    private final Context context;
    private final SharedPreferences prefs;
    private final GoogleSignInClient googleSignInClient;
    private final SettingsManager settingsManager;

    public AuthManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.settingsManager = SettingsManager.getInstance(context);
        
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        
        this.googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public boolean isFirstLaunch() {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunchComplete() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    public Intent getSignInIntent() {
        if (googleSignInClient == null) {
            Log.e(TAG, "GoogleSignInClient is null");
            return null;
        }
        return googleSignInClient.getSignInIntent();
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                saveUserData(account);
                settingsManager.setGuestMode(false);
                setFirstLaunchComplete();
                Log.i(TAG, "Sign in successful: " + account.getEmail());
            }
        } catch (ApiException e) {
            Log.e(TAG, "Sign in failed: " + e.getStatusCode());
            switch (e.getStatusCode()) {
                case 12501:
                    Log.e(TAG, "Sign in canceled by user");
                    break;
                case 7:
                    Log.e(TAG, "Network error during sign in");
                    break;
                case 4:
                    Log.e(TAG, "Sign in currently in progress");
                    break;
                default:
                    Log.e(TAG, "Unknown sign in error: " + e.getMessage());
            }
        }
    }

    private void saveUserData(GoogleSignInAccount account) {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_USER_ID, account.getId());
            editor.putString(KEY_USER_NAME, account.getDisplayName());
            editor.putString(KEY_USER_EMAIL, account.getEmail());
            editor.putBoolean(KEY_IS_GUEST, false);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Error saving user data: " + e.getMessage());
        }
    }

    public void enableGuestMode() {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_IS_GUEST, true);
            editor.apply();
            settingsManager.setGuestMode(true);
            setFirstLaunchComplete();
        } catch (Exception e) {
            Log.e(TAG, "Error enabling guest mode: " + e.getMessage());
        }
    }

    public void signOut() {
        try {
            googleSignInClient.signOut();
            clearUserData();
        } catch (Exception e) {
            Log.e(TAG, "Error signing out: " + e.getMessage());
        }
    }

    private void clearUserData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_IS_GUEST);
        editor.apply();
    }

    public boolean isSignedIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        return account != null;
    }

    public boolean isGuest() {
        return prefs.getBoolean(KEY_IS_GUEST, false);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void setGuestMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_IS_GUEST, enabled).apply();
    }
} 