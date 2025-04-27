package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LandmarksFragment extends Fragment {
    private RecyclerView recyclerView;
    private LandmarksAdapter adapter;
    private List<Landmark> landmarks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landmarks, container, false);
        recyclerView = view.findViewById(R.id.landmarks_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        landmarks = LandmarkData.getLandmarks();
        adapter = new LandmarksAdapter(landmarks, landmark -> {
            // Обработка нажатия на достопримечательность
            LandmarkDetailsDialog dialog = LandmarkDetailsDialog.newInstance(
                landmark.getNameResId(),
                landmark.getShortDescriptionResId(),
                landmark.getFullDescriptionResId(),
                landmark.getImageUrl(),
                landmark.getLatitude(),
                landmark.getLongitude()
            );
            dialog.show(getChildFragmentManager(), "landmark_details");
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
} 