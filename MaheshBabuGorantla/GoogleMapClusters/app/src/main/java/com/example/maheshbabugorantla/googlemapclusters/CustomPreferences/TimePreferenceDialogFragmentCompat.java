package com.example.maheshbabugorantla.googlemapclusters.CustomPreferences;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import com.example.maheshbabugorantla.googlemapclusters.R;

/**
 * DESCRIPTION: TimePreferenceDialogFragmentCompat class
 * Created by MaheshBabuGorantla
 * First Update On Apr 02, 2017 .
 * Last Update On Apr 02, 2017.
 */

public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    /**
     * TimePicker Widget
     */
    private TimePicker mTimePicker;

    public static TimePreferenceDialogFragmentCompat newInstance(String key) {

        final TimePreferenceDialogFragmentCompat fragment = new TimePreferenceDialogFragmentCompat();

        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Binds views in the content View of the dialog to data
     *
     * @param view: The content view of the dialog, if it is custom
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = (TimePicker) view.findViewById(R.id.edit);

        // Exception: When there is no TimePicker with the id 'edit' in the dialog
        if (mTimePicker == null) {
            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'edit'");
        }

        // Get the time from the related Preference
        Integer minutesAfterMidnight = null;
        DialogPreference preference = getPreference();

        if (preference instanceof TimePreference) {
            minutesAfterMidnight = ((TimePreference) preference).getTime();
        }

        // Set the time to the TimePicker
        if (minutesAfterMidnight != null) {
            int hours = minutesAfterMidnight / 60;
            int minutes = minutesAfterMidnight % 60;
            boolean is24hour = DateFormat.is24HourFormat(getContext());

            mTimePicker.setIs24HourView(is24hour);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker.setHour(hours);
                mTimePicker.setMinute(minutes);
            } else {
                mTimePicker.setCurrentHour(hours);
                mTimePicker.setCurrentMinute(minutes);
            }
        }
    }

    /**
     * This method is called when the Dialog is closed
     *
     * @param positiveResult: Whether the Dialog was accepted or canceled
     */
    @Override
    public void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {

            // Getting the current Value from the TimePicker
            int hours;
            int minutes;

            if (Build.VERSION.SDK_INT >= 23) {
                hours = mTimePicker.getHour();
                minutes = mTimePicker.getMinute();
            } else {
                hours = mTimePicker.getCurrentHour();
                minutes = mTimePicker.getCurrentMinute();
            }

            // Generate a value to save
            int minutesAfterMidnight = (hours * 60) + minutes;

            // Save the value
            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                TimePreference timePreference = ((TimePreference) preference);

                // This allows the client to ignore the user Value
                if (timePreference.callChangeListener(minutesAfterMidnight)) {
                    timePreference.setTime(minutesAfterMidnight);
                }
            }
        }
    }
}
