package com.hannah.phoneaddict.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hannah.phoneaddict.R;
import com.hannah.phoneaddict.activity.OverviewActivity;
import com.hannah.phoneaddict.service.ScreenDetectionService;
import com.hannah.phoneaddict.utility.TimeFomatUtility;

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
		
		widgetViews.setTextViewText(R.id.time_since_check, TimeFomatUtility.formatTime(context, timeDiffInMillis));
		AppWidgetManager.getInstance(context).updateAppWidget(widgetName, widgetViews);
	}
}
