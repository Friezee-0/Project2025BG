package com.example.audioguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);

        if (authManager.isSignedIn()) {
            startMainActivity();
            return;
        }

        SignInButton signInButton = findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(v -> startSignIn());

        Button skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(v -> {
            authManager.setGuestMode();
            Toast.makeText(this, "Продолжаем без авторизации", Toast.LENGTH_SHORT).show();
            startMainActivity();
        });
    }

    private void startSignIn() {
        try {
            Intent signInIntent = authManager.getSignInIntent();
            if (signInIntent != null) {
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } else {
                Toast.makeText(this, "Ошибка инициализации Google Sign-In", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error starting sign in: " + e.getMessage());
            Toast.makeText(this, "Ошибка при запуске авторизации", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                if (resultCode == RESULT_OK && data != null) {
                    if (authManager.handleSignInResult(data)) {
                        Toast.makeText(this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                        startMainActivity();
                    } else {
                        Toast.makeText(this, "Ошибка входа: проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "Sign in cancelled by user: resultCode=" + resultCode);
                    Toast.makeText(this, "Авторизация отменена", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in onActivityResult: " + e.getMessage());
                Toast.makeText(this, "Произошла ошибка при обработке входа", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startMainActivity() {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error starting MainActivity: " + e.getMessage());
            Toast.makeText(this, "Ошибка при запуске приложения", Toast.LENGTH_SHORT).show();
        }
    }
} 