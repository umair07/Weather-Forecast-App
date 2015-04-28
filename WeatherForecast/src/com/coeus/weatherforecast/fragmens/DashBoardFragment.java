package com.coeus.weatherforecast.fragmens;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coeus.weatherforecast.R;
import com.coeus.weatherforecast.location_manager.GPSTracker;
import com.coeus.weatherforecast.services.WeatherUpdateAsyncTask;

public class DashBoardFragment extends Fragment implements OnClickListener {

	//variables
	View rootView;
	TextView textView_dashboard_date, textView_dashboard_time,textView_dashboard_city,
	textView_dashboard_temprature,textView_dashboard_highest_temprature,textView_dashboard_lowest_temprature,
	textView_dashboard_wind_speed,textView_dashboard_pressuer;
	EditText editText_dashboard_sreach_city;
	ImageView imageView_dashboard_timeticker, imageView_dashboard_select_scale, 
	imageView_dashboard_refresh_data,imageView_dashboard_sreach,imageView_dashboard_wather_symbol;
	WeatherUpdateAsyncTask weatherUpdateAsyncTask;
	private String internetMessage = "No Internet Connection";
	protected final static String TEMP_SYM_C = (char) 0x00B0 + "C";
	protected final static String TEMP_SYM_F = (char) 0x00B0 + "F";
	GPSTracker gpsTracker;
	double latitude,longitude;
	public static double loc_latitude,loc_longitude;
	public static String currentCityTempValue;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		loadUIComponents();
		registerClickListeners();
		try
		{
			gpsTracker = new GPSTracker(getActivity());
			// check if GPS enabled		
			if(gpsTracker.canGetLocation()){

				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
				loc_latitude = latitude;
				loc_longitude = longitude;

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
			weatherUpdateAsyncTask = new WeatherUpdateAsyncTask(getActivity());

			if(isConnectingToInternet())
			{

				weatherUpdateAsyncTask.execute(getCityName(latitude,longitude));

			}
			else
			{
				weatherUpdateAsyncTask.execute("N/A");
				Toast.makeText(getActivity(), internetMessage, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		// setWeatherData() function call
		rootView.getContext().registerReceiver(updateWeatherDataBroadCastReceiver,
				new IntentFilter("weatherdailyupdate"));


		return rootView;
	}
	// Initialize all UI components
	public void loadUIComponents()
	{
		textView_dashboard_date = (TextView)rootView.findViewById(R.id.textView_dashboard_date);
		textView_dashboard_time = (TextView)rootView.findViewById(R.id.textView_dashboard_time);
		textView_dashboard_city = (TextView)rootView.findViewById(R.id.textView_dashboard_city);
		textView_dashboard_temprature = (TextView)rootView.findViewById(R.id.textView_dashboard_temprature);
		textView_dashboard_highest_temprature = (TextView)rootView.findViewById(R.id.textView_dashboard_highest_temprature);
		textView_dashboard_lowest_temprature = (TextView)rootView.findViewById(R.id.textView_dashboard_lowest_temprature);
		textView_dashboard_wind_speed = (TextView)rootView.findViewById(R.id.textView_dashboard_wind_speed);
		textView_dashboard_pressuer = (TextView)rootView.findViewById(R.id.textView_dashboard_pressuer);
		editText_dashboard_sreach_city = (EditText)rootView.findViewById(R.id.editText_dashboard_sreach_city);
		imageView_dashboard_timeticker = (ImageView)rootView.findViewById(R.id.imageView_dashboard_timeticker);
		imageView_dashboard_select_scale = (ImageView)rootView.findViewById(R.id.imageView_dashboard_select_scale);
		imageView_dashboard_refresh_data = (ImageView)rootView.findViewById(R.id.imageView_dashboard_refresh_data);
		imageView_dashboard_sreach = (ImageView)rootView.findViewById(R.id.imageView_dashboard_sreach);
		imageView_dashboard_wather_symbol = (ImageView)rootView.findViewById(R.id.imageView_dashboard_wather_symbol);
	}

	// Register Click Listeners
	public void registerClickListeners()
	{
		rootView.setOnClickListener(this);
		imageView_dashboard_timeticker.setOnClickListener(this);
		imageView_dashboard_select_scale.setOnClickListener(this);
		imageView_dashboard_refresh_data.setOnClickListener(this);
		imageView_dashboard_sreach.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageView_dashboard_timeticker:

			break;
		case R.id.imageView_dashboard_select_scale:
			try {
				
				String[] splitedValuesTemprature = textView_dashboard_temprature.getText().toString().split(""+(char) 0x00B0);
				String tempratureValue = splitedValuesTemprature[0];
				String tempratureUnit = splitedValuesTemprature[1]; 
				SharedPrefStoreData("tempUnit", "F");
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				String lastTempValue = sharedPreferences.getString("lastTempValue", "");
				if(tempratureUnit.contains("F"))
				{
					SharedPrefStoreData("tempUnit", "C");
					convertTemprature(lastTempValue);
				}
				
				else
				{
					
					imageView_dashboard_select_scale.setImageResource(R.drawable.selector_btn_temprature_c);
					textView_dashboard_temprature.setText(lastTempValue);
					textView_dashboard_highest_temprature.setText(lastTempValue); 
					textView_dashboard_lowest_temprature.setText(lastTempValue); 
				}
				
			} catch (Exception e) {
				e.getStackTrace();
			}

			break;	
		case R.id.imageView_dashboard_refresh_data:
			try{
				weatherUpdateAsyncTask = new WeatherUpdateAsyncTask(getActivity());
				if(isConnectingToInternet())
				{

					weatherUpdateAsyncTask.execute(textView_dashboard_city.getText().toString());

				}
				else
				{
					weatherUpdateAsyncTask.execute("N/A");
					Toast.makeText(getActivity(), internetMessage, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
			break;
		case R.id.imageView_dashboard_sreach:
			try
			{
				if(isConnectingToInternet())
				{
					if(!editText_dashboard_sreach_city.getText().toString().equals(""))
					{
						weatherUpdateAsyncTask = new WeatherUpdateAsyncTask(getActivity());
						weatherUpdateAsyncTask.execute(editText_dashboard_sreach_city.getText().toString());
					}
					else
					{
						Toast.makeText(getActivity(), "Enter City Name", Toast.LENGTH_SHORT).show();
					}

				}
				else
				{
					Toast.makeText(getActivity(), internetMessage, Toast.LENGTH_SHORT).show();
				}
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText_dashboard_sreach_city.getWindowToken(), 0);
			} catch (Exception e) {
				e.getStackTrace();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try
		{
			getActivity().unregisterReceiver(updateWeatherDataBroadCastReceiver);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	// check internet connection

	public boolean isConnectingToInternet(){
		ConnectivityManager cm =
				(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	// set weather data to UI components
	@SuppressLint("DefaultLocale")
	private final BroadcastReceiver updateWeatherDataBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {

				textView_dashboard_date.setText("Last Update: "+intent.getStringExtra("lastBuildDate"));
				SharedPrefStoreData("currentCityData", intent.getStringExtra("city") + "**" + intent.getStringExtra("temp"));
					
				if(!intent.getStringExtra("city").equals(""))
				{
					textView_dashboard_city.setText(intent.getStringExtra("city"));

				}
				else
				{
					textView_dashboard_city.setText("N/A");

				}
				if(!intent.getStringExtra("pressure").equals(""))
				{
					textView_dashboard_pressuer.setText(intent.getStringExtra("pressure"));

				}
				else
				{
					textView_dashboard_pressuer.setText("N/A");
				}
				if(!intent.getStringExtra("temp").equals(""))
				{
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(context);
					String tempratureUnit = sharedPreferences.getString("tempUnit", "");
					if(tempratureUnit.equals("C"))
					{
						convertTemprature(intent.getStringExtra("temp") +TEMP_SYM_F);
					}
					else if(tempratureUnit.equals("F"))
					{
						textView_dashboard_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
					}
					else
					{
						textView_dashboard_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
					}

				}
				else
				{
					textView_dashboard_temprature.setText("N/A");


				}
				if(!intent.getStringExtra("temp").equals(""))
				{
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(context);
					String tempratureUnit = sharedPreferences.getString("tempUnit", "");
					SharedPrefStoreData("lastTempValue", intent.getStringExtra("temp") +TEMP_SYM_F);
					currentCityTempValue = intent.getStringExtra("temp");
					if(tempratureUnit.equals("C"))
					{
						convertTemprature(intent.getStringExtra("temp") +TEMP_SYM_F);
					}
					else if(tempratureUnit.equals("F"))
					{
						textView_dashboard_lowest_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
					}
					else
					{
						textView_dashboard_lowest_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
					}

				}
				else
				{
					textView_dashboard_lowest_temprature.setText("N/A");

				}
				if(!intent.getStringExtra("temp").equals(""))
				{
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(context);
					String tempratureUnit = sharedPreferences.getString("tempUnit", "");
					
					if(tempratureUnit.equals("C"))
					{
						convertTemprature(intent.getStringExtra("temp") +TEMP_SYM_F);
					}
					else if(tempratureUnit.equals("F"))
					{
						textView_dashboard_highest_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
					}
					else
					{
						textView_dashboard_highest_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
					}


				}
				else
				{
					textView_dashboard_highest_temprature.setText("N/A");

				}
				if(!intent.getStringExtra("speed").equals(""))
				{
					textView_dashboard_wind_speed.setText(intent.getStringExtra("speed") + " mph");

				}
				else
				{
					textView_dashboard_wind_speed.setText("N/A");

				}
				if(!intent.getStringExtra("text").equals(""))
				{
					String  input = intent.getStringExtra("text");
					input = input.replace(" ", "");
					try {
						if(input.toLowerCase().equals("brokenclouds"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.broken_clouds_l);

						} else if(input.toLowerCase().equals("clearsky"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.clear_sky_l);

						}else if(input.toLowerCase().equals("fewclouds"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.few_clouds_l);

						}else if(input.toLowerCase().equals("mist"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.mist_l);

						}else if(input.toLowerCase().equals("rain"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.rain_l);

						}else if(input.toLowerCase().equals("scatteredclouds"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.scattered_clouds_l);

						}else if(input.toLowerCase().equals("shower"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.shower_l);

						}else if(input.toLowerCase().equals("snow"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.snow_l);

						}else if(input.toLowerCase().equals("thunderstorm"))
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.thunderstorm_l);

						}
						else
						{
							imageView_dashboard_wather_symbol.setImageResource(R.drawable.clear_sky_l);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				else
				{
					String img_Name = "";

				}

				SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
				Calendar c = Calendar.getInstance();
				textView_dashboard_time.setText(df.format(c.getTime()));




			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void convertTemprature(String pTempratureValue)
	{

		String[] splitedValuesTemprature = pTempratureValue.split(""+(char) 0x00B0);
		
		String tempratureValue = splitedValuesTemprature[0];
		
		String tempratureUnit = splitedValuesTemprature[1]; 
		
		try
		{
			if(tempratureUnit.contains("F"))
			{
				double farnhiteTemprature = Double.parseDouble(tempratureValue);
				double celsiusValue = (farnhiteTemprature- 32) * (5 / 9.0);
//				double finalTemprature_C =  (Math.round( celsiusValue * 100.0 ) / 100.0);
				int finalTemprature_C = (int) (Math.round( celsiusValue * 100.0 ) / 100.0);
				imageView_dashboard_select_scale.setImageResource(R.drawable.selector_btn_temprature_f);
				textView_dashboard_temprature.setText(finalTemprature_C + TEMP_SYM_C);
				textView_dashboard_highest_temprature.setText(finalTemprature_C + TEMP_SYM_C); 
				textView_dashboard_lowest_temprature.setText(finalTemprature_C + TEMP_SYM_C); 
			}
			else if(tempratureUnit.contains("C"))
			{
//				double celsuicTemprature = Double.parseDouble(tempratureValue);
//				double franhiteValue =  ((celsuicTemprature * 9 / 5.0) + 32);
//				int finalTemprature_F = (int) (Math.round( franhiteValue * 100.0 ) / 100.0);
//				imageView_dashboard_select_scale.setImageResource(R.drawable.selector_btn_temprature_c);
//				textView_dashboard_temprature.setText(finalTemprature_F + TEMP_SYM_F);
//				textView_dashboard_highest_temprature.setText(finalTemprature_F + TEMP_SYM_F); 
//				textView_dashboard_lowest_temprature.setText(finalTemprature_F + TEMP_SYM_F); 
			}

		} catch (Exception e) {
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
		} catch (Exception e) {
			e.getStackTrace();
		}
		return cityName;
	}


	//save data in prefrences

	public void SharedPrefStoreData(String tag, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(tag, value);
		editor.commit();
	}

}
