package com.example.audioguide;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;

public class LandmarkDetailDialog extends DialogFragment {
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_URL = "image_url";

    private String name;
    private String description;
    private String imageUrl;

    public static LandmarkDetailDialog newInstance(String name, String description, String imageUrl) {
        LandmarkDetailDialog fragment = new LandmarkDetailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            description = getArguments().getString(ARG_DESCRIPTION);
            imageUrl = getArguments().getString(ARG_IMAGE_URL);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_landmark_detail, null);

        TextView nameTextView = view.findViewById(R.id.landmark_name);
        TextView descriptionTextView = view.findViewById(R.id.landmark_description);
        ImageView imageView = view.findViewById(R.id.landmark_image);

        nameTextView.setText(name);
        descriptionTextView.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .into(imageView);
        }

        builder.setView(view)
            .setPositiveButton(R.string.ok, (dialog, id) -> dismiss());

        return builder.create();
    }
} 