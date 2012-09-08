package com.hannah.phoneaddict.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannah.phoneaddict.R;
import com.hannah.phoneaddict.provider.DurationsContentProvider;
import com.hannah.phoneaddict.utility.TimeFomatUtility;
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
			y = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_DURATION) / 1000) / 60.0;
			graphData.add(new GraphViewData(x, y));
		}

		if (graphData.size() > 1) {

			GraphViewSeries exampleSeries = new GraphViewSeries(graphData.toArray(new GraphViewData[graphData.size()]));

			GraphView graphView = new LineGraphView(this, "Phone Addiction Over Time") {

				@Override
				protected String formatLabel(double value, boolean isValueX) {
					if (isValueX) {
						double timeAgoInMillis = mCurrentTimeInMillis - value;

						if ((mCurrentTimeInMillis - getMinX(false)) < TimeFomatUtility.MILLIS_IN_AN_HOUR) {
							return TimeFomatUtility.displayMinutesAgoToTheMinute(timeAgoInMillis);
						} else {
							return TimeFomatUtility.displayHoursAgoToQuarterHour(timeAgoInMillis);
						}
					} else {
						return TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(value) + " m";
					}
				}

				@Override
				protected double getMaxY() {
					double viewPortMax = super.getMaxY();

					if(viewPortMax < 2){
						return 2;
					} else if (viewPortMax < 4) {
						return 4;
					} else if (viewPortMax < 20) {
						return 20;
					} else {
						return 60;
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
}
