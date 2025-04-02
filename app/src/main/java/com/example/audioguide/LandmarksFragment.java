package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LandmarksFragment extends Fragment implements LandmarksAdapter.OnLandmarkClickListener {
    private RecyclerView recyclerView;
    private LandmarksAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landmarks, container, false);
        recyclerView = view.findViewById(R.id.landmarks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<Landmark> landmarks = LandmarkData.getLandmarks();
        adapter = new LandmarksAdapter(landmarks, this);
        recyclerView.setAdapter(adapter);
        
        return view;
    }

    @Override
    public void onLandmarkClick(Landmark landmark) {
        LandmarkDetailDialog dialog = LandmarkDetailDialog.newInstance(
            landmark.getName(),
            landmark.getFullDescription(),
            landmark.getImageUrl()
        );
        dialog.show(getParentFragmentManager(), "landmark_detail");
    }
} 