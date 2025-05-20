package com.example.audioguide;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class FragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> fragments;
    private List<String> titles;

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, 
                          List<Fragment> fragments, 
                          List<String> titles) {
        super(fragmentActivity);
        this.fragments = fragments;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public String getPageTitle(int position) {
        return titles.get(position);
    }
} 