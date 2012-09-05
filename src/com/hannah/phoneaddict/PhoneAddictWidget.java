package com.hannah.phoneaddict;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class PhoneAddictWidget extends AppWidgetProvider {

	private static long previousTimeInMillis = System.currentTimeMillis();
	
	private RemoteViews widgetViews;
	private ComponentName widgetName;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		Log.d("update", "on update called with previous time " + previousTimeInMillis + " and current time " + System.currentTimeMillis());
		onUpdate(context, AppWidgetManager.getInstance(context), null);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Log.d("update", "on update called with previous time " + previousTimeInMillis + " and current time " + System.currentTimeMillis());
		
		widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		widgetName = new ComponentName(context, PhoneAddictWidget.class);
		
		long timeDiffInMillis = System.currentTimeMillis() - previousTimeInMillis;
		previousTimeInMillis = System.currentTimeMillis();
		
		widgetViews.setTextViewText(R.id.time_since_check, (timeDiffInMillis / 1000) + " seconds");
		appWidgetManager.updateAppWidget(widgetName, widgetViews);
	}
}
