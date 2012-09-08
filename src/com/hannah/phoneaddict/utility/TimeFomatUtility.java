package com.hannah.phoneaddict.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;

import com.hannah.phoneaddict.R;

public class TimeFomatUtility {

	private static final long MILLIS_IN_A_MINUTE = 60 * 1000;
	private static final long MILLIS_IN_AN_HOUR = 60 * MILLIS_IN_A_MINUTE;
	
	public static final long MILLIS_IN_A_DAY = 24 * MILLIS_IN_AN_HOUR;
	public static final long MILLIS_IN_6_HOURS = 6 * MILLIS_IN_AN_HOUR;
	
	public static final NumberFormat AVERAGE_DOUBLE_FORMAT = new DecimalFormat("###,###,###.##");

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
			formattedTime = AVERAGE_DOUBLE_FORMAT.format(seconds) + " seconds";
		} else if (hours < 1) {
			formattedTime = AVERAGE_DOUBLE_FORMAT.format(minutes) + " minutes";
		} else if (days < 1) {
			formattedTime = AVERAGE_DOUBLE_FORMAT.format(hours) + " hours";
		} else {
			formattedTime = AVERAGE_DOUBLE_FORMAT.format(days) + " days";
		}

		return formattedTime;
	}
	
	public static String displayHoursAgoToQuarterHour(double dateMillis){
		double fractionalHours = (dateMillis / MILLIS_IN_AN_HOUR) * 10.0;
		double hours = Math.round(fractionalHours) / 10.0;
		
		return AVERAGE_DOUBLE_FORMAT.format(hours) + " hrs ago";
	}
}
