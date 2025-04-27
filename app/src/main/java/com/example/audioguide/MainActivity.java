package com.example.audioguide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnRouteMapClickListener {
    private static final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    
    private NavController navController;
    private AuthManager authManager;
    private SettingsManager settingsManager;
    private AudioGuideManager audioGuideManager;
    private AppBarConfiguration appBarConfiguration;
    private Route currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsManager = SettingsManager.getInstance(this);
        String language = settingsManager.getAppLanguage();
        updateLocale(language);
        
        if ("dark".equals(settingsManager.getTheme())) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        
        authManager = new AuthManager(this);
        
        // Проверяем авторизацию
        if (!authManager.isSignedIn() && !authManager.isGuest()) {
            startLoginActivity();
            return;
        }
        
        setContentView(R.layout.activity_main);
        
        audioGuideManager = new AudioGuideManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                NavigationUI.setupWithNavController(bottomNav, navController);

                appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_map,
                    R.id.navigation_landmarks,
                    R.id.navigation_routes,
                    R.id.navigation_settings
                ).build();
                
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            } else {
                Log.e(TAG, "NavHostFragment not found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up navigation: " + e.getMessage());
        }

        checkLocationPermission();
    }

    private void updateLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        
        Context context = createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        
        // Обновляем конфигурацию базового контекста
        getBaseContext().getResources().updateConfiguration(config, 
            getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentLanguage = settingsManager.getAppLanguage();
        if (!currentLanguage.equals(getResources().getConfiguration().getLocales().get(0).getLanguage())) {
            updateLocale(currentLanguage);
            recreate();
        }
    }

    private void recreateFragments() {
        try {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            
            if (navHostFragment != null) {
                // Получаем текущий фрагмент
                int currentDestination = navController.getCurrentDestination().getId();
                
                // Пересоздаем NavHostFragment
                getSupportFragmentManager().beginTransaction()
                        .detach(navHostFragment)
                        .attach(navHostFragment)
                        .commit();
                
                // Возвращаемся на текущий фрагмент
                navController.navigate(currentDestination);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error recreating fragments: " + e.getMessage());
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, 
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST);
        } else {
            startAudioGuide();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startAudioGuide();
            }
        }
    }

    private void startAudioGuide() {
        audioGuideManager.startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioGuideManager != null) {
            audioGuideManager.shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_sign_out).setVisible(!authManager.isGuest() && authManager.isSignedIn());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            authManager.signOut();
            startLoginActivity();
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRouteMapClick() {
        if (currentRoute != null) {
            showRouteOnMap(currentRoute);
        }
    }

    public void showRouteOnMap(Route route) {
        currentRoute = route;
        if (route != null) {
            // TODO: Implement map navigation
            Toast.makeText(this, R.string.map_navigation_coming_soon, Toast.LENGTH_SHORT).show();
        }
    }
} 