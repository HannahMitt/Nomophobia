package com.hannah.nomophobia.utility;

import android.util.Log;

/**
 * Created by HannahMitt on 8/31/14.
 */
public class NomoLog {

    private static final boolean IS_DEBUG = true;

    public static void d(String tag, String message) {
        if (IS_DEBUG) {
            Log.d(tag, message);
        }
    }
}
