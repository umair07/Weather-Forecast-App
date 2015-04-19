package com.coeusassignmentone.weatherforecast.fragmens;

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

public class DashBoardFragment extends Fragment implements OnClickListener{

	View rootView;
	TextView textView_dashboard_date, textView_dashboard_time,textView_dashboard_city,
	textView_dashboard_temprature,textView_dashboard_highest_temprature,textView_dashboard_lowest_temprature,
	textView_dashboard_wind_speed,textView_dashboard_pressuer;
	EditText editText_dashboard_sreach_city;
	ImageView imageView_dashboard_timeticker, imageView_dashboard_select_scale, 
	imageView_dashboard_refresh_data,imageView_dashboard_sreach;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		loadUIComponents();
		registerClickListeners();
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
	}

	// Register Click Listeners
	public void registerClickListeners()
	{
		rootView.setOnClickListener(this);
		imageView_dashboard_timeticker.setOnClickListener(this);
		imageView_dashboard_select_scale.setOnClickListener(this);
		imageView_dashboard_refresh_data.setOnClickListener(this);
		imageView_dashboard_refresh_data.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageView_dashboard_timeticker:

			break;
		case R.id.imageView_dashboard_select_scale:

			break;	
		case R.id.imageView_dashboard_refresh_data:

			break;
		case R.id.imageView_dashboard_sreach:

			break;
		default:
			break;
		}

	}
}
