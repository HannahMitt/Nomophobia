package com.hannah.nomophobia.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.StatisticsSingleton;
import com.hannah.nomophobia.utility.DatabaseUtility;
import com.hannah.nomophobia.utility.TimeFomatUtility;

public class OverviewActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);
		
		Typeface typeface = Typeface.createFromAsset(getAssets(), "Market_Deco.ttf");
		((TextView)findViewById(R.id.nomophobia)).setTypeface(typeface, Typeface.BOLD);
		
		typeface = Typeface.createFromAsset(getAssets(), "Ubuntu-Title.ttf");
		((TextView)findViewById(R.id.definition)).setTypeface(typeface, Typeface.BOLD);
		
		setUpTabs();
	}

	private void setUpTabs() {
		final Button statsTab = (Button) findViewById(R.id.stats_tab);
		final Button graphTab = (Button) findViewById(R.id.graph_tab);
		statsTab.setSelected(true);

		final ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new NomophobiaFragmentAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					graphTab.setSelected(false);
					statsTab.setSelected(true);
				} else {
					graphTab.setSelected(true);
					statsTab.setSelected(false);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		statsTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pager.setCurrentItem(0);
			}
		});

		graphTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pager.setCurrentItem(1);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatisticsSingleton.updateStatistics(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DatabaseUtility.clearOldData(this, StatisticsSingleton.getCurrentTime(), (long) (TimeFomatUtility.MILLIS_IN_A_DAY * 1.5));
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
		int phoneChecks = StatisticsSingleton.getPhoneChecks();
		String checksIn24Hours = TimeFomatUtility.AVERAGE_DOUBLE_FORMAT.format(phoneChecks) + " " + getResources().getQuantityString(R.plurals.time, phoneChecks);

		String averageTime = TimeFomatUtility.formatTime(this, StatisticsSingleton.getAverageIgnoreTime());
		String timePeriod = TimeFomatUtility.displayClosestHour(StatisticsSingleton.getLongestTimeAge());
		return String.format(getString(R.string.share_message), checksIn24Hours, timePeriod, averageTime);
	}

	class NomophobiaFragmentAdapter extends FragmentPagerAdapter {

		public NomophobiaFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			if (arg0 == 0) {
				return Fragment.instantiate(OverviewActivity.this, StatisticsFragment.class.getName());
			} else {
				return Fragment.instantiate(OverviewActivity.this, GraphFragment.class.getName());
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
