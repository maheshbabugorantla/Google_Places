package com.example.maheshbabugorantla.google_places.CustomPreferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.example.maheshbabugorantla.google_places.R;

/**
 * DESCRIPTION: TimePreference class
 * Created by MaheshBabuGorantla
 * First Update On Apr 02, 2017 .
 * Last Update On Apr 02, 2017.
 */

public class TimePreference extends DialogPreference {

    private int mTime;
    private int mDialogLayoutResId = R.layout.pref_dialog_time;

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public TimePreference(Context context) {
        this(context, null);
    }

    // Methods to save time to the Shared Preferences
    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;

        // Save to the sharedPreferences
        persistInt(mTime);
    }

    // From here we will override from methods

    // Overriding the method to read the default Value which is defined using android:defaultValue
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        // Read value input by the user else, use the Default Value provided
        setTime(restorePersistedValue ? getPersistedInt(mTime) : (int) defaultValue);
    }

    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }
}
