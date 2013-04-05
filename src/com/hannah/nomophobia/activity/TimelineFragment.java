package com.hannah.nomophobia.activity;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannah.nomophobia.R;

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
			timecell = getCellView(timeCalc);
			timelineLayout.addView(timecell);

			timeCalc.add(Calendar.HOUR, 1);
		}
		
		timecell = getCellView(timeCalc);
		
		int timewidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, timeCalc.get(Calendar.MINUTE) * 2, getResources().getDisplayMetrics());
		timecell.setLayoutParams(new LayoutParams(timewidth, getResources().getDimensionPixelSize(R.dimen.time_cell_height)));
		timelineLayout.addView(timecell);
	}

	private View getCellView(Calendar timeCalc) {
		View timecell = getLayoutInflater().inflate(R.layout.time_cell, null);
		TextView hourText = (TextView) timecell.findViewById(R.id.hour_text);

		hourText.setText(timeCalc.get(Calendar.HOUR) + ":00");
		return timecell;
	}
}
