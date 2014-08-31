package com.hannah.nomophobia.view;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.hannah.nomophobia.utility.NomoLog;

/**
 * Created by HannahMitt on 8/31/14.
 */
public class ClockVisual extends Drawable {

    private static final String TAG = "ClockVisual";
    private static final double RADIAN_CONVERSION = Math.PI / 180;

    private Paint mBackgroundPaint;
    private Paint mEventPaint;

    private Cursor mTimesCursor;

    public ClockVisual() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mEventPaint = new Paint();
        mEventPaint.setColor(Color.BLUE);
        mEventPaint.setAntiAlias(true);
        mEventPaint.setStyle(Paint.Style.FILL);
    }

    public void setCursor(Cursor cursor) {
        mTimesCursor = cursor;
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

        float stopX;
        float stopY;
        if (mTimesCursor != null) {
            NomoLog.d(TAG, "starting radial drawing");
            for (int i = 0; i < 100; i++) {
                NomoLog.d(TAG, "line");
                stopX = (float) (centerX + (radius * Math.cos(i * RADIAN_CONVERSION)));
                stopY = (float) (centerY + (radius * Math.cos(i * RADIAN_CONVERSION)));
                canvas.drawLine(centerX, centerY, stopX, stopY, mEventPaint);
            }
        } else {
            NomoLog.d(TAG, "cursor null");
        }
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
