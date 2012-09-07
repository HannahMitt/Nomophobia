package com.hannah.phoneaddict.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.hannah.phoneaddict.R;
import com.hannah.phoneaddict.provider.DurationsContentProvider;
import com.hannah.phoneaddict.utility.TimeFomatUtility;

public class OverviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);

		ListView listView = (ListView) findViewById(R.id.durations_list);

		Cursor cursor = getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, null, null, null);
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.duration_cell, cursor, new String[] { DurationsContentProvider.Contract.Columns.DURATION },
				new int[] { R.id.duration_text });
		
		listView.setAdapter(simpleCursorAdapter);
		
		((TextView)findViewById(R.id.checks_in_timeperiod)).setText("You checked your phone " + phoneChecksInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY) + " times in the last 24 hours");
	}
	
	private int phoneChecksInTimePeriod(long timePeriod){
		String selectionTime = String.valueOf(System.currentTimeMillis() - timePeriod);
		String selection = DurationsContentProvider.Contract.Columns.TIME  + " > ?";
		
		Cursor cursor = getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[] {selectionTime}, null);
		return cursor.getCount();
	}
}
