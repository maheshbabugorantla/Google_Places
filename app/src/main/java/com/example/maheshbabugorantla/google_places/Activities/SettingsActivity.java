package com.example.maheshbabugorantla.google_places.Activities;

/**
 * Created by Mahesh Babu Gorantla on 10/10/17.
 * Last Updated: 10 Oct 2017
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.TextView;

import com.example.maheshbabugorantla.google_places.CustomPreferences.NumberPickerPreference;
import com.example.maheshbabugorantla.google_places.R;
import com.example.maheshbabugorantla.google_places.CustomPreferences.TimePreference;
import com.example.maheshbabugorantla.google_places.CustomPreferences.TimePreferenceDialogFragmentCompat;

/**
 * DESCRIPTION: SettingsActivity class
 * Created by MaheshBabuGorantla
 * First Update On Apr 02, 2017 .
 * Last Update On Apr 02, 2017.
 */

public class SettingsActivity extends AppCompatActivity implements Preference.OnPreferenceChangeListener {
    private final String TAG = "SettingsActivity";

    /**
     *  This is used to create the SettingsActivity
     *  Author: Mahesh Babu Gorantla
     *  Date Created: Oct 10, 2017
     *  Last Edited: Oct 10, 2017
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here add the preference Manager created with the xml file
        if(savedInstanceState == null){
            Fragment preferenceFragment = new MyPreferenceFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, preferenceFragment);
            fragmentTransaction.commit();
        }
    }

    /**
     *  This method is called whenever the user makes changes to the existing settings
     *  Author: Mahesh Babu Gorantla
     *  Date Created: Oct 10, 2017
     *  Last Edited: Oct 10, 2017
     * */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        // handling when the user has input the new Steps Goal
        if(preference instanceof EditTextPreference) {
            String textValue = newValue.toString();
            preference.setSummary(textValue);
        }
        return true;
    }

    /**
     *  This Fragment is used to add the possible preferences that can be given to the user.
     *  Author: Mahesh Babu Gorantla
     *  Date Created: Oct 10, 2017
     *  Last Edited: Oct 10, 2017
     * */
    public static class MyPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Adding the settings using settings preferences xml file
            addPreferencesFromResource(R.xml.settings_preferences);

            // Fetching the Keys that reference different settings on the settings page
            new SettingsActivity().bindPreferenceSummarytoValue(findPreference(getString(R.string.pref_steps_goal_key)));
            new SettingsActivity().bindPreferenceSummarytoValue(findPreference(getString(R.string.pref_height_key)));
            new SettingsActivity().bindPreferenceSummarytoValue(findPreference(getString(R.string.pref_weight_key)));
            new SettingsActivity().bindPreferenceSummarytoValue(findPreference(getString(R.string.pref_age_key)));
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {

            // Try if the preference is one of our custom preferences
            DialogFragment dialogFragment = null;

            if(preference instanceof TimePreference) {
                // Create a new instance of teh TimePreferenceDialogFragmentCompat
                // with the key of related Preference
                dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey());
            }
            else if(preference instanceof NumberPickerPreference) {
                dialogFragment = NumberPickerPreference.NumberPickerPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            }

            // If it is one of our custom Preferences, show its dialog
            if(dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference" + ".PreferenceFragment.DIALOG");
            }
            else{
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }

    /**
     *  This is used to save the new preferences from the user whenever he/she makes changes to the Settings
     *  Author: Mahesh Babu Gorantla
     *  Date Created: Oct 10, 2017
     *  Last Edited: Oct 10, 2017
     * */
    protected void bindPreferenceSummarytoValue(Preference preference) {

        // Set the Listener to watch for the preference changes
        preference.setOnPreferenceChangeListener(this);

        // Immediately make changes to the user preferences as soon as there is a change
        if (preference instanceof EditTextPreference) {
            onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
        }
    }
}

