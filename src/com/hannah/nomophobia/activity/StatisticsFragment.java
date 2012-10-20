package com.hannah.nomophobia.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.StatisticsSingleton;
import com.hannah.nomophobia.utility.TimeFomatUtility;

public class StatisticsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.statistics, container, false);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTextFields();
	}

	private void setTextFields() {
		int phoneChecks = StatisticsSingleton.getPhoneChecks();
		String checksIn24Hours = TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(phoneChecks) + " " + getResources().getQuantityString(R.plurals.time, phoneChecks);
		((TextView) getView().findViewById(R.id.checks_in_timeperiod)).setText(checksIn24Hours);

		String timeperiod = String.format(getString(R.string.in_the_last_timeperiod), TimeFomatUtility.displayClosestHour(StatisticsSingleton.getLongestTimeAge()));
		((TextView) getView().findViewById(R.id.timeperiod)).setText(timeperiod);

		String averageCheckTime = TimeFomatUtility.formatTime(getActivity(), StatisticsSingleton.getAverageIgnoreTime());
		((TextView) getView().findViewById(R.id.average_check_time)).setText(averageCheckTime);
		
		String longestIgnoreTime = TimeFomatUtility.formatTime(getActivity(), StatisticsSingleton.getLongestIgnoreTime());
		((TextView) getView().findViewById(R.id.longest_ignore_time)).setText(longestIgnoreTime);
	}
}
