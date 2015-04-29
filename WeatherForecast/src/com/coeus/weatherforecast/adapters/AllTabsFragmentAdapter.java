package com.coeus.weatherforecast.adapters;

import com.coeus.weatherforecast.fragments.DashBoardFragment;
import com.coeus.weatherforecast.fragments.MajorCityListFragment;
import com.coeus.weatherforecast.fragments.MapLocationFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AllTabsFragmentAdapter extends FragmentPagerAdapter {

	public AllTabsFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top DashBoard Fragment activity
			return new DashBoardFragment();
		case 1:
			// Major City List fragment activity
			return new MajorCityListFragment();
		case 2:
			// Map fragment activity
			return new MapLocationFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
