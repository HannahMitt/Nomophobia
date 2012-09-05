package com.hannah.phoneaddict;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PhoneAddictWidget extends AppWidgetProvider {

	private static long previousTimeInMillis = System.currentTimeMillis();

	private RemoteViews widgetViews;
	private ComponentName widgetName;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		onUpdate(context, AppWidgetManager.getInstance(context), null);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		widgetName = new ComponentName(context, PhoneAddictWidget.class);

		long currentTimeInMillis = System.currentTimeMillis();
		long timeDiffInMillis = currentTimeInMillis - previousTimeInMillis;
		previousTimeInMillis = currentTimeInMillis;

		widgetViews.setTextViewText(R.id.time_since_check,
				formatTime(timeDiffInMillis));
		appWidgetManager.updateAppWidget(widgetName, widgetViews);
	}

	private String formatTime(long timeDiffInMillis) {
		String formattedTime;

		int seconds = (int) (timeDiffInMillis / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;
		int days = hours / 24;

		if (days > 0) {
			formattedTime = days + " days";
		} else if (hours > 0) {
			formattedTime = hours + " hours";
		} else if (minutes > 0) {
			formattedTime = minutes + " minutes";
		} else {
			formattedTime = seconds + " seconds";
		}

		return formattedTime;
	}
}
