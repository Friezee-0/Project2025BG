package com.example.audioguide;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RouteDetailsDialog extends DialogFragment {
    private static final String ARG_NAME_RES_ID = "name_res_id";
    private static final String ARG_DESCRIPTION_RES_ID = "description_res_id";
    private static final String ARG_LANDMARKS = "landmarks";
    private static final String ARG_START_LAT = "start_lat";
    private static final String ARG_START_LNG = "start_lng";
    private static final String ARG_END_LAT = "end_lat";
    private static final String ARG_END_LNG = "end_lng";

    private int nameResId;
    private int descriptionResId;
    private List<Landmark> landmarks;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;

    public static RouteDetailsDialog newInstance(int nameResId, int descriptionResId,
                                               List<Landmark> landmarks,
                                               double startLatitude, double startLongitude,
                                               double endLatitude, double endLongitude) {
        RouteDetailsDialog dialog = new RouteDetailsDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_NAME_RES_ID, nameResId);
        args.putInt(ARG_DESCRIPTION_RES_ID, descriptionResId);
        args.putSerializable(ARG_LANDMARKS, new java.util.ArrayList<>(landmarks));
        args.putDouble(ARG_START_LAT, startLatitude);
        args.putDouble(ARG_START_LNG, startLongitude);
        args.putDouble(ARG_END_LAT, endLatitude);
        args.putDouble(ARG_END_LNG, endLongitude);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameResId = getArguments().getInt(ARG_NAME_RES_ID);
            descriptionResId = getArguments().getInt(ARG_DESCRIPTION_RES_ID);
            landmarks = (List<Landmark>) getArguments().getSerializable(ARG_LANDMARKS);
            startLatitude = getArguments().getDouble(ARG_START_LAT);
            startLongitude = getArguments().getDouble(ARG_START_LNG);
            endLatitude = getArguments().getDouble(ARG_END_LAT);
            endLongitude = getArguments().getDouble(ARG_END_LNG);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_route_details, container, false);

        TextView titleTextView = view.findViewById(R.id.route_title);
        TextView descriptionTextView = view.findViewById(R.id.route_description);
        RecyclerView landmarksRecyclerView = view.findViewById(R.id.landmarks_recycler_view);
        Button showOnMapButton = view.findViewById(R.id.show_on_map_button);

        titleTextView.setText(getString(nameResId));
        descriptionTextView.setText(getString(descriptionResId));

        landmarksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        landmarksRecyclerView.setAdapter(new LandmarksAdapter(landmarks, landmark -> {
            // Обработка клика по достопримечательности
        }));

        showOnMapButton.setOnClickListener(v -> {
            // Показать маршрут на карте
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
} 