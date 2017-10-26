package com.example.maheshbabugorantla.google_places.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.Math;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.maheshbabugorantla.google_places.R;

/**
 * DESCRIPTION: Utility class
 *              This is just a Utility class used to get the user's preferences in the Settings
 * Created by MaheshBabuGorantla
 * First Update On Apr 02, 2017 .
 * Last Update On Apr 02, 2017.
 */

public class Utility {

    // This is used to get the max Steps count set by the user
    public int getMaxStepsCount(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String stepsCount = sharedPreferences.getString(context.getString(R.string.pref_steps_goal_key), context.getString(R.string.pref_default_steps_goal));
        return Integer.parseInt(stepsCount);
    }

    public int getUserAge(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userAge = sharedPreferences.getString(context.getString(R.string.pref_age_key), "21");

        return Integer.parseInt(userAge);
    }

    // This method always returns the user's height in meters irrespective of the users preference of
    // units for height
    public float getUserHeight(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userHeight = sharedPreferences.getString(context.getString(R.string.pref_height_key), "");

        if (getHeightUnits().equals("meters")) {
            return Float.parseFloat(userHeight);
        } else {
            return convertFootToMeters(userHeight);
        }
    }

    // This method is used to get the user chosen radius to search for restaurants
    // Units: Miles (1 - 10 miles)
    public String getSearchRadius(Context context) { // The radius is converted to meters
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // By Default 1 mile is chosen as the search radius
        int searchRadius = sharedPreferences.getInt(context.getString(R.string.pref_restaurant_radius_key), 1);

        System.out.println(searchRadius);
        searchRadius = searchRadius * 1600; // Miles to meters
        return Integer.toString(searchRadius); // Returning the search radius in meters.
    }

    // This method always returns the user's weight in kgs irrespective of the users preference of
    // units for weight
    public float getUserWeight(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userWeight = sharedPreferences.getString(context.getString(R.string.pref_weight_key), "");

        if (getWeightUnits().equals("kgs")) {
            return Float.parseFloat(userWeight);
        } else {
            return convertPoundsToKgs(userWeight);
        }
    }

    public double convertDegreestoRadians(double degree_coordinate) {

        return (Math.PI * degree_coordinate) / 180;
    }

    public float getDistanceBetween(double lat1, double lng1, double lat2, double lng2) {

        double dLat = convertDegreestoRadians(lat2 - lat1);
        double dLng = convertDegreestoRadians(lng2 - lng1);

        lat1 = convertDegreestoRadians(lat1);
        lat2 = convertDegreestoRadians(lat2);

        double val_1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);

        val_1 = 2 * Math.atan2(Math.sqrt(val_1), Math.sqrt(1 - val_1));

        return (float)(6371 * val_1);
    }

    // This is used to get the user search criteria
    public boolean getSortChoice(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean getSortChoice = sharedPreferences.getBoolean("search_by_radius", false);
        Log.d(Utility.class.getSimpleName(), Boolean.toString(getSortChoice));

        return getSortChoice;
    }

    // This used to get the user preferred units for the Height
    private String getWeightUnits() {
        return "Kgs";
    }

    // This used to get the user preferred units for the Height
    private String getHeightUnits() {
        return "meters";
    }

    private float convertFootToMeters(String userHeight) {
        return (float) (Float.parseFloat(userHeight) * 0.305);
    }

    private float convertPoundsToKgs(String userWeight) {
        return (float) (Float.parseFloat(userWeight) * 0.453592);
    }
}
