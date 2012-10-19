package com.hannah.nomophobia.provider;

import android.content.Context;

import com.hannah.nomophobia.utility.DatabaseUtility;
import com.hannah.nomophobia.utility.TimeFomatUtility;

public class StatisticsSingleton {

	private long mCurrentTimeInMillis;
	private int mPhoneChecks;
	private double mAverageIgnoreTime;
	private long mLongestTimeAgoMillis;

	private static StatisticsSingleton mStatisticsSingleton;

	private static StatisticsSingleton getStatisticsSingleton() {
		if (mStatisticsSingleton == null)
			mStatisticsSingleton = new StatisticsSingleton();

		return mStatisticsSingleton;
	}

	public static void updateStatistics(Context context) {
		getStatisticsSingleton().updateStatisticsHelper(context);
	}
	
	public void updateStatisticsHelper(Context context) {
		mCurrentTimeInMillis = System.currentTimeMillis();
		mPhoneChecks = DatabaseUtility.phoneChecksInTimePeriod(context, mCurrentTimeInMillis, TimeFomatUtility.MILLIS_IN_A_DAY);
		mAverageIgnoreTime = DatabaseUtility.averageIgnoreDurationInTimePeriod(context, mCurrentTimeInMillis, TimeFomatUtility.MILLIS_IN_A_DAY);

		long oldestCheckValue = DatabaseUtility.oldestCheckValue(context);
		if (oldestCheckValue > 0) {
			mLongestTimeAgoMillis = mCurrentTimeInMillis - oldestCheckValue;
		}
	}
	
	public static int getPhoneChecks(){
		return getStatisticsSingleton().mPhoneChecks;
	}
	
	public static double getAverageIgnoreTime(){
		return getStatisticsSingleton().mAverageIgnoreTime;
	}
	
	public static long getLongestTimeAge(){
		return getStatisticsSingleton().mLongestTimeAgoMillis;
	}
	
	public static long getCurrentTime(){
		return getStatisticsSingleton().mCurrentTimeInMillis;
	}
}
