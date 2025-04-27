package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class LandmarksFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_landmarks, container, false);
        setupViewPager();
        return view;
    }

    private void setupViewPager() {
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LandmarkListFragment("red_square", "saint_basil", "kremlin"));
        fragments.add(new LandmarkListFragment("tretyakov", "bolshoi"));
        fragments.add(new LandmarkListFragment("gum", "lenin_mausoleum", "alexander_garden"));
        fragments.add(new LandmarkListFragment("manege", "historical_museum", "kazan_cathedral"));

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.historical_center));
        titles.add(getString(R.string.cultural_places));
        titles.add(getString(R.string.red_square_area));
        titles.add(getString(R.string.museums));

        FragmentAdapter adapter = new FragmentAdapter(requireActivity(), fragments, titles);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> tab.setText(titles.get(position))
        ).attach();
    }
} 