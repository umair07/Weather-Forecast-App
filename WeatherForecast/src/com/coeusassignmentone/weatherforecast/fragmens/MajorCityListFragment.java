package com.coeusassignmentone.weatherforecast.fragmens;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.coeusassignmentone.weatherforecast.R;
import com.coeusassignmentone.weatherforecast.adapters.MajorCitiesListAdapter;
import com.coeusassignmentone.weatherforecast.datamodel.CitiesWeatherDetailsModel;
import com.coeusassignmentone.weatherforecast.services.CitiesWeatherUpdateService;

public class MajorCityListFragment extends Fragment {

	ListView listView_MajorCities_Details;
	View rootView;
	MajorCitiesListAdapter majorCitiesListAdapter;
	ArrayList<String> citiesNameList;
	 ArrayList<CitiesWeatherDetailsModel> weatherCitiesData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_major_city_list, container, false);
		loadUIComponents();
		try
		{
		citiesNameList = new ArrayList<String>();
		citiesNameList.add("Lahore");
		citiesNameList.add("Islamabad");
		citiesNameList.add("Karachi");
		citiesNameList.add("Faisalabad");
		citiesNameList.add("Rawalpindi");
		citiesNameList.add("Multan");
		citiesNameList.add("Sialkot");
		weatherCitiesData = new ArrayList<CitiesWeatherDetailsModel>();
		getActivity().registerReceiver(updateCitiesDataBroadCastReceiver,
				new IntentFilter("majorcitiesupdate"));
		for (int i = 0; i < citiesNameList.size(); i++) {
			Intent startWeatherService = new Intent(getActivity(),CitiesWeatherUpdateService.class);
			startWeatherService.putExtra("cityname", citiesNameList.get(i));
			getActivity().startService(startWeatherService);

		}
		} catch (Exception e) {
			e.getStackTrace();
		}
//		majorCitiesListAdapter = new MajorCitiesListAdapter(weatherCitiesData, getActivity());
//		listView_MajorCities_Details.setAdapter(majorCitiesListAdapter);

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
	private final BroadcastReceiver updateCitiesDataBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				CitiesWeatherDetailsModel citiesWeatherDetailsModel = new CitiesWeatherDetailsModel();
				citiesWeatherDetailsModel.setCityName(intent.getStringExtra("city"));
				citiesWeatherDetailsModel.setTempratureValue(intent.getStringExtra("temp"));
				citiesWeatherDetailsModel.setWeatherType(intent.getStringExtra("text"));
				weatherCitiesData.add(citiesWeatherDetailsModel);

					
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try
			{
			majorCitiesListAdapter = new MajorCitiesListAdapter(citiesNameList,weatherCitiesData, getActivity());
			listView_MajorCities_Details.setAdapter(majorCitiesListAdapter);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		
		
	};


}
