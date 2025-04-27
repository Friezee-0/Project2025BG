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
    private static final String ARG_NAME_RES_ID = "name_res_id";
    private static final String ARG_SHORT_DESCRIPTION_RES_ID = "short_description_res_id";
    private static final String ARG_FULL_DESCRIPTION_RES_ID = "full_description_res_id";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_DISTANCE = "distance";
    private static final String ARG_LANDMARKS = "landmarks";
    private static final String ARG_TIPS = "tips";

    public static RouteDetailDialog newInstance(int nameResId, int shortDescriptionResId,
                                              int fullDescriptionResId, String duration,
                                              String distance, String landmarks, String tips) {
        RouteDetailDialog dialog = new RouteDetailDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_NAME_RES_ID, nameResId);
        args.putInt(ARG_SHORT_DESCRIPTION_RES_ID, shortDescriptionResId);
        args.putInt(ARG_FULL_DESCRIPTION_RES_ID, fullDescriptionResId);
        args.putString(ARG_DURATION, duration);
        args.putString(ARG_DISTANCE, distance);
        args.putString(ARG_LANDMARKS, landmarks);
        args.putString(ARG_TIPS, tips);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_route_detail, container, false);

        TextView titleView = view.findViewById(R.id.route_title);
        TextView shortDescriptionView = view.findViewById(R.id.route_short_description);
        TextView fullDescriptionView = view.findViewById(R.id.route_full_description);
        TextView landmarksView = view.findViewById(R.id.route_landmarks);
        TextView durationView = view.findViewById(R.id.route_duration);
        TextView distanceView = view.findViewById(R.id.route_distance);
        TextView tipsView = view.findViewById(R.id.route_tips);

        Bundle args = getArguments();
        if (args != null) {
            titleView.setText(args.getInt(ARG_NAME_RES_ID));
            shortDescriptionView.setText(args.getInt(ARG_SHORT_DESCRIPTION_RES_ID));
            fullDescriptionView.setText(args.getInt(ARG_FULL_DESCRIPTION_RES_ID));
            landmarksView.setText(args.getString(ARG_LANDMARKS));
            durationView.setText(getString(R.string.duration_label) + " " + args.getString(ARG_DURATION));
            distanceView.setText(getString(R.string.distance_label) + " " + args.getString(ARG_DISTANCE));
            tipsView.setText(args.getString(ARG_TIPS));
        }

        return view;
    }
} 