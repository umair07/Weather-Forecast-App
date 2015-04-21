package com.coeusassignmentone.weatherforecast.fragmens;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coeusassignmentone.weatherforecast.R;
import com.coeusassignmentone.weatherforecast.backgroundservices.WeatherUpdateAsyncTask;

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
	private static String tempratureValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		loadUIComponents();
		registerClickListeners();


		weatherUpdateAsyncTask = new WeatherUpdateAsyncTask(getActivity());

		if(isConnectingToInternet())
		{
			weatherUpdateAsyncTask.execute("Lahore");

		}
		else
		{
			Toast.makeText(getActivity(), internetMessage, Toast.LENGTH_SHORT).show();
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
				convertTemprature();
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			break;	
		case R.id.imageView_dashboard_refresh_data:

			break;
		case R.id.imageView_dashboard_sreach:
			

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
			break;
		default:
			break;
		}

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		getActivity().unregisterReceiver(updateWeatherDataBroadCastReceiver);
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
					tempratureValue = intent.getStringExtra("temp") +TEMP_SYM_F;
					textView_dashboard_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);
				}
				else
				{
					textView_dashboard_temprature.setText("N/A");


				}
				if(!intent.getStringExtra("temp").equals(""))
				{
					textView_dashboard_lowest_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);

				}
				else
				{
					textView_dashboard_lowest_temprature.setText("N/A");

				}
				if(!intent.getStringExtra("temp").equals(""))
				{
					textView_dashboard_highest_temprature.setText(intent.getStringExtra("temp") +TEMP_SYM_F);

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
	
	public void convertTemprature()
	{
		if(tempratureValue.contains("F"))
		{
			imageView_dashboard_select_scale.setImageResource(R.drawable.selector_btn_temprature_c);
			
			
			
			textView_dashboard_temprature.setText("" + TEMP_SYM_C);
			textView_dashboard_highest_temprature.setText("" + TEMP_SYM_C); 
			textView_dashboard_lowest_temprature.setText("" + TEMP_SYM_C); 
		}
		else if(tempratureValue.contains("C"))
		{
			imageView_dashboard_select_scale.setImageResource(R.drawable.selector_btn_temprature_f);
			textView_dashboard_temprature.setText("" + TEMP_SYM_C);
			textView_dashboard_highest_temprature.setText("" + TEMP_SYM_C); 
			textView_dashboard_lowest_temprature.setText("" + TEMP_SYM_C); 
		}
		
		
	}

}
