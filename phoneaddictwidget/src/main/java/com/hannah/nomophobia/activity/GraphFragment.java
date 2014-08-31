package com.hannah.nomophobia.activity;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.DurationsContentProvider;
import com.hannah.nomophobia.provider.StatisticsSingleton;
import com.hannah.nomophobia.utility.TimeFomatUtility;
import com.hannah.nomophobia.view.ClockVisual;

public class GraphFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "GraphFragment";
    private static final int LOADER_ID = 0;

    private long mSelectionTime;

    private ImageView mCircleGraphView;
    private ClockVisual mClockVisual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph, container, false);

        mCircleGraphView = (ImageView) view.findViewById(R.id.graph_circle);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mClockVisual = new ClockVisual(getResources());
        mCircleGraphView.setImageDrawable(mClockVisual);

        mSelectionTime = StatisticsSingleton.getCurrentTime() - ClockVisual.CLOCK_MILLIS;
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = DurationsContentProvider.Contract.Columns.TIME + " > ?";
        return new CursorLoader(getActivity(), DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[]{String.valueOf(mSelectionTime)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mClockVisual.setCursor(data, mSelectionTime);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mClockVisual.setCursor(null, 0);
    }
}
