package com.hannah.nomophobia.utility;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by HannahMitt on 8/31/14.
 */
public class TypefaceUtility {

    private static final String TYPEFACE_MARKET = "Market_Deco.ttf";

    public static SpannableString getTypeFaceSpan(Context context, String string) {
        SpannableString s = new SpannableString(string);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), TYPEFACE_MARKET);
        s.setSpan(new TypefaceSpan(typeface), 0, s.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    private static class TypefaceSpan extends MetricAffectingSpan {

        private Typeface mTypeface;

        public TypefaceSpan(Typeface typeface) {
            mTypeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint textPaint) {
            textPaint.setTypeface(mTypeface);

            // Note: This flag is required for proper typeface rendering
            textPaint.setFlags(textPaint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setTypeface(mTypeface);

            // Note: This flag is required for proper typeface rendering
            textPaint.setFlags(textPaint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
