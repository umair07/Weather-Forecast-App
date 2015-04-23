package com.coeusassignmentone.weatherforecast.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coeusassignmentone.weatherforecast.R;
import com.coeusassignmentone.weatherforecast.datamodel.CitiesWeatherDetailsModel;

public class MajorCitiesListAdapter extends BaseAdapter {

	private ArrayList<CitiesWeatherDetailsModel> weatherCitiesData = new ArrayList<CitiesWeatherDetailsModel>();
	private ArrayList<String> citiesNameList= new ArrayList<String>();
	private Context contextMajorCitiesListAdapter;
	private LayoutInflater inflater;

	public MajorCitiesListAdapter(ArrayList<String> citiesNameList, ArrayList<CitiesWeatherDetailsModel> weatherCitiesData, Context contextMajorCitiesListAdapter
			) {

		this.weatherCitiesData = weatherCitiesData;
		this.contextMajorCitiesListAdapter = contextMajorCitiesListAdapter;
		this.citiesNameList = citiesNameList;
		inflater = (LayoutInflater)contextMajorCitiesListAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return weatherCitiesData.size();
	}

	@Override
	public Object getItem(int position) {

		return weatherCitiesData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}


	@SuppressLint({ "ViewHolder", "NewApi" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		view =  inflater.inflate(R.layout.major_cities_list_adapter, null);

		TextView adapterCityName = (TextView)view.findViewById(R.id.textView_adapter_cities_name);
		TextView adapterTempratureValue = (TextView)view.findViewById(R.id.textView_adapter_temprature_value);
		ImageView adapterTempratureImage = (ImageView)view.findViewById(R.id.imageView_adapter_temprature_type);
		ProgressBar progressBarTempratureValue  = (ProgressBar)view.findViewById(R.id.progressBar_tempratureValue);
		ProgressBar progressBarTempratureImage = (ProgressBar)view.findViewById(R.id.progressBar_temprature_type);
	try {
	
		adapterCityName.setText(citiesNameList.get(position));
		
		if(!weatherCitiesData.get(position).getTempratureValue().equals(""))
		{
			
			progressBarTempratureValue.setVisibility(View.GONE);
			adapterTempratureValue.setVisibility(View.VISIBLE);
			adapterTempratureValue.setText(weatherCitiesData.get(position).getTempratureValue());

		}
		
	
		if(!weatherCitiesData.get(position).getWeatherType().equals(""))
		{
			String  input = weatherCitiesData.get(position).getWeatherType();
			input = input.replace(" ", "");
			progressBarTempratureImage.setVisibility(View.GONE);
			adapterTempratureImage.setVisibility(View.VISIBLE);
			if(input.toLowerCase().equals("brokenclouds"))
			{
				
				adapterTempratureImage.setImageResource(R.drawable.broken_clouds_l);

			} else if(input.toLowerCase().equals("clearsky"))
			{
				adapterTempratureImage.setImageResource(R.drawable.clear_sky_l);

			}else if(input.toLowerCase().equals("fewclouds"))
			{
				adapterTempratureImage.setImageResource(R.drawable.few_clouds_l);

			}else if(input.toLowerCase().equals("mist"))
			{
				adapterTempratureImage.setImageResource(R.drawable.mist_l);

			}else if(input.toLowerCase().equals("rain"))
			{
				adapterTempratureImage.setImageResource(R.drawable.rain_l);

			}else if(input.toLowerCase().equals("scatteredclouds"))
			{
				adapterTempratureImage.setImageResource(R.drawable.scattered_clouds_l);

			}else if(input.toLowerCase().equals("shower"))
			{
				adapterTempratureImage.setImageResource(R.drawable.shower_l);

			}else if(input.toLowerCase().equals("snow"))
			{
				adapterTempratureImage.setImageResource(R.drawable.snow_l);

			}else if(input.toLowerCase().equals("thunderstorm"))
			{
				adapterTempratureImage.setImageResource(R.drawable.thunderstorm_l);

			}
			else
			{
				adapterTempratureImage.setImageResource(R.drawable.clear_sky_l);
			}

		}
		
		} catch (Exception e) {
			e.getStackTrace();
		}
		return view;
	}
}






