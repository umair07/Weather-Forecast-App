package com.coeus.weatherforecast.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.coeus.weatherforecast.R;
import com.coeus.weatherforecast.adapters.MajorCitiesListAdapter;
import com.coeus.weatherforecast.datamodel.CitiesWeatherDetailsModel;
import com.coeus.weatherforecast.services.CitiesWeatherUpdateService;

public class MajorCityListFragment extends Fragment implements OnClickListener {

	ListView listView_MajorCities_Details;
	View rootView;
	ImageView refreshMajorCitiesData;
	MajorCitiesListAdapter majorCitiesListAdapter;
	ArrayList<String> citiesNameList;
	ArrayList<CitiesWeatherDetailsModel> weatherCitiesData;
	protected final static String TEMP_SYM_F = (char) 0x00B0 + "F";
	Intent startWeatherService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_major_city_list, container, false);
		loadUIComponents();
		registerClickListeners();
		
		//set major cities list
		updateMajorCitiesLit();
		getActivity().registerReceiver(updateCitiesDataBroadCastReceiver,
				new IntentFilter("majorcitiesupdate"));
		return rootView;
	}

	private void updateMajorCitiesLit() {
		// TODO Auto-generated method stub
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
		
		for (int i = 0; i < citiesNameList.size(); i++) {
			 startWeatherService = new Intent(getActivity(),CitiesWeatherUpdateService.class);
			startWeatherService.putExtra("cityname", citiesNameList.get(i));
			getActivity().startService(startWeatherService);

		}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// Initialize all UI components
	public void loadUIComponents()
	{
		
		listView_MajorCities_Details = (ListView)rootView.findViewById(R.id.listView_MajorCities);
		refreshMajorCitiesData = (ImageView)rootView.findViewById(R.id.imageView_dashboard_refresh_majorcities_data);
	}

	// Register Click Listeners
	public void registerClickListeners()
	{
		refreshMajorCitiesData.setOnClickListener(this);
	}
	private final BroadcastReceiver updateCitiesDataBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				CitiesWeatherDetailsModel citiesWeatherDetailsModel = new CitiesWeatherDetailsModel();
				citiesWeatherDetailsModel.setCitiesWeatheCityName(intent.getStringExtra("city"));
				if(intent.getStringExtra("temp").equals("N/A"))
				{
					citiesWeatherDetailsModel.setCitiesWeatheTempratureValue(intent.getStringExtra("temp"));

				}
				else
				{
					citiesWeatherDetailsModel.setCitiesWeatheTempratureValue(intent.getStringExtra("temp") + TEMP_SYM_F);

				}
				citiesWeatherDetailsModel.setCitiesWeatheWeatherType(intent.getStringExtra("text"));
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView_dashboard_refresh_majorcities_data:
			try
			{
				weatherCitiesData.clear();
			for (int i = 0; i < citiesNameList.size(); i++) {
				Intent startWeatherService = new Intent(getActivity(),CitiesWeatherUpdateService.class);
				startWeatherService.putExtra("cityname", citiesNameList.get(i));
				getActivity().startService(startWeatherService);

			}
			} catch (Exception e) {
				e.getStackTrace();
			}
			break;
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			getActivity().unregisterReceiver(updateCitiesDataBroadCastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

}
