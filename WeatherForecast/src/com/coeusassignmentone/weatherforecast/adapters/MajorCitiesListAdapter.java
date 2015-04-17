package com.coeusassignmentone.weatherforecast.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coeusassignmentone.weatherforecast.R;

public class MajorCitiesListAdapter extends BaseAdapter {

	private ArrayList<String> citiesNameList = new ArrayList<String>();
	private Context contextMajorCitiesListAdapter;
	private LayoutInflater inflater;

	public MajorCitiesListAdapter(ArrayList<String> citiesNameList, Context contextMajorCitiesListAdapter
			) {

		this.citiesNameList = citiesNameList;
		this.contextMajorCitiesListAdapter = contextMajorCitiesListAdapter;

		inflater = (LayoutInflater)contextMajorCitiesListAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return citiesNameList.size();
	}

	@Override
	public Object getItem(int position) {

		return citiesNameList.get(position);
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

		adapterCityName.setText(citiesNameList.get(position));
		adapterTempratureValue.setText("16f");
		adapterTempratureImage.setBackground(contextMajorCitiesListAdapter.getResources().getDrawable(R.drawable.rain_l));
		return view;
	}


}


