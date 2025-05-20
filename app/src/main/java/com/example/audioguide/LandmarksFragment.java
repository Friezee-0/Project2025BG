package com.example.audioguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LandmarksFragment extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private List<String> titles;
    private static final String TAG = "LandmarksFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landmarks, container, false);
        
        try {
            viewPager = view.findViewById(R.id.viewPager);
            tabLayout = view.findViewById(R.id.tabLayout);
            
            if (viewPager == null || tabLayout == null) {
                Log.e(TAG, "ViewPager or TabLayout not found");
                return view;
            }
            
            initializeFragments();
            
            FragmentAdapter adapter = new FragmentAdapter(requireActivity(), fragments, titles);
            viewPager.setAdapter(adapter);
            
            new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles.get(position))
            ).attach();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing LandmarksFragment: " + e.getMessage());
        }
        
        return view;
    }

    private void initializeFragments() {
        try {
            fragments = new ArrayList<>();
            titles = new ArrayList<>();
            
            // Исторический центр
            List<String> historicalCenter = Arrays.asList("red_square", "saint_basil", "kremlin");
            fragments.add(LandmarkListFragment.newInstance(historicalCenter));
            titles.add(getString(R.string.historical_center));
            
            // Культурные места
            List<String> culturalPlaces = Arrays.asList("tretyakov", "bolshoi");
            fragments.add(LandmarkListFragment.newInstance(culturalPlaces));
            titles.add(getString(R.string.cultural_places));
            
            // Красная площадь
            List<String> redSquare = Arrays.asList("gum", "lenin_mausoleum", "alexander_garden");
            fragments.add(LandmarkListFragment.newInstance(redSquare));
            titles.add(getString(R.string.red_square));
            
            // Музеи
            List<String> museums = Arrays.asList("manege", "historical_museum", "kazan_cathedral");
            fragments.add(LandmarkListFragment.newInstance(museums));
            titles.add(getString(R.string.museums));
        } catch (Exception e) {
            Log.e(TAG, "Error initializing fragments: " + e.getMessage());
        }
    }
} 