package com.hannah.nomophobia.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hannah.nomophobia.R;
import com.hannah.nomophobia.provider.StatisticsSingleton;
import com.hannah.nomophobia.utility.GraphUtility;
import com.hannah.nomophobia.utility.TimeFomatUtility;
import com.jjoe64.graphview.GraphView;

public class GraphFragment extends Fragment {

	private GraphView mGraphView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.graph, container, false);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		showGraph();
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
