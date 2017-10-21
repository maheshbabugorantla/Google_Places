package com.example.maheshbabugorantla.google_places.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.maheshbabugorantla.google_places.R;

public class CameraScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.camera_fragment, container, false);

        TextView CameraText = (TextView) rootView.findViewById(R.id.camera);
        CameraText.setText("CAMERA FRAGMENT");

        return rootView;
    }
}
