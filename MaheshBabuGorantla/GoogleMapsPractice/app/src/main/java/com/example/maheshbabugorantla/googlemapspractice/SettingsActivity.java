package com.example.maheshbabugorantla.googlemapspractice;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by MaheshBabuGorantla on 2/6/2017.
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here we add the general preference created using the XML File
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String preferenceValue = newValue.toString();

        System.out.println("String value in onPreferenceChange: " + preferenceValue);

        // For list preferences, look up correct display value in rhe preference's entries list (since they have separate labels/values)
        if(preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;

            // finding the index of the Selected Value
            int prefIndex = listPreference.findIndexOfValue(preferenceValue);

            // Setting the summary of the Selected Option in the List Preference
            if(prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        return true;
    }


    // Preference Fragment
    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Adding the resources from the settings xml file
            addPreferencesFromResource(R.xml.settings_preferences);
            new SettingsActivity().bindPreferenceSummarytoValue(findPreference(getString(R.string.pref_categories_key)));
        }
    }

    protected void bindPreferenceSummarytoValue(Preference preference) {

        // Set the listener to the watch for the preference changes
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener Immediately with the preference's Current Value
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));
    }
}
