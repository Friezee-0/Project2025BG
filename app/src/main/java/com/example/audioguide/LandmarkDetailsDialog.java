package com.example.audioguide;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class LandmarkDetailsDialog extends DialogFragment {
    private static final String TAG = "LandmarkDetailsDialog";
    private static final String ARG_NAME_RES_ID = "name_res_id";
    private static final String ARG_SHORT_DESCRIPTION_RES_ID = "short_description_res_id";
    private static final String ARG_FULL_DESCRIPTION_RES_ID = "full_description_res_id";
    private static final String ARG_IMAGE_URL = "image_url";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    private int nameResId;
    private int shortDescriptionResId;
    private int fullDescriptionResId;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private OnAudioPlayClickListener listener;

    public interface OnAudioPlayClickListener {
        void onPlayAudio(LandmarkDetailsDialog dialog);
    }

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnAudioPlayClickListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "Context must implement OnAudioPlayClickListener", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeResId = SettingsManager.getInstance(requireContext()).isDarkTheme() ? 
            R.style.DialogThemeDark : R.style.DialogTheme;
        setStyle(DialogFragment.STYLE_NORMAL, themeResId);
        
        try {
            Bundle args = getArguments();
            if (args == null) {
                Log.e(TAG, "Arguments cannot be null");
                dismiss();
                return;
            }
            nameResId = args.getInt(ARG_NAME_RES_ID);
            shortDescriptionResId = args.getInt(ARG_SHORT_DESCRIPTION_RES_ID);
            fullDescriptionResId = args.getInt(ARG_FULL_DESCRIPTION_RES_ID);
            imageUrl = args.getString(ARG_IMAGE_URL);
            latitude = args.getDouble(ARG_LATITUDE);
            longitude = args.getDouble(ARG_LONGITUDE);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing dialog", e);
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        try {
            if (nameResId != 0) {
                dialog.setTitle(getString(nameResId));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting dialog title", e);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_landmark_details, container, false);

        try {
            ImageView imageView = view.findViewById(R.id.landmark_image);
            TextView titleTextView = view.findViewById(R.id.landmark_title);
            TextView shortDescriptionTextView = view.findViewById(R.id.landmark_short_description);
            TextView descriptionTextView = view.findViewById(R.id.landmark_description);
            TextView coordinatesTextView = view.findViewById(R.id.landmark_coordinates);
            Button playAudioButton = view.findViewById(R.id.play_audio_button);

            // Загрузка изображения
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            }

            if (nameResId != 0) {
                titleTextView.setText(getString(nameResId));
            }
            if (shortDescriptionResId != 0) {
                shortDescriptionTextView.setText(getString(shortDescriptionResId));
            }
            if (fullDescriptionResId != 0) {
                descriptionTextView.setText(getString(fullDescriptionResId));
            }
            coordinatesTextView.setText(String.format("%.6f, %.6f", latitude, longitude));

            playAudioButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlayAudio(this);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error initializing dialog view", e);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.getWindow() != null) {
                Window window = dialog.getWindow();
                window.setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );
                window.setGravity(Gravity.CENTER);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting dialog layout", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int themeResId = SettingsManager.getInstance(requireContext()).isDarkTheme() ? 
            R.style.DialogThemeDark : R.style.DialogTheme;
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(
                themeResId == R.style.DialogThemeDark ? 
                R.drawable.dialog_background_dark : 
                R.drawable.dialog_background
            );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
} 