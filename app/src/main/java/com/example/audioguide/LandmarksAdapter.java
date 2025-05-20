package com.example.audioguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LandmarksAdapter extends RecyclerView.Adapter<LandmarksAdapter.ViewHolder> {
    private List<Landmark> landmarks;
    private OnLandmarkClickListener listener;
    private Context context;

    public interface OnLandmarkClickListener {
        void onLandmarkClick(Landmark landmark);
    }

    public LandmarksAdapter(Context context, List<Landmark> landmarks, OnLandmarkClickListener listener) {
        this.context = context;
        this.landmarks = landmarks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_landmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Landmark landmark = landmarks.get(position);
        holder.titleTextView.setText(context.getString(landmark.getNameResId()));
        holder.descriptionTextView.setText(context.getString(landmark.getShortDescriptionResId()));
        
        if (landmark.getImageResId() != 0) {
            holder.imageView.setImageResource(landmark.getImageResId());
            holder.imageView.setContentDescription(context.getString(R.string.landmark_image_description));
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
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.landmark_name);
            descriptionTextView = view.findViewById(R.id.landmark_description);
            imageView = view.findViewById(R.id.landmark_image);
        }
    }
} 