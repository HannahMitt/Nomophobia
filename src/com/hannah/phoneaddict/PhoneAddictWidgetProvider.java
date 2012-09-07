package com.hannah.phoneaddict;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PhoneAddictWidgetProvider extends AppWidgetProvider {

	private static long screenOffTimeInMillis = System.currentTimeMillis();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		startScreenDetectionService(context);
		addOnClickIntent(context);
	}

	private void startScreenDetectionService(Context context) {
		Intent serviceIntent = new Intent(context.getApplicationContext(), ScreenDetectionService.class);
		context.startService(serviceIntent);
	}
	
	private void addOnClickIntent(Context context) {
		Intent overviewIntent = new Intent(context, OverviewActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, overviewIntent, 0);
		
		RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		ComponentName widgetName = new ComponentName(context, PhoneAddictWidgetProvider.class);
		
		widgetViews.setOnClickPendingIntent(R.id.wiget_layout, pendingIntent);
		AppWidgetManager.getInstance(context).updateAppWidget(widgetName, widgetViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			screenOffTimeInMillis = System.currentTimeMillis();
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			updateWidget(context);
		}
	}

	private void updateWidget(Context context) {
		RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		ComponentName widgetName = new ComponentName(context, PhoneAddictWidgetProvider.class);

		long currentTimeInMillis = System.currentTimeMillis();
		long timeDiffInMillis = currentTimeInMillis - screenOffTimeInMillis;

		if(timeDiffInMillis > 0){
			ContentValues durationValues = new ContentValues();
			durationValues.put(DurationsContentProvider.Contract.Columns.DURATION, timeDiffInMillis);
			durationValues.put(DurationsContentProvider.Contract.Columns.TIME, currentTimeInMillis);
			
			context.getContentResolver().insert(DurationsContentProvider.Contract.CONTENT_URI, durationValues);
		}
		
		widgetViews.setTextViewText(R.id.time_since_check, formatTime(context, timeDiffInMillis));
		AppWidgetManager.getInstance(context).updateAppWidget(widgetName, widgetViews);
	}

	private String formatTime(Context context, long timeDiffInMillis) {
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
