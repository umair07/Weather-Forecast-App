package com.coeusassignmentone.weatherforecast.fragmens;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.coeusassignmentone.weatherforecast.R;
import com.coeusassignmentone.weatherforecast.adapters.MajorCitiesListAdapter;

public class MajorCityListFragment extends Fragment {

	ListView listView_MajorCities_Details;
	View rootView;
	MajorCitiesListAdapter majorCitiesListAdapter;
	ArrayList<String> citiesNameList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 rootView = inflater.inflate(R.layout.fragment_major_city_list, container, false);
		 loadUIComponents();
		 citiesNameList = new ArrayList<String>();
		 citiesNameList.add("Lahore");
		 citiesNameList.add("Islamabad");
		 citiesNameList.add("Karachi");
		 majorCitiesListAdapter = new MajorCitiesListAdapter(citiesNameList, getActivity());
		listView_MajorCities_Details.setAdapter(majorCitiesListAdapter);
		 
		return rootView;
	}
	
	// Initialize all UI components
	public void loadUIComponents()
	{
		listView_MajorCities_Details = (ListView)rootView.findViewById(R.id.listView_MajorCities);
	}
	
	// Register Click Listeners
	public void registerClickListeners()
	{
		
	}

}
