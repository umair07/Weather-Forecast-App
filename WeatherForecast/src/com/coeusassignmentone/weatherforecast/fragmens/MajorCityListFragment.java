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
	static ArrayList<CitiesWeatherDetailsModel> weatherCitiesData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_major_city_list, container, false);
		loadUIComponents();
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
				new IntentFilter("weatherdailyupdate"));
		for (int i = 0; i < citiesNameList.size(); i++) {
			Intent startWeatherService = new Intent(getActivity(),CitiesWeatherUpdateService.class);
			startWeatherService.putExtra("cityname", citiesNameList.get(i));
			getActivity().startService(startWeatherService);

		}
		majorCitiesListAdapter = new MajorCitiesListAdapter(weatherCitiesData, getActivity());
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
			
			//			try {
			//				adapterCityName.setText(intent.getStringExtra());
			//				adapterTempratureValue.setText(intent.getStringExtra());
			//
			//				if(!intent.getStringExtra("text").equals(""))
			//				{
			//					String  input = intent.getStringExtra("text");
			//					input = input.replace(" ", "");
			//					try {
			//						if(input.toLowerCase().equals("brokenclouds"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.broken_clouds_l);
			//
			//						} else if(input.toLowerCase().equals("clearsky"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.clear_sky_l);
			//
			//						}else if(input.toLowerCase().equals("fewclouds"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.few_clouds_l);
			//
			//						}else if(input.toLowerCase().equals("mist"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.mist_l);
			//
			//						}else if(input.toLowerCase().equals("rain"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.rain_l);
			//
			//						}else if(input.toLowerCase().equals("scatteredclouds"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.scattered_clouds_l);
			//
			//						}else if(input.toLowerCase().equals("shower"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.shower_l);
			//
			//						}else if(input.toLowerCase().equals("snow"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.snow_l);
			//
			//						}else if(input.toLowerCase().equals("thunderstorm"))
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.thunderstorm_l);
			//
			//						}
			//						else
			//						{
			//							adapterTempratureImage.setImageResource(R.drawable.clear_sky_l);
			//						}
			//					} catch (Exception e) {
			//						// TODO: handle exception
			//					}
			//
			//				}
			//			} catch (Exception e) {
			//				// TODO: handle exception
			//			}
		}
	};


}
