package com.hannah.nomophobia.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;

import com.hannah.nomophobia.R;

public class TimeFomatUtility {

	public static final int MINUTES_IN_AN_HOUR = 60;
	public static final long MILLIS_IN_A_MINUTE = 60 * 1000;
	public static final long MILLIS_IN_AN_HOUR = MINUTES_IN_AN_HOUR * MILLIS_IN_A_MINUTE;
	public static final long MILLIS_IN_A_DAY = 24 * MILLIS_IN_AN_HOUR;
	public static final long MILLIS_IN_6_HOURS = 6 * MILLIS_IN_AN_HOUR;
	public static final long MILLIS_IN_12_HOURS = 12 * MILLIS_IN_AN_HOUR;
	
	public static final NumberFormat AVERAGE_DOUBLE_FORMAT = new DecimalFormat("###,###,###.##");

	public static String formatTime(Context context, long timeDiffInMillis) {
		String formattedTime;

		int seconds = (int) (timeDiffInMillis / 1000);
		int minutes = seconds / 60;
		int hours = minutes / MINUTES_IN_AN_HOUR;
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
		double hours = minutes / MINUTES_IN_AN_HOUR;
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
	
	public static String displayClosestHour(double timeAgoInMillis){
		double hours = timeAgoInMillis / MILLIS_IN_AN_HOUR;
		
		if(hours < 1){
			return "hour";
		} else if (hours > 23){
			return "24 hours";
		} else {
			return (int) Math.round(hours) + " hours";
		}
	}
	
	public static String displayHoursAgoToTheTenth(double timeAgoInMillis){
		double fractionalHours = (timeAgoInMillis / MILLIS_IN_AN_HOUR) * 10.0;
		double hours = Math.round(fractionalHours) / 10.0;
		
		return AVERAGE_DOUBLE_FORMAT.format(hours) + " hrs ago";
	}
	
	public static String displayMinutesAgoToTheMinute(double timeAgoInMillis){
		return (int) Math.round(timeAgoInMillis / TimeFomatUtility.MILLIS_IN_A_MINUTE) + " min ago";
	}
}
