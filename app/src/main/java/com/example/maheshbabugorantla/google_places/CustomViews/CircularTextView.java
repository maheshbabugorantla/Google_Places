package com.example.maheshbabugorantla.google_places.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * DESCRIPTION: CircularTextView class
 * Created by MaheshBabuGorantla
 * First Update On Sep 01, 2017 .
 * Last Update On Sep 01, 2017.
 */
public class CircularTextView extends android.support.v7.widget.AppCompatTextView{

    private float strokeWidth;
    int strokeColor, solidColor;

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
