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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.audioguide.service.TTSService;

public class LandmarkDetailsDialog extends DialogFragment {
    private static final String TAG = "LandmarkDetailsDialog";
    private static final String ARG_LANDMARK_ID = "landmark_id";
    private Landmark landmark;
    private TTSService ttsService;
    private boolean isPlaying = false;

    public static LandmarkDetailsDialog newInstance(String landmarkId) {
        LandmarkDetailsDialog dialog = new LandmarkDetailsDialog();
        Bundle args = new Bundle();
        args.putString(ARG_LANDMARK_ID, landmarkId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle args = getArguments();
            if (args == null) {
                Log.e(TAG, "Arguments cannot be null");
                dismiss();
                return;
            }

            String landmarkId = args.getString(ARG_LANDMARK_ID);
            if (landmarkId == null) {
                Log.e(TAG, "Landmark ID is null");
                dismiss();
                return;
            }

            landmark = LandmarkData.getLandmarkById(landmarkId);
            if (landmark == null) {
                Log.e(TAG, "Landmark not found: " + landmarkId);
                dismiss();
                return;
            }

            ttsService = TTSService.getInstance(requireContext());
            int themeResId = SettingsManager.getInstance(requireContext()).isDarkTheme() ? 
                R.style.DialogThemeDark : R.style.DialogTheme;
            setStyle(DialogFragment.STYLE_NORMAL, themeResId);
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        try {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            if (landmark != null) {
                dialog.setTitle(getString(landmark.getNameResId()));
            }
            return dialog;
        } catch (Exception e) {
            Log.e(TAG, "Error creating dialog: " + e.getMessage());
            return super.onCreateDialog(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.dialog_landmark_details, container, false);
            
            if (landmark == null) {
                Log.e(TAG, "Landmark is null");
                dismiss();
                return view;
            }

            TextView titleView = view.findViewById(R.id.landmark_title);
            TextView descriptionView = view.findViewById(R.id.landmark_description);
            ImageView imageView = view.findViewById(R.id.landmark_image);
            Button showOnMapButton = view.findViewById(R.id.show_on_map_button);
            Button playAudioButton = view.findViewById(R.id.play_audio_button);

            if (titleView == null || descriptionView == null || imageView == null || 
                showOnMapButton == null || playAudioButton == null) {
                Log.e(TAG, "Required views are missing");
                dismiss();
                return view;
            }

            try {
                titleView.setText(getString(landmark.getNameResId()));
                descriptionView.setText(getString(landmark.getFullDescriptionResId()));

                if (landmark.getImageResId() != 0) {
                    imageView.setImageResource(landmark.getImageResId());
                    imageView.setContentDescription(getString(R.string.landmark_image_description));
                }

                showOnMapButton.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).showLandmarkOnMap(landmark);
                    }
                    dismiss();
                });

                // Настраиваем кнопку воспроизведения аудио
                playAudioButton.setOnClickListener(v -> {
                    try {
                        if (!isPlaying) {
                            String description = getString(landmark.getFullDescriptionResId());
                            if (description != null && !description.isEmpty()) {
                                ttsService.addLandmarkText(landmark.getId(), description);
                                ttsService.speakLandmark(landmark.getId());
                                playAudioButton.setText(R.string.route_stop_guide);
                                isPlaying = true;
                                
                                // Добавляем проверку состояния воспроизведения
                                new android.os.Handler().postDelayed(() -> {
                                    if (!ttsService.isSpeaking()) {
                                        playAudioButton.setText(R.string.play_audio);
                                        isPlaying = false;
                                    }
                                }, 1000);
                            } else {
                                Log.e(TAG, "Empty description for landmark: " + landmark.getId());
                                Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ttsService.stopSpeaking();
                            playAudioButton.setText(R.string.play_audio);
                            isPlaying = false;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error playing audio: " + e.getMessage());
                        Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                        isPlaying = false;
                        playAudioButton.setText(R.string.play_audio);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error setting up views: " + e.getMessage());
                dismiss();
                return view;
            }

            return view;
        } catch (Exception e) {
            Log.e(TAG, "Error creating view: " + e.getMessage());
            return super.onCreateView(inflater, container, savedInstanceState);
        }
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
    public void onPause() {
        super.onPause();
        if (ttsService != null) {
            ttsService.stopSpeaking();
            isPlaying = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ttsService != null && landmark != null) {
            ttsService.removeLandmarkText(landmark.getId());
        }
    }
} 