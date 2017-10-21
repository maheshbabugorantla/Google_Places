package com.example.maheshbabugorantla.google_places.CustomPreferences;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by Mahesh Babu Gorantla
 * Project Google_Places
 * Created on Oct 10, 2017
 * Last Updated on Oct 10, 2017
 */

public class CustomNumberPicker extends NumberPicker {

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        initEditText(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        initEditText(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        initEditText(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        initEditText(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        initEditText(child);
    }

    private void initEditText(View view) {
        if (view instanceof EditText) {
            EditText inputText = (EditText) view;
            inputText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        CustomNumberPicker.this.setValue(Integer.parseInt(s.toString()));
                    } catch (NumberFormatException ignored) {}
                }

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            });
        }
    }
}
