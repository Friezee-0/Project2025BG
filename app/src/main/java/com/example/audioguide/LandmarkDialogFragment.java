import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.audioguide.R;
import com.example.audioguide.service.TTSService;

public class LandmarkDialogFragment extends DialogFragment {
    private static final String ARG_LANDMARK_ID = "landmark_id";
    private static final String ARG_LANDMARK_NAME = "landmark_name";
    private static final String ARG_LANDMARK_DESCRIPTION = "landmark_description";
    private static final String ARG_LANDMARK_IMAGE = "landmark_image";
    
    private TTSService ttsService;
    private String currentLandmarkId;
    private boolean isPlaying = false;

    public static LandmarkDialogFragment newInstance(String landmarkId, String name, String description, int imageResId) {
        LandmarkDialogFragment fragment = new LandmarkDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LANDMARK_ID, landmarkId);
        args.putString(ARG_LANDMARK_NAME, name);
        args.putString(ARG_LANDMARK_DESCRIPTION, description);
        args.putInt(ARG_LANDMARK_IMAGE, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsService = TTSService.getInstance(requireContext());
        if (getArguments() != null) {
            currentLandmarkId = getArguments().getString(ARG_LANDMARK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_landmark, container, false);
        
        // ... existing view initialization code ...

        Button playButton = view.findViewById(R.id.play_audio_button);
        playButton.setOnClickListener(v -> {
            if (!isPlaying) {
                String description = getArguments().getString(ARG_LANDMARK_DESCRIPTION);
                ttsService.addLandmarkText(currentLandmarkId, description);
                ttsService.speakLandmark(currentLandmarkId);
                playButton.setText(R.string.route_stop_guide);
                isPlaying = true;
            } else {
                ttsService.stopSpeaking();
                playButton.setText(R.string.play_audio);
                isPlaying = false;
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        ttsService.stopSpeaking();
        isPlaying = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsService.removeLandmarkText(currentLandmarkId);
    }
} 