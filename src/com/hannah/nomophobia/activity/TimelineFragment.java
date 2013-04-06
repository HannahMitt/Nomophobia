package com.hannah.nomophobia.activity;

import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.DurationsContentProvider;
import com.hannah.nomophobia.utility.DatabaseUtility;
import com.hannah.nomophobia.view.TimeMarkerView;

public class TimelineFragment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		setUpTimeline();
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View view = inflater.inflate(R.layout.timeline, container, false);
	// return view;
	// }

	private void setUpTimeline() {
		LinearLayout timelineLayout = (LinearLayout) findViewById(R.id.timeline_layout);

		Calendar timeCalc = Calendar.getInstance();
		timeCalc.add(Calendar.HOUR, -11);

		View timecell;

		for (int i = 0; i < 11; i++) {
			timecell = getCellView(timeCalc, 60);
			timelineLayout.addView(timecell);

			timeCalc.add(Calendar.HOUR, 1);
		}

		timecell = getCellView(timeCalc, timeCalc.get(Calendar.MINUTE));
		timelineLayout.addView(timecell);
	}

	private View getCellView(Calendar timeCalc, int minutes) {
		RelativeLayout timecell = (RelativeLayout) getLayoutInflater().inflate(R.layout.time_cell, null);
		int timewidth = (int) ((minutes * 2) * getResources().getDisplayMetrics().density);
		timecell.setLayoutParams(new LayoutParams(timewidth, getResources().getDimensionPixelSize(R.dimen.time_cell_height)));

		TextView hourText = (TextView) timecell.findViewById(R.id.hour_text);
		hourText.setText(timeCalc.get(Calendar.HOUR) + ":00");

		addTimeMarker(timeCalc, timecell);

		return timecell;
	}

	private void addTimeMarker(Calendar timeCalc, RelativeLayout timecell) {
		Calendar startTime = (Calendar) timeCalc.clone();
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		Calendar endTime = (Calendar) startTime.clone();
		endTime.add(Calendar.HOUR, 1);

		long x;
		Calendar xDate = Calendar.getInstance();

		Cursor dataCursor = DatabaseUtility.cursorForTimeWindow(this, startTime.getTimeInMillis(), endTime.getTimeInMillis());
		while (dataCursor.moveToNext()) {
			x = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_TIME));
			xDate.setTimeInMillis(x);
			timecell.addView(new TimeMarkerView(this, xDate.get(Calendar.MINUTE)));
		}
	}
}
