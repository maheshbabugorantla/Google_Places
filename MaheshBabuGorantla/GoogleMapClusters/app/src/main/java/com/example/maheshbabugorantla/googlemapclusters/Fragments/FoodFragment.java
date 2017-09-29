package com.example.maheshbabugorantla.googlemapclusters.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maheshbabugorantla.googlemapclusters.R;

/**
 * DESCRIPTION: FoodFragment class
 *              This Fragment is used to display the food related data from Kai Ping Qwah Work
 * Created by MaheshBabuGorantla
 * First Update On Apr 07, 2017 .
 * Last Update On Apr 07, 2017.
 */

public class FoodFragment extends Fragment {

    private static final String LOG_TAG = "FoodFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_food_data, container, false);

        return rootView;
    }
}