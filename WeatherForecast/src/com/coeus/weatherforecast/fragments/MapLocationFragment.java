package com.coeus.weatherforecast.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coeus.weatherforecast.R;
import com.coeus.weatherforecast.location_manager.GPSTracker;
import com.coeus.weatherforecast.services.MapDataUpdateService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationFragment extends Fragment {
	//	static final LatLng latLongPakistan = new LatLng(33.6667,73.1667);
	static LatLng current_Location_LatLong;
	private GoogleMap googleMap;
	GPSTracker gpsTracker;
	View rootView;
	private ArrayList<String> citiesNameList;
	Intent startUpdateMapPinService;
	protected final static String TEMP_SYM_F = (char) 0x00B0 + "F";
	protected final static String TEMP_SYM_C = (char) 0x00B0 + "C";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView != null) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null)
				parent.removeView(rootView);
		}
		try {
			rootView = inflater.inflate(R.layout.fragment_map_location, container, false);
		} catch (InflateException e) {
			//		    	return rootView;
			/* map is already there, just return view as it is */
		}

		//loadMap
		loadMap();

		// Get current location and set data
		setCurrentLocation();


		getActivity().registerReceiver(updateCitiesDataBroadCastReceiver,
				new IntentFilter("mapdrawpin"));

		// Set other cities data
		setMajorCitiesPin();


		return rootView;
	}

	private void loadMap()
	{
		try
		{
			googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

			gpsTracker = new GPSTracker(getActivity());
			// check if GPS enabled		
			if(gpsTracker.canGetLocation()){
				current_Location_LatLong = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

				// \n is for new line
			}else{
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gpsTracker.showSettingsAlert();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private void setCurrentLocation()
	{
		try
		{
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			String seperator = "[**]";
			String tempratureUnit = sharedPreferences.getString("currentCityData", "");
			String[] splitedCityData = tempratureUnit.split(seperator);

			String cityName = splitedCityData[0];

			String tempratureValue = splitedCityData[2]; 

			Marker currentMarker = googleMap.addMarker(new MarkerOptions()
			.position(current_Location_LatLong)
			.title(cityName)
			.snippet(tempratureValue + TEMP_SYM_F + "\n" + convertTemprature(tempratureValue))
			.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));

			// Move the camera instantly to Pakistan with a zoom of 15.
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_Location_LatLong, 15));

			// Zoom in, animating the camera.
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private void setMajorCitiesPin()
	{
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

			for (int i = 0; i < citiesNameList.size(); i++) {
				startUpdateMapPinService = new Intent(getActivity(),MapDataUpdateService.class);
				startUpdateMapPinService.putExtra("cityname", citiesNameList.get(i));
				getActivity().startService(startUpdateMapPinService);

			}
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public void addMarker(double pLatitude, double pLongitude, String pCityName , String pMessage, float hueColor)
	{
		try
		{
			// create marker
			MarkerOptions marker = new MarkerOptions().position(new LatLng(pLatitude, pLongitude)).title("");

			marker.title(pCityName);
			marker.snippet(pMessage);
			// Changing marker icon
			marker.icon(BitmapDescriptorFactory.defaultMarker(hueColor));

			// adding marker
			googleMap.addMarker(marker);	
		}catch (Exception e) {
			e.getStackTrace();
		}
	}
	public String getCityName(LatLng latLng)
	{
		Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
		String cityName = null;
		try {
			List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

			if(addresses != null) {
				Address returnedAddress = addresses.get(0);
				cityName = returnedAddress.getLocality();
			}
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		return cityName;
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {

			getActivity().unregisterReceiver(updateCitiesDataBroadCastReceiver);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	// cities data Recevier
	private final BroadcastReceiver updateCitiesDataBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				addMarker(Double.parseDouble(intent.getStringExtra("lat")),Double.parseDouble(intent.getStringExtra("long")),intent.getStringExtra("city"),intent.getStringExtra("temp")+ TEMP_SYM_F + "\n" + convertTemprature(intent.getStringExtra("temp")),BitmapDescriptorFactory.HUE_RED);
				//				addMarker(33.7167,73.0667,"Islamabad","Islamabad is hot",BitmapDescriptorFactory.HUE_CYAN);
				//				addMarker(24.8600,67.0100,"Karachi","Karachi is hot",BitmapDescriptorFactory.HUE_MAGENTA);
				//				addMarker(33.6000,73.0333,"Rawalpindi","Rawalpindi is hot",BitmapDescriptorFactory.HUE_YELLOW);
				//				addMarker(30.19787,71.4697,"Multan","Multan is too hot",BitmapDescriptorFactory.HUE_ORANGE);
				//				addMarker(32.4972,74.5361,"Sialkot","Sialkot is hot",BitmapDescriptorFactory.HUE_ROSE);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}


	};

	public String convertTemprature(String pTempratureValue)
	{

		String tempInCelcius="";

		try
		{
			double farnhiteTemprature = Double.parseDouble(pTempratureValue);
			double celsiusValue = (farnhiteTemprature- 32) * (5 / 9.0);
			//				double finalTemprature_C =  (Math.round( celsiusValue * 100.0 ) / 100.0);
			int finalTemprature_C = (int) (Math.round( celsiusValue * 100.0 ) / 100.0);
			tempInCelcius = finalTemprature_C + TEMP_SYM_C;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return tempInCelcius;
	}




}
