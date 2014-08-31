package com.hannah.nomophobia.view;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.DurationsContentProvider;
import com.hannah.nomophobia.utility.TimeFomatUtility;

/**
 * Created by HannahMitt on 8/31/14.
 */
public class ClockVisual extends Drawable {

    private static final String TAG = "ClockVisual";

    public static final long CLOCK_MILLIS = TimeFomatUtility.MILLIS_IN_6_HOURS;

    private static final double RADIAN_CONVERSION = 2 * Math.PI;
    private static final double RADIAN_START = -Math.PI / 2;

    private Paint mBackgroundPaint;
    private Paint mEventPaint;

    private Cursor mTimesCursor;
    private long mClockStart;

    public ClockVisual(Resources resources) {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mEventPaint = new Paint();
        mEventPaint.setColor(resources.getColor(R.color.top_title_blue));
        mEventPaint.setAntiAlias(true);
        mEventPaint.setStyle(Paint.Style.FILL);
    }

    public void setCursor(Cursor cursor, long startTime) {
        mTimesCursor = cursor;
        mClockStart = startTime;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        float width;
        float height;

        float radius;
        float centerX;
        float centerY;

        Rect bounds = getBounds();
        width = bounds.right - bounds.left;
        height = bounds.bottom - bounds.top;
        radius = Math.min(width, height) / 2;
        centerX = radius;
        centerY = radius;

        canvas.drawCircle(centerX, centerY, radius, mBackgroundPaint);

        if (mTimesCursor != null) {
            double timeInMillis;

            mTimesCursor.moveToFirst();

            while (mTimesCursor.moveToNext()) {
                timeInMillis = mTimesCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_TIME) - mClockStart;
                drawLine(canvas, radius, centerX, centerY, timeInMillis);
            }
        }
    }

    private void drawLine(Canvas canvas, float radius, float centerX, float centerY, double timeInMillis) {
        double radians = RADIAN_START + (timeInMillis / CLOCK_MILLIS) * RADIAN_CONVERSION;

        float stopX = (float) (centerX + (radius * Math.cos(radians)));
        float stopY = (float) (centerY + (radius * Math.sin(radians)));
        canvas.drawLine(centerX, centerY, stopX, stopY, mEventPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
