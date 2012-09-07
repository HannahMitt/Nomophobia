package com.hannah.phoneaddict.utility;

import android.content.Context;

import com.hannah.phoneaddict.R;

public class TimeFomatUtility {

	public static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;
	
	public static String formatTime(Context context, long timeDiffInMillis) {
		String formattedTime;

		int seconds = (int) (timeDiffInMillis / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;
		int days = hours / 24;

		if (days > 0) {
			formattedTime = context.getResources().getQuantityString(R.plurals.day, days, days);
		} else if (hours > 0) {
			formattedTime = context.getResources().getQuantityString(R.plurals.hour, hours, hours);
		} else if (minutes > 0) {
			formattedTime = context.getResources().getQuantityString(R.plurals.minute, minutes, minutes);
		} else {
			formattedTime = context.getResources().getQuantityString(R.plurals.second, seconds, seconds);
		}

		return formattedTime;
	}
}
