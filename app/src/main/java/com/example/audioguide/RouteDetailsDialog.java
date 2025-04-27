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
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.List;
import java.util.ArrayList;

public class RouteDetailsDialog extends DialogFragment implements OnRouteMapClickListener {
    private static final String TAG = "RouteDetailsDialog";
    private static final String ARG_NAME_RES_ID = "name_res_id";
    private static final String ARG_DESCRIPTION_RES_ID = "description_res_id";
    private static final String ARG_DURATION_RES_ID = "duration_res_id";
    private static final String ARG_DISTANCE_RES_ID = "distance_res_id";
    private static final String ARG_LANDMARKS = "landmarks";
    private static final String ARG_TIPS = "tips";

    private int nameResId;
    private int descriptionResId;
    private int durationResId;
    private int distanceResId;
    private List<String> landmarks;
    private List<String> tips;
    private OnRouteMapClickListener listener;

    public interface OnRouteMapClickListener {
        void onRouteMapClick();
    }

    public static RouteDetailsDialog newInstance(int nameResId, int descriptionResId, int durationResId, int distanceResId, List<String> landmarks, List<String> tips) {
        RouteDetailsDialog dialog = new RouteDetailsDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_NAME_RES_ID, nameResId);
        args.putInt(ARG_DESCRIPTION_RES_ID, descriptionResId);
        args.putInt(ARG_DURATION_RES_ID, durationResId);
        args.putInt(ARG_DISTANCE_RES_ID, distanceResId);
        args.putStringArrayList(ARG_LANDMARKS, new ArrayList<>(landmarks));
        args.putStringArrayList(ARG_TIPS, new ArrayList<>(tips));
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRouteMapClickListener) {
            listener = (OnRouteMapClickListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            Log.e(TAG, "Arguments cannot be null");
            dismiss();
            return;
        }

        nameResId = args.getInt(ARG_NAME_RES_ID);
        descriptionResId = args.getInt(ARG_DESCRIPTION_RES_ID);
        durationResId = args.getInt(ARG_DURATION_RES_ID);
        distanceResId = args.getInt(ARG_DISTANCE_RES_ID);
        landmarks = args.getStringArrayList(ARG_LANDMARKS);
        tips = args.getStringArrayList(ARG_TIPS);

        if (nameResId == 0) {
            Log.e(TAG, "Name resource ID is missing");
            dismiss();
            return;
        }

        if (landmarks == null) {
            landmarks = new ArrayList<>();
        }
        if (tips == null) {
            tips = new ArrayList<>();
        }

        int themeResId = SettingsManager.getInstance(requireContext()).isDarkTheme() ? 
            R.style.DialogThemeDark : R.style.DialogTheme;
        setStyle(DialogFragment.STYLE_NORMAL, themeResId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (nameResId != 0) {
            dialog.setTitle(getString(nameResId));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_route_details, container, false);
        
        TextView titleView = view.findViewById(R.id.route_title);
        TextView descriptionView = view.findViewById(R.id.route_description);
        TextView durationView = view.findViewById(R.id.route_duration);
        TextView distanceView = view.findViewById(R.id.route_distance);
        RecyclerView landmarksRecyclerView = view.findViewById(R.id.landmarksRecyclerView);
        RecyclerView tipsRecyclerView = view.findViewById(R.id.tipsRecyclerView);
        Button showOnMapButton = view.findViewById(R.id.showOnMapButton);

        if (titleView == null || descriptionView == null || durationView == null || 
            distanceView == null || landmarksRecyclerView == null || tipsRecyclerView == null || 
            showOnMapButton == null) {
            Log.e(TAG, "Required views are missing");
            dismiss();
            return view;
        }

        try {
            titleView.setText(getString(nameResId));
            if (descriptionResId != 0) {
                descriptionView.setText(getString(descriptionResId));
                descriptionView.setVisibility(View.VISIBLE);
            } else {
                descriptionView.setVisibility(View.GONE);
            }
            if (durationResId != 0) {
                durationView.setText(getString(durationResId));
                durationView.setVisibility(View.VISIBLE);
            } else {
                durationView.setVisibility(View.GONE);
            }
            if (distanceResId != 0) {
                distanceView.setText(getString(distanceResId));
                distanceView.setVisibility(View.VISIBLE);
            } else {
                distanceView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting text from resources", e);
            dismiss();
            return view;
        }

        List<String> landmarkStrings = new ArrayList<>();
        for (String landmarkId : landmarks) {
            try {
                int resId = getResources().getIdentifier(landmarkId, "string", requireContext().getPackageName());
                if (resId != 0) {
                    landmarkStrings.add(getString(resId));
                } else {
                    Log.w(TAG, "Resource not found for landmark: " + landmarkId);
                    landmarkStrings.add(landmarkId);
                }
            } catch (Exception e) {
                Log.w(TAG, "Error processing landmark: " + landmarkId, e);
                landmarkStrings.add(landmarkId);
            }
        }

        List<String> tipStrings = new ArrayList<>();
        for (String tipId : tips) {
            try {
                int resId = getResources().getIdentifier(tipId, "string", requireContext().getPackageName());
                if (resId != 0) {
                    tipStrings.add(getString(resId));
                } else {
                    Log.w(TAG, "Resource not found for tip: " + tipId);
                    tipStrings.add(tipId);
                }
            } catch (Exception e) {
                Log.w(TAG, "Error processing tip: " + tipId, e);
                tipStrings.add(tipId);
            }
        }

        try {
            Context context = requireContext();
            landmarksRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            landmarksRecyclerView.setAdapter(new SimpleTextAdapter(landmarkStrings));
            landmarksRecyclerView.setVisibility(landmarkStrings.isEmpty() ? View.GONE : View.VISIBLE);

            tipsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            tipsRecyclerView.setAdapter(new SimpleTextAdapter(tipStrings));
            tipsRecyclerView.setVisibility(tipStrings.isEmpty() ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerViews", e);
            dismiss();
            return view;
        }

        showOnMapButton.setOnClickListener(v -> onRouteMapClick());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.w(TAG, "Dialog is null");
            return;
        }
        
        Window window = dialog.getWindow();
        if (window == null) {
            Log.w(TAG, "Window is null");
            return;
        }
        
        window.setLayout(
            (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onRouteMapClick() {
        if (listener != null) {
            listener.onRouteMapClick();
        } else if (getParentFragment() instanceof OnRouteMapClickListener) {
            ((OnRouteMapClickListener) getParentFragment()).onRouteMapClick();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
} 