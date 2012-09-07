package com.hannah.phoneaddict.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.hannah.phoneaddict.R;
import com.hannah.phoneaddict.provider.DurationsContentProvider;
import com.hannah.phoneaddict.utility.TimeFomatUtility;

public class OverviewActivity extends Activity {

	private static final NumberFormat numberFormatter = new DecimalFormat("###,###,###");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);

		String checksIn24Hours = numberFormatter.format(phoneChecksInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY)) + " " + getString(R.string.times);
		((TextView) findViewById(R.id.checks_in_timeperiod)).setText(checksIn24Hours);

		String averageCheckTime = TimeFomatUtility.formatTime(this, averageIgnoreDurationInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY));
		((TextView) findViewById(R.id.average_check_time)).setText(averageCheckTime);

		ListView listView = (ListView) findViewById(R.id.durations_list);

		Cursor cursor = getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, null, null, null);
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.duration_cell, cursor, new String[] { DurationsContentProvider.Contract.Columns.DURATION },
				new int[] { R.id.duration_text });

		listView.setAdapter(simpleCursorAdapter);
	}

	private int phoneChecksInTimePeriod(long timePeriod) {
		String selectionTime = String.valueOf(System.currentTimeMillis() - timePeriod);
		String selection = DurationsContentProvider.Contract.Columns.TIME + " > ?";

		Cursor cursor = getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[] { selectionTime }, null);
		return cursor.getCount();
	}

	private double averageIgnoreDurationInTimePeriod(long timePeriod) {
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(DurationsContentProvider.AUTHORITY);
		DurationsContentProvider durationsContentProvider = (DurationsContentProvider) client.getLocalContentProvider();

		String whereClause = DurationsContentProvider.Contract.Columns.TIME + " > " + (System.currentTimeMillis() - timePeriod);
		double sumColumn = durationsContentProvider.sumColumn(DurationsContentProvider.Contract.Columns.DURATION, whereClause);

		return sumColumn / phoneChecksInTimePeriod(timePeriod);
	}
}
