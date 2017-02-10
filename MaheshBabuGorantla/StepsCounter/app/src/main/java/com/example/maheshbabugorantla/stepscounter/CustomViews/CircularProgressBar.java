package com.example.maheshbabugorantla.stepscounter.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.maheshbabugorantla.stepscounter.R;

/**
 * Created by MaheshBabuGorantla on 2/10/2017.
 *
 * 
 */

public class CircularProgressBar extends View {

    /**
     *  The Below variables are used to set the thickness for the Circular Progress Bar
     *  Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     **/
    private float strokeWidth = 100;
    private int progress = 0;
    private int min = 0;
    private int max = 100;

    /**
     *  Start the Progress at 12 o'clock
     *  Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     **/
    private int startAngle = 0;
    private int color = Color.rgb(242, 133, 0);
    private RectF rectF;

    /**
     *  Paint objects hold the style and color information about how to draw geometries,
     *  text and bitmaps.
     *  Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     * */
    private Paint backgroundPaint;
    private Paint foregroundPaint;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    /**
    *   This is used to initialize the CircularProgressBar View Properties
     *  Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
    * */
    private void initialize(Context context, AttributeSet attributeSet) {

        rectF = new RectF();

        // TypedArray is used create an array of other resources, such as drawables. This array need
        // not be homogeneous, it could contain mixed resource types.
        TypedArray typedArray = context.getTheme().obtainStyledAttributes
                                (
                                    attributeSet,
                                    R.styleable.CircularProgressBar,
                                    0, 0
                                );

        // Reading values from the XML Layout
        try {
            strokeWidth = typedArray.getDimension(R.styleable.CircularProgressBar_progressBarThickness, strokeWidth);
            progress = typedArray.getInteger(R.styleable.CircularProgressBar_progress, progress);
            min = typedArray.getInt(R.styleable.CircularProgressBar_min, min);
            max = typedArray.getInt(R.styleable.CircularProgressBar_max, max);
            color = typedArray.getColor(R.styleable.CircularProgressBar_progressbarColor, color);
        }
        finally {
            // This almost works similar to C Pointer, Recycles the typedArray to be re-used for a later call
            // After calling this function you must not ever touch the typed array again.
            // Else it will raise a RuntimeException if the typed Array has already been recycled.
            typedArray.recycle();
        }

        // Setting the background and foreground colors
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, 1.3f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    /**
     * This is used the adjust the Alpha factor in the color.
     * Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     * */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color)*factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }

    /**
     *  This is used by the LayoutManager to specialize the calculation of  children of the Custom View
     *  Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth/2, 0 + strokeWidth/2, min - strokeWidth/2, min - strokeWidth/2);
    }

    /**
    *   This Draw the View on the Screen
     *   Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / max;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    /**
     *  This is used to update the Progress Bar as the user's step Count Starts Increasing
    *   Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
    * */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // This notifies the view to redraw itself which in turn calls "onDraw".
    }

    /**
     *  This will be used in conjunction with the user preference of Maximum Step Count of the Day
     *  Author: Mahesh Babu Gorantla, Date: Feb 10, 2017
     * */
    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
