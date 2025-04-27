package com.example.audioguide;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;

public class LandmarkDetailsDialog extends DialogFragment {
    private static final String ARG_NAME_RES_ID = "name_res_id";
    private static final String ARG_SHORT_DESCRIPTION_RES_ID = "short_description_res_id";
    private static final String ARG_FULL_DESCRIPTION_RES_ID = "full_description_res_id";
    private static final String ARG_IMAGE_URL = "image_url";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    public static LandmarkDetailsDialog newInstance(int nameResId, int shortDescriptionResId,
                                                  int fullDescriptionResId, String imageUrl,
                                                  double latitude, double longitude) {
        LandmarkDetailsDialog dialog = new LandmarkDetailsDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_NAME_RES_ID, nameResId);
        args.putInt(ARG_SHORT_DESCRIPTION_RES_ID, shortDescriptionResId);
        args.putInt(ARG_FULL_DESCRIPTION_RES_ID, fullDescriptionResId);
        args.putString(ARG_IMAGE_URL, imageUrl);
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Arguments cannot be null");
        }

        int nameResId = args.getInt(ARG_NAME_RES_ID);
        int shortDescriptionResId = args.getInt(ARG_SHORT_DESCRIPTION_RES_ID);
        int fullDescriptionResId = args.getInt(ARG_FULL_DESCRIPTION_RES_ID);
        String imageUrl = args.getString(ARG_IMAGE_URL);
        double latitude = args.getDouble(ARG_LATITUDE);
        double longitude = args.getDouble(ARG_LONGITUDE);

        View view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_landmark_details, null);

        TextView nameTextView = view.findViewById(R.id.landmark_name);
        TextView shortDescriptionTextView = view.findViewById(R.id.landmark_short_description);
        TextView fullDescriptionTextView = view.findViewById(R.id.landmark_full_description);
        ImageView imageView = view.findViewById(R.id.landmark_image);

        nameTextView.setText(nameResId);
        shortDescriptionTextView.setText(shortDescriptionResId);
        fullDescriptionTextView.setText(fullDescriptionResId);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);
        }

        return new AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton(R.string.ok, null)
            .setNeutralButton(R.string.show_on_map, (dialog, which) -> {
                // TODO: Показать на карте
            })
            .create();
    }
} 