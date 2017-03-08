package com.example.maheshbabugorantla.stepscounter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

/**
 *  Created by MaheshBabuGorantla on 2/10/2017.
 *  This is used to enable to user to set the settings for the application
 *  Date Created: Feb 10, 2017, Last Updated: Feb 10, 2017
 */
public class SettingsActivity extends AppCompatActivity implements Preference.OnPreferenceChangeListener {

    private final String TAG = "SettingsActivity";

    /**
     *  This is used to create the SettingsActivity
     *  Author: Mahesh Babu Gorantla
     *  Date Created: Feb 10, 2017
     *  Last Edited: Feb 10, 2017
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
     *  Author: Mahesh Babu Gorantl
     *
     *  Date Created: Feb 10, 2017, Last Edited: Feb 10, 2017
     * */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        // handling when the user has input the new Steps Goal
        if(preference instanceof EditTextPreference) {
            String textValue = newValue.toString();
             preference.setSummary(textValue);
        }

/*        if(preference instanceof SwitchPreference) {
            String locationServicesEnabled = newValue.toString();
            if(locationServicesEnabled == "true") {
                preference.setSummary("ON");
            }
            else {
                preference.setSummary("OFF");
            }
        } */

        return true;
    }

    /**
     *  This Fragment is used to add the possible preferences that can be given to the user.
     *  Author: Mahesh Babu Gorantla, Date Created: Feb 10, 2017, Last Edited: Feb 10, 2017
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
    }

    /**
     *  This is used to save the new preferences from the user whenever he/she makes changes to the Settings
     *  Author: Mahesh Babu Gorantla
     *  Date Created: Feb 10, 2017
     *  Last Edited: Feb 10, 2017
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