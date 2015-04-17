package com.coeusassignmentone.weatherforecast.fragmens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coeusassignmentone.weatherforecast.R;

public class DashBoardFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

		return rootView;
	}
	// Initialize all UI components
	public void loadUIComponents()
	{

	}

	// Register Click Listeners
	public void registerClickListeners()
	{

	}
}
