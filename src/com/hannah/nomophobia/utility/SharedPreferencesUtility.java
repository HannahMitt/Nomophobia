package com.hannah.nomophobia.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {

	private static final String PREFS_NAME = "nomophobia_prefs";
	private static final String PREF_LAST_SCREEN_OFF = "screen_off_pref";

	public static void updateScreenOffTime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(PREF_LAST_SCREEN_OFF, System.currentTimeMillis());
		editor.commit();
	}

	public static long getLastScreenOffTime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return prefs.getLong(PREF_LAST_SCREEN_OFF, System.currentTimeMillis());
	}
}
