package com.coeusassignmentone.weatherforecast.fragmens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coeusassignmentone.weatherforecast.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationFragment extends Fragment {
	static final LatLng latLongPakistan = new LatLng(33.6667,73.1667);
	static final LatLng latLongLahore = new LatLng(31.5497, 74.3436);
	private GoogleMap googleMap;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map_location, container, false);
		googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		Marker markerPakistan = googleMap.addMarker(new MarkerOptions().position(latLongPakistan)
				.title("Pakistan"));
		markerPakistan.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		Marker markerLahore = googleMap.addMarker(new MarkerOptions()
		.position(latLongLahore)
		.title("Lahore")
		.snippet("Lahore is hot")
		.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));

		// Move the camera instantly to Pakistan with a zoom of 15.
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongPakistan, 15));

		// Zoom in, animating the camera.
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		addMarker(31.4292,73.0789,"Faislabad","Faislabad is hot too.");
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



	public void addMarker(double pLatitude, double pLongitude, String pCityName , String pMessage)
	{
		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(pLatitude, pLongitude)).title("Hello Maps");
		
		marker.title(pCityName);
		marker.snippet(pMessage);
		// Changing marker icon
		marker.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

		// adding marker
		googleMap.addMarker(marker);			
	}
}
