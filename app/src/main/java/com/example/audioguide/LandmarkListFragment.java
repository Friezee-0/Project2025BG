package com.example.audioguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LandmarkListFragment extends Fragment implements LandmarksAdapter.OnLandmarkClickListener {
    private static final String ARG_LANDMARK_IDS = "landmark_ids";
    private static final String TAG = "LandmarkListFragment";
    private List<String> landmarkIds;
    private RecyclerView recyclerView;
    private LandmarksAdapter adapter;

    public static LandmarkListFragment newInstance(List<String> landmarkIds) {
        LandmarkListFragment fragment = new LandmarkListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_LANDMARK_IDS, new ArrayList<>(landmarkIds));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            landmarkIds = getArguments().getStringArrayList(ARG_LANDMARK_IDS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landmark_list, container, false);
        
        try {
            recyclerView = view.findViewById(R.id.landmarks_recycler_view);
            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                
                List<Landmark> landmarks = new ArrayList<>();
                if (landmarkIds != null) {
                    for (String id : landmarkIds) {
                        Landmark landmark = LandmarkData.getLandmarkById(id);
                        if (landmark != null) {
                            landmarks.add(landmark);
                        }
                    }
                }
                
                adapter = new LandmarksAdapter(requireContext(), landmarks, this);
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing LandmarkListFragment: " + e.getMessage());
        }
        
        return view;
    }

    @Override
    public void onLandmarkClick(Landmark landmark) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showLandmarkDetails(landmark);
        }
    }
} 