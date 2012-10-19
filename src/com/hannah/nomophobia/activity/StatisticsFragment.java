package com.hannah.nomophobia.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.StatisticsSingleton;
import com.hannah.nomophobia.utility.GraphUtility;
import com.hannah.nomophobia.utility.TimeFomatUtility;
import com.jjoe64.graphview.GraphView;

public class StatisticsFragment extends Fragment {

	private GraphView mGraphView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.statistics, container, false);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTextFields();
		showGraph();
	}

	private void setTextFields() {
		int phoneChecks = StatisticsSingleton.getPhoneChecks();
		String checksIn24Hours = TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(phoneChecks) + " " + getResources().getQuantityString(R.plurals.time, phoneChecks);
		((TextView) getView().findViewById(R.id.checks_in_timeperiod)).setText(checksIn24Hours);

		String timeperiod = String.format(getString(R.string.in_the_last_timeperiod), TimeFomatUtility.displayClosestHour(StatisticsSingleton.getLongestTimeAge()));
		((TextView) getView().findViewById(R.id.timeperiod)).setText(timeperiod);

		String averageCheckTime = TimeFomatUtility.formatTime(getActivity(), StatisticsSingleton.getAverageIgnoreTime());
		((TextView) getView().findViewById(R.id.average_check_time)).setText(averageCheckTime);
	}

	private void showGraph() {

		FrameLayout layout = (FrameLayout) getView().findViewById(R.id.graph_frame);
		
		if(mGraphView != null){
			layout.removeView(mGraphView);
		}
		
		mGraphView = GraphUtility.getGraphOfTimes(getActivity(), StatisticsSingleton.getCurrentTime(), TimeFomatUtility.MILLIS_IN_12_HOURS);

		if (mGraphView != null) {
			layout.addView(mGraphView);
		}
	}
}
