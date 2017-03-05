package com.example.maheshbabugorantla.stepscounter.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.maheshbabugorantla.stepscounter.R;

/**
 * Created by MaheshBabuGorantla on 2/10/2017.
 */

public class Utility {

    // This is used to get the max Steps count set by the user
    public int getMaxStepsCount(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String stepsCount = sharedPreferences.getString(context.getString(R.string.pref_steps_goal_key), context.getString(R.string.pref_default_steps_goal));
        return Integer.parseInt(stepsCount);
    }

    // This is used to Google Location Services Settings by the user
    public boolean locationServicesEnabled(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.pref_google_location_services_key), true);
    }
}