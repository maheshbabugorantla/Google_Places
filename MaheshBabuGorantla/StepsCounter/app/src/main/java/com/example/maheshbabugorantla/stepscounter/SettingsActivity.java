package com.example.maheshbabugorantla.stepscounter;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 *  Created by MaheshBabuGorantla on 2/10/2017.
 *  This is used to enable to user to set the settings for the application
 *  Date Created: Feb 10, 2017, Last Updated: Feb 10, 2017
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    /**
     *  This is used to create the SettingsActivity
     *  Author: Mahesh Babu Gorantla, Date Created: Feb 10, 2017, Last Edited: Feb 10, 2017
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here add the preference Manager created with the xml file
        getFragmentManager().beginTransaction().add(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    /**
     *  This method is called whenever the user makes changes to the existing settings
     *  Author: Mahesh Babu Gorantla, Date Created: Feb 10, 2017, Last Edited: Feb 10, 2017
     * */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String newStepsGoals = newValue.toString();

        // handling when the user has input the new Steps Goal
        if(preference instanceof EditTextPreference) {
             preference.setSummary(newStepsGoals);
        }

        return true;
    }

    /**
     *  This Fragment is used to add the possible preferences that can be given to the user.
     *  Author: Mahesh Babu Gorantla, Date Created: Feb 10, 2017, Last Edited: Feb 10, 2017
     * */
    public static class MyPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Adding the settings using settings preferences xml file
            addPreferencesFromResource(R.xml.settings_preferences);

            new SettingsActivity().bindPreferenceSummarytoValue(findPreference(getString(R.string.pref_steps_goal_key)));
        }
    }

    /**
     *  This is used to save the new preferences from the user whenever he/she makes changes to the Settings
     *  Author: Mahesh Babu Gorantla, Date Created: Feb 10, 2017, Last Edited: Feb 10, 2017
     * */
    protected void bindPreferenceSummarytoValue(Preference preference) {

        // Set the Listener to watch for the preference changes
        preference.setOnPreferenceChangeListener(this);

        // Immediately make changes to the user preferences as soon as there is a change
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }
}