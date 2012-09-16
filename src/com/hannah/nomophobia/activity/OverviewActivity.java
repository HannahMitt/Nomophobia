package com.hannah.nomophobia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.utility.DatabaseUtility;
import com.hannah.nomophobia.utility.GraphUtility;
import com.hannah.nomophobia.utility.TimeFomatUtility;
import com.jjoe64.graphview.GraphView;

public class OverviewActivity extends Activity {

	private long mCurrentTimeInMillis;
	private int mPhoneChecks;
	private double mAverageIgnoreTime;
	private long mLongestTimeAgoMillis;
	
	private GraphView mGraphView;

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
		mPhoneChecks = DatabaseUtility.phoneChecksInTimePeriod(this, mCurrentTimeInMillis, TimeFomatUtility.MILLIS_IN_A_DAY);
		mAverageIgnoreTime = DatabaseUtility.averageIgnoreDurationInTimePeriod(this, mCurrentTimeInMillis, TimeFomatUtility.MILLIS_IN_A_DAY);

		long oldestCheckValue = DatabaseUtility.oldestCheckValue(this);
		if (oldestCheckValue > 0) {
			mLongestTimeAgoMillis = mCurrentTimeInMillis - oldestCheckValue;
		}

		setTextFields();
		showGraph();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DatabaseUtility.clearOldData(this, mCurrentTimeInMillis, TimeFomatUtility.MILLIS_IN_A_DAY * 2);
	}

	private void setTextFields() {
		String checksIn24Hours = TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(mPhoneChecks) + " " + getResources().getQuantityString(R.plurals.time, mPhoneChecks);
		((TextView) findViewById(R.id.checks_in_timeperiod)).setText(checksIn24Hours);

		String timeperiod = String.format(getString(R.string.in_the_last_timeperiod), TimeFomatUtility.displayClosestHour(mLongestTimeAgoMillis));
		((TextView) findViewById(R.id.timeperiod)).setText(timeperiod);

		String averageCheckTime = TimeFomatUtility.formatTime(this, mAverageIgnoreTime);
		((TextView) findViewById(R.id.average_check_time)).setText(averageCheckTime);
	}

	private void showGraph() {

		LinearLayout layout = (LinearLayout) findViewById(R.id.overview_layout);
		
		if(mGraphView != null){
			layout.removeView(mGraphView);
		}
		
		mGraphView = GraphUtility.getGraphOfTimes(this, mCurrentTimeInMillis, TimeFomatUtility.MILLIS_IN_12_HOURS);

		if (mGraphView != null) {
			layout.addView(mGraphView);
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

		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String getShareMessage() {
		String checksIn24Hours = TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(mPhoneChecks) + " " + getResources().getQuantityString(R.plurals.time, mPhoneChecks);

		String averageTime = TimeFomatUtility.formatTime(this, mAverageIgnoreTime);
		String timePeriod = TimeFomatUtility.displayClosestHour(mLongestTimeAgoMillis);
		return String.format(getString(R.string.share_message), checksIn24Hours, timePeriod, averageTime);
	}
}
