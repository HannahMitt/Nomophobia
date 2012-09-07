package com.hannah.phoneaddict.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

	private static final NumberFormat numberFormatter = new DecimalFormat("###,###,###");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);

		String checksIn24Hours = numberFormatter.format(phoneChecksInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY)) + " " + getString(R.string.times);
		((TextView) findViewById(R.id.checks_in_timeperiod)).setText(checksIn24Hours);

		String averageCheckTime = TimeFomatUtility.formatTime(this, averageIgnoreDurationInTimePeriod(TimeFomatUtility.MILLIS_IN_A_DAY));
		((TextView) findViewById(R.id.average_check_time)).setText(averageCheckTime);

		showGraph();
	}

	private int phoneChecksInTimePeriod(long timePeriod) {
		return cursorForTimePeriod(timePeriod).getCount();
	}
	
	private Cursor cursorForTimePeriod(long timePeriod) {
		String selectionTime = String.valueOf(System.currentTimeMillis() - timePeriod);
		String selection = DurationsContentProvider.Contract.Columns.TIME + " > ?";

		return getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[] { selectionTime }, null);
	}

	private double averageIgnoreDurationInTimePeriod(long timePeriod) {
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(DurationsContentProvider.AUTHORITY);
		DurationsContentProvider durationsContentProvider = (DurationsContentProvider) client.getLocalContentProvider();

		String whereClause = DurationsContentProvider.Contract.Columns.TIME + " > " + (System.currentTimeMillis() - timePeriod);
		double sumColumn = durationsContentProvider.sumColumn(DurationsContentProvider.Contract.Columns.DURATION, whereClause);

		return sumColumn / phoneChecksInTimePeriod(timePeriod);
	}

	private void showGraph() {
		ArrayList<GraphViewData> graphData = new ArrayList<GraphView.GraphViewData>();
		
		Cursor dataCursor = cursorForTimePeriod(6 * 60 * 60 * 1000);
		dataCursor.moveToFirst();
		
		double x;
		double y;
		
		while(dataCursor.moveToNext()){
			x = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_TIME) / 1000) / 60.0;
			y = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_DURATION) / 1000) / 60.0;
			graphData.add(new GraphViewData(x, y));
		}
		
		GraphViewSeries exampleSeries = new GraphViewSeries(graphData.toArray(new GraphViewData[graphData.size()]));

		GraphView graphView = new LineGraphView(this, "Phone Addiction Graph");
		graphView.addSeries(exampleSeries);

		LinearLayout layout = (LinearLayout) findViewById(R.id.overview_layout);
		layout.addView(graphView);
	}
}
