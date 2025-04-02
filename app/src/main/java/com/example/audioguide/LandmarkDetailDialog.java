package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;

public class LandmarkDetailDialog extends DialogFragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_URL = "image_url";

    public static LandmarkDetailDialog newInstance(String title, String description, String imageUrl) {
        LandmarkDetailDialog dialog = new LandmarkDetailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_IMAGE_URL, imageUrl);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_landmark_detail, container, false);

        ImageView imageView = view.findViewById(R.id.landmark_image);
        TextView titleView = view.findViewById(R.id.landmark_title);
        TextView descriptionView = view.findViewById(R.id.landmark_description);

        Bundle args = getArguments();
        if (args != null) {
            String imageUrl = args.getString(ARG_IMAGE_URL);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView);
            }

            titleView.setText(args.getString(ARG_TITLE));
            descriptionView.setText(args.getString(ARG_DESCRIPTION));
        }

        return view;
    }
} 