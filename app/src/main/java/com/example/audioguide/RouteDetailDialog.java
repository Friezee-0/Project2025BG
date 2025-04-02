package com.example.audioguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RouteDetailDialog extends DialogFragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_LANDMARKS = "landmarks";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_DISTANCE = "distance";
    private static final String ARG_TIPS = "tips";

    public static RouteDetailDialog newInstance(String title, String description, String landmarks,
                                              String duration, String distance, String tips) {
        RouteDetailDialog dialog = new RouteDetailDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_LANDMARKS, landmarks);
        args.putString(ARG_DURATION, duration);
        args.putString(ARG_DISTANCE, distance);
        args.putString(ARG_TIPS, tips);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_route_detail, container, false);

        TextView titleView = view.findViewById(R.id.route_title);
        TextView descriptionView = view.findViewById(R.id.route_description);
        TextView landmarksView = view.findViewById(R.id.route_landmarks);
        TextView durationView = view.findViewById(R.id.route_duration);
        TextView distanceView = view.findViewById(R.id.route_distance);
        TextView tipsView = view.findViewById(R.id.route_tips);

        Bundle args = getArguments();
        if (args != null) {
            titleView.setText(args.getString(ARG_TITLE));
            descriptionView.setText(args.getString(ARG_DESCRIPTION));
            landmarksView.setText(args.getString(ARG_LANDMARKS));
            durationView.setText("Продолжительность: " + args.getString(ARG_DURATION));
            distanceView.setText("Протяженность: " + args.getString(ARG_DISTANCE));
            tipsView.setText(args.getString(ARG_TIPS));
        }
        return view;
    }
} 