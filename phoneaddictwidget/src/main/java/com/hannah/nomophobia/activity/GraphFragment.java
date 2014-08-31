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
        mClockVisual = new ClockVisual();
        mCircleGraphView.setImageDrawable(mClockVisual);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selectionTime = String.valueOf(StatisticsSingleton.getCurrentTime() - TimeFomatUtility.MILLIS_IN_12_HOURS);
        String selection = DurationsContentProvider.Contract.Columns.TIME + " > ?";
        return new CursorLoader(getActivity(), DurationsContentProvider.Contract.CONTENT_URI, null, selection, new String[]{selectionTime}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mClockVisual.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mClockVisual.setCursor(null);
    }
}
