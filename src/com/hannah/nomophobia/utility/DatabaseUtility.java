package com.hannah.nomophobia.utility;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;

import com.hannah.nomophobia.provider.DurationsContentProvider;

public class DatabaseUtility {

	public static double averageIgnoreDurationInTimePeriod(Context context, long currentTimeMillis, long timePeriod) {
		ContentProviderClient client = context.getContentResolver().acquireContentProviderClient(DurationsContentProvider.AUTHORITY);
		DurationsContentProvider durationsContentProvider = (DurationsContentProvider) client.getLocalContentProvider();

		String whereClause = DurationsContentProvider.Contract.Columns.TIME + " > " + (currentTimeMillis - timePeriod);
		double sumColumn = durationsContentProvider.sumColumn(DurationsContentProvider.Contract.Columns.DURATION, whereClause);

		return sumColumn / phoneChecksInTimePeriod(context, currentTimeMillis, timePeriod);
	}
	
	public static int phoneChecksInTimePeriod(Context context, long currentTimeMillis, long timePeriod) {
		return cursorForTimePeriod(context, currentTimeMillis, timePeriod).getCount();
	}
	
	public static long oldestCheckValue(Context context){
		ContentProviderClient client = context.getContentResolver().acquireContentProviderClient(DurationsContentProvider.AUTHORITY);
		DurationsContentProvider durationsContentProvider = (DurationsContentProvider) client.getLocalContentProvider();

		long minColumn = durationsContentProvider.minColumn(DurationsContentProvider.Contract.Columns.TIME, DurationsContentProvider.Contract.Columns.INDEX_TIME);

		return minColumn;
	}
	
	public static long longestIgnoreValue(Context context){
		ContentProviderClient client = context.getContentResolver().acquireContentProviderClient(DurationsContentProvider.AUTHORITY);
		DurationsContentProvider durationsContentProvider = (DurationsContentProvider) client.getLocalContentProvider();

		long maxColumn = durationsContentProvider.maxColumn(DurationsContentProvider.Contract.Columns.DURATION, DurationsContentProvider.Contract.Columns.INDEX_TIME);

		return maxColumn;
	}
	
	public static Cursor cursorForTimePeriod(Context context, long currentTimeMillis, long timePeriod) {
		String selectionTime = String.valueOf(currentTimeMillis - timePeriod);
		String selection = DurationsContentProvider.Contract.Columns.TIME + " > ?";

		return context.getContentResolver().query(DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[] { selectionTime }, null);
	}
	
	public static void clearOldData(Context context, long currentTimeMillis, long timePeriod){
		String selectionTime = String.valueOf(currentTimeMillis - timePeriod);
		String where = DurationsContentProvider.Contract.Columns.TIME + " < ?";
		
		context.getContentResolver().delete(DurationsContentProvider.Contract.CONTENT_URI, where, new String[] {selectionTime});
	}
}
