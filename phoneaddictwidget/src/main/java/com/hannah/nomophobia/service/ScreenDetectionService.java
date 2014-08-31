package com.hannah.nomophobia.service;

import com.hannah.nomophobia.provider.WidgetProvider;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ScreenDetectionService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);

		WidgetProvider widgetReceiver = new WidgetProvider();
		registerReceiver(widgetReceiver, intentFilter);
	}
}
