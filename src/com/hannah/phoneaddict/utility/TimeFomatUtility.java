package com.hannah.phoneaddict.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;

import com.hannah.phoneaddict.R;

public class TimeFomatUtility {

	public static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;

	private static final NumberFormat averageDoubleFormat = new DecimalFormat("###,###,###.##");

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

	public static String formatTime(Context context, double timeDiffInMillis) {
		String formattedTime;

		double seconds = (int) (timeDiffInMillis / 1000);
		double minutes = seconds / 60;
		double hours = minutes / 60;
		double days = hours / 24;

		if (minutes < 1) {
			formattedTime = averageDoubleFormat.format(seconds) + " seconds";
		} else if (hours < 1) {
			formattedTime = averageDoubleFormat.format(minutes) + " minutes";
		} else if (days < 1) {
			formattedTime = averageDoubleFormat.format(hours) + " hours";
		} else {
			formattedTime = averageDoubleFormat.format(days) + " days";
		}

		return formattedTime;
	}
}
