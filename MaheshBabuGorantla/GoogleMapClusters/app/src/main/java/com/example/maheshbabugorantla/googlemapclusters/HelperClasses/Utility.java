package com.example.maheshbabugorantla.googlemapclusters.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.example.maheshbabugorantla.googlemapclusters.R;

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
        String userHeight = sharedPreferences.getString(context.getString(R.string.pref_height_key),"");

        if(getHeightUnits().equals("meters")) {
            return Float.parseFloat(userHeight);
        }
        else {
            return convertFootToMeters(userHeight);
        }
    }

    // This method always returns the user's weight in kgs irrespective of the users preference of
    // units for weight
    public float getUserWeight(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userWeight = sharedPreferences.getString(context.getString(R.string.pref_weight_key), "");

        if(getWeightUnits().equals("kgs")) {
            return Float.parseFloat(userWeight);
        }
        else {
            return convertPoundsToKgs(userWeight);
        }
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

    /**
     * This method is used to determine if the GPS Location Services are enabled on the device
     * */
    public boolean isLocationEnabled(Context context) {

        int locationStatus = 0; // false

        String locProviders;

        // If the Device OS is either Kitkat or anything after KitKat
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationStatus = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            }
            catch(Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationStatus != Settings.Secure.LOCATION_MODE_OFF;
         }
        else {
            locProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locProviders);
        }
    }
}
