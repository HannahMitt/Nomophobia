package com.hannah.nomophobia.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.activity.OverviewActivity;
import com.hannah.nomophobia.service.ScreenDetectionService;
import com.hannah.nomophobia.utility.SharedPreferencesUtility;
import com.hannah.nomophobia.utility.TimeFomatUtility;

public class WidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		startScreenDetectionService(context);
		updateWidget(context);
	}

	private void startScreenDetectionService(Context context) {
		Intent serviceIntent = new Intent(context.getApplicationContext(), ScreenDetectionService.class);
		context.startService(serviceIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			SharedPreferencesUtility.updateScreenOffTime(context);
			
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			updateWidget(context);
		}
	}

	private void updateWidget(Context context) {
		RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		ComponentName widgetName = new ComponentName(context, WidgetProvider.class);

		long currentTimeInMillis = System.currentTimeMillis();
		long timeDiffInMillis = currentTimeInMillis - SharedPreferencesUtility.getLastScreenOffTime(context);

		if (timeDiffInMillis > 0) {
			ContentValues durationValues = new ContentValues();
			durationValues.put(DurationsContentProvider.Contract.Columns.DURATION, timeDiffInMillis);
			durationValues.put(DurationsContentProvider.Contract.Columns.TIME, currentTimeInMillis);

			context.getContentResolver().insert(DurationsContentProvider.Contract.CONTENT_URI, durationValues);
		}

		widgetViews.setTextViewText(R.id.time_since_check, TimeFomatUtility.formatTime(context, timeDiffInMillis));
		widgetViews.setOnClickPendingIntent(R.id.wiget_layout, getOnClickIntent(context));
		AppWidgetManager.getInstance(context).updateAppWidget(widgetName, widgetViews);
	}
	
	private PendingIntent getOnClickIntent(Context context) {
		Intent overviewIntent = new Intent(context, OverviewActivity.class);
		overviewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return PendingIntent.getActivity(context, 0, overviewIntent, 0);
	}
}
