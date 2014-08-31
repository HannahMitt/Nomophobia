package com.hannah.nomophobia.utility;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.hannah.nomophobia.provider.DurationsContentProvider;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class GraphUtility {

	private static final int MAX_Y_IN_MINS = 60;
	
	public static GraphView getGraphOfTimes(Context context, long currentTimeMillis, long graphTimePeriod) {
		ArrayList<GraphViewData> graphData = new ArrayList<GraphView.GraphViewData>();

		double x;
		double y;

		Cursor dataCursor = DatabaseUtility.cursorForTimePeriod(context, currentTimeMillis, graphTimePeriod);

		while (dataCursor.moveToNext()) {
			x = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_TIME));
			y = (dataCursor.getLong(DurationsContentProvider.Contract.Columns.INDEX_DURATION) / (double) TimeFomatUtility.MILLIS_IN_A_MINUTE);
			graphData.add(new GraphViewData(x, y));
		}

		if (graphData.size() > 1) {

			GraphViewSeries exampleSeries = new GraphViewSeries(graphData.toArray(new GraphViewData[graphData.size()]));
			GraphView graphView = getLineGraphView(context, currentTimeMillis);
			graphView.addSeries(exampleSeries);

			// graphView.setViewPort(mCurrentTimeInMillis - graphTimePeriod,
			// graphData.size());
			// graphView.setScrollable(true);
			// graphView.setScalable(true);

			return graphView;
		} else {
			return null;
		}
	}

	private static GraphView getLineGraphView(Context context, final long currentTimeMillis) {
		return new LineGraphView(context, "") {

			@Override
			protected String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					double timeAgoInMillis = currentTimeMillis - value;

					if ((currentTimeMillis - getMinX(false)) < TimeFomatUtility.MILLIS_IN_AN_HOUR) {
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
			protected double getMinY() {
				return 0;
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
				} else if (viewPortMax > MAX_Y_IN_MINS) {
					return MAX_Y_IN_MINS;
				} else {
					return viewPortMax;
				}
			}
			
			@Override
			protected double getMaxX(boolean ignoreViewport) {
				return currentTimeMillis;
			}

		};
	}
}
