package com.hannah.nomophobia.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.DurationsContentProvider;
import com.hannah.nomophobia.utility.TimeFomatUtility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class OverviewActivity extends Activity {

	private long mCurrentTimeInMillis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Use the current time at creation
		mCurrentTimeInMillis = System.currentTimeMillis();

		setTextFields();
		showGraph(TimeFomatUtility.MILLIS_IN_12_HOURS);
	}

	private void setTextFields() {
		int phoneChecks = phoneChecksInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY);
		String checksIn24Hours = TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(phoneChecks) + " " + getResources().getQuantityString(R.plurals.time, phoneChecks);
		((TextView) findViewById(R.id.checks_in_timeperiod)).setText(checksIn24Hours);

		String averageCheckTime = TimeFomatUtility.formatTime(this, averageIgnoreDurationInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY));
		((TextView) findViewById(R.id.average_check_time)).setText(averageCheckTime);
	}

	private int phoneChecksInTimePeriod(long timePeriod) {
		return cursorForTimePeriod(timePeriod).getCount();
	}

	private Cursor cursorForTimePeriod(long timePeriod) {
		String selectionTime = String.valueOf(mCurrentTimeInMillis - timePeriod);
		String selection = DurationsContentProvider.Contract.Columns.TIME + " > ?";

		return getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[] { selectionTime }, null);
	}

	private double averageIgnoreDurationInTimePeriod(long timePeriod) {
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(DurationsContentProvider.AUTHORITY);
		DurationsContentProvider durationsContentProvider = (DurationsContentProvider) client.getLocalContentProvider();

		String whereClause = DurationsContentProvider.Contract.Columns.TIME + " > " + (mCurrentTimeInMillis - timePeriod);
		double sumColumn = durationsContentProvider.sumColumn(DurationsContentProvider.Contract.Columns.DURATION, whereClause);

		return sumColumn / phoneChecksInTimePeriod(timePeriod);
	}

	private void showGraph(long graphTimePeriod) {
		ArrayList<GraphViewData> graphData = new ArrayList<GraphView.GraphViewData>();

		double x;
		double y;

		Cursor dataCursor = cursorForTimePeriod(graphTimePeriod);

		while (dataCursor.moveToNext()) {
			x = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_TIME));
			y = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_DURATION) / (double) TimeFomatUtility.MILLIS_IN_A_MINUTE);
			graphData.add(new GraphViewData(x, y));
		}

		if (graphData.size() > 1) {

			GraphViewSeries exampleSeries = new GraphViewSeries(graphData.toArray(new GraphViewData[graphData.size()]));

			GraphView graphView = new LineGraphView(this, getString(R.string.graph_title)) {

				@Override
				protected String formatLabel(double value, boolean isValueX) {
					if (isValueX) {
						double timeAgoInMillis = mCurrentTimeInMillis - value;

						if ((mCurrentTimeInMillis - getMinX(false)) < TimeFomatUtility.MILLIS_IN_AN_HOUR) {
							return TimeFomatUtility.displayMinutesAgoToTheMinute(timeAgoInMillis);
						} else {
							return TimeFomatUtility.displayHoursAgoToTheTenth(timeAgoInMillis);
						}
					} else {
						if (value < 60) {
							return TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(value) + " m";
						} else {
							return TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(value / TimeFomatUtility.MINUTES_IN_AN_HOUR) + " hr";
						}
					}
				}

				@Override
				protected double getMaxY() {
					double viewPortMax = super.getMaxY();

					if (viewPortMax < 2) {
						return 2;
					} else if (viewPortMax < 4) {
						return 4;
					} else if (viewPortMax < 20) {
						return 20;
					} else {
						return viewPortMax;
					}
				}

			};

			graphView.addSeries(exampleSeries);
			// graphView.setViewPort(mCurrentTimeInMillis - graphTimePeriod,
			// graphData.size());
			// graphView.setScrollable(true);
			// graphView.setScalable(true);

			LinearLayout layout = (LinearLayout) findViewById(R.id.overview_layout);
			layout.addView(graphView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getShareMessage());
			startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String getShareMessage() {
		int phoneChecksInTimePeriod = phoneChecksInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY);
		String averageTime = TimeFomatUtility.formatTime(this, averageIgnoreDurationInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY));
		return "Nomophobia Android app record: checked phone " + phoneChecksInTimePeriod + " times in the last 24 hours with an average time between checks of " + averageTime;
	}
}
