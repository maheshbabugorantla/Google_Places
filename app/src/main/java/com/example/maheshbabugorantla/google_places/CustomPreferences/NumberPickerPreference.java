package com.example.maheshbabugorantla.google_places.CustomPreferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.example.maheshbabugorantla.google_places.R;

/**
 * Created by Mahesh Babu Gorantla
 * Created on Oct 10, 2017
 * Last Updated on Oct 10, 2017
 */

public class NumberPickerPreference extends DialogPreference {
    private int currentValue;
    private int maxValue;
    private int minValue;

    private static final int DEFAULT_value = 0;
    private static final int DEFAULT_maxValue = 0;
    private static final int DEFAULT_minValue = 0;

    private final String defaultSummary;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        defaultSummary = getSummary().toString();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);

        try {
            maxValue = a.getInt(R.styleable.NumberPickerPreference_maxValue, DEFAULT_maxValue);
            minValue = a.getInt(R.styleable.NumberPickerPreference_minValue, DEFAULT_minValue);
        } finally {
            a.recycle();
        }

        setDialogLayoutResource(R.layout.numberpicker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);

    }

    public int getValue() {
        return currentValue;
    }

    public void setValue(int value) {
        currentValue = value;
        persistInt(currentValue);
        setSummary(String.format(defaultSummary, getValue()));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, DEFAULT_value);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(currentValue) : (Integer) defaultValue);
    }

    public static class NumberPickerPreferenceDialogFragmentCompat
            extends PreferenceDialogFragmentCompat {
        private static final String SAVE_STATE_VALUE = "NumberPickerPreferenceDialogFragment.value";
        private CustomNumberPicker picker;
        private int currentValue = 1;

        public NumberPickerPreferenceDialogFragmentCompat() {
        }

        public static NumberPickerPreferenceDialogFragmentCompat newInstance(String key) {
            NumberPickerPreferenceDialogFragmentCompat fragment =
                    new NumberPickerPreferenceDialogFragmentCompat();
            Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);
            return fragment;
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (savedInstanceState == null) {
                currentValue = getNumberPickerPreference().getValue();
            } else {
                currentValue = savedInstanceState.getInt(SAVE_STATE_VALUE);
            }
        }

        public void onSaveInstanceState(@NonNull Bundle outState) {
            outState.putInt(SAVE_STATE_VALUE, currentValue);
        }

        private NumberPickerPreference getNumberPickerPreference() {
            return (NumberPickerPreference) this.getPreference();
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);
            picker = (CustomNumberPicker) view.findViewById(R.id.numpicker_pref);
            picker.setMaxValue(getNumberPickerPreference().maxValue);
            picker.setMinValue(getNumberPickerPreference().minValue);
            picker.setValue(currentValue);
        }

        @Override
        public void onDialogClosed(boolean b) {
            if (b) {
                int value = picker.getValue();
                if(getPreference().callChangeListener(value)) {
                    getNumberPickerPreference().setValue(value);
                }
            }
        }
    }
}