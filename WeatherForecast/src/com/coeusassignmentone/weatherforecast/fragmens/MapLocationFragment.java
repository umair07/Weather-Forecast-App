package com.coeusassignmentone.weatherforecast.fragmens;

import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coeusassignmentone.weatherforecast.R;
import com.coeusassignmentone.weatherforecast.location_manager.GPSTracker;
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		View rootView = inflater.inflate(R.layout.fragment_map_location, container, false);
		
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

		try
		{
			Marker currentMarker = googleMap.addMarker(new MarkerOptions()
			.position(current_Location_LatLong)
			.title(getCityName(current_Location_LatLong.latitude , current_Location_LatLong.longitude))
			.snippet(getCityName(current_Location_LatLong.latitude , current_Location_LatLong.longitude) +" is hot.")
			.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));

			// Move the camera instantly to Pakistan with a zoom of 15.
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_Location_LatLong, 15));

			// Zoom in, animating the camera.
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);
			addMarker(31.4292,73.0789,"Faislabad","Faislabad is hot too.",BitmapDescriptorFactory.HUE_RED);
			addMarker(33.7167,73.0667,"Islamabad","Islamabad is hot",BitmapDescriptorFactory.HUE_CYAN);
			addMarker(24.8600,67.0100,"Karachi","Karachi is hot",BitmapDescriptorFactory.HUE_MAGENTA);
			addMarker(33.6000,73.0333,"Rawalpindi","Rawalpindi is hot",BitmapDescriptorFactory.HUE_YELLOW);
			addMarker(30.19787,71.4697,"Multan","Multan is too hot",BitmapDescriptorFactory.HUE_ORANGE);
			addMarker(32.4972,74.5361,"Sialkot","Sialkot is hot",BitmapDescriptorFactory.HUE_ROSE);
		} catch (Exception e) {
			e.getStackTrace();
		}
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



	public void addMarker(double pLatitude, double pLongitude, String pCityName , String pMessage, float hueColor)
	{
		try
		{
			// create marker
			MarkerOptions marker = new MarkerOptions().position(new LatLng(pLatitude, pLongitude)).title("Hello Maps");

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
	public String getCityName(double pLatitude, double pLongitude)
	{
		Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
		String cityName = null;
		try {
			List<Address> addresses = geocoder.getFromLocation(pLatitude, pLongitude, 1);

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
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}
