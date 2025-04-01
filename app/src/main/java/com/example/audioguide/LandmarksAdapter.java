package com.example.audioguide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class LandmarksAdapter extends RecyclerView.Adapter<LandmarksAdapter.ViewHolder> {
    private final List<Landmark> landmarks;
    private final OnLandmarkClickListener listener;

    public interface OnLandmarkClickListener {
        void onLandmarkClick(Landmark landmark);
    }

    public LandmarksAdapter(List<Landmark> landmarks, OnLandmarkClickListener listener) {
        this.landmarks = landmarks;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_landmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Landmark landmark = landmarks.get(position);
        holder.nameTextView.setText(landmark.getName());
        holder.descriptionTextView.setText(landmark.getDescription());
        
        if (landmark.getImageUrl() != null && !landmark.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(landmark.getImageUrl())
                .centerCrop()
                .into(holder.imageView);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLandmarkClick(landmark);
            }
        });
    }

    @Override
    public int getItemCount() {
        return landmarks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.landmark_title);
            descriptionTextView = view.findViewById(R.id.landmark_description);
            imageView = view.findViewById(R.id.landmark_image);
        }
    }
} 