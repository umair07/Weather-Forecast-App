package com.coeusassignmentone.weatherforecast.services;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.coeusassignmentone.weatherforecast.datamodel.CitiesWeatherDetailsModel;
import com.coeusassignmentone.weatherforecast.datamodel.WeatherDetailsModel;
import com.coeusassignmentone.weatherforecast.handlers.ServiceHandler;

public class CitiesWeatherUpdateService extends IntentService{

	//	private ArrayList<Temperature> weatherinfo;
	public static final String ACTION_MyIntentService = "IntentService";
	private JSONObject 	jsonObject;
	//	private Temperature weatherTemperatures;
	public CitiesWeatherUpdateService() {
		super("CitiesWeatherUpdateService");
		// TODO Auto-generated constructor stub


	}
	/*Handler handler = new Handler();
	Runnable runnable = new Runnable() {
	        public void run() {
	            sendBroadcast();
	        }
	};*/

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		updateAdapterItem(intent.getStringExtra("cityname"));

		//runnable.run();
	}

	private void updateAdapterItem(String city)
	{
		ServiceHandler serviceHandler;
		String jasonResponse = null;

		try{
			String apiUrl="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%2Cnull%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
			serviceHandler = new ServiceHandler();
			if(isConnectingToInternet())
			{
				jasonResponse = serviceHandler.makeServiceCall(
						apiUrl,
						ServiceHandler.GET);
			}


			jsonObject = new JSONObject(jasonResponse);
			JSONObject query =jsonObject.getJSONObject("query");
			JSONObject results = query.getJSONObject("results");
			JSONObject channel =results.getJSONObject("channel");
			JSONObject location =channel.getJSONObject("location");
			JSONObject item =channel.getJSONObject("item");
			JSONObject condition =item.getJSONObject("condition");
			JSONObject wind =channel.getJSONObject("wind");
			JSONObject atmosphere =channel.getJSONObject("atmosphere");

			try {
				Intent sendWeatherDailyUpdate = new Intent("weatherdailyupdate");
				sendWeatherDailyUpdate.putExtra("city",location.getString("city"));
				sendWeatherDailyUpdate.putExtra("temp",condition.getString("temp"));
				sendWeatherDailyUpdate.putExtra("text",condition.getString("text"));
				sendBroadcast(sendWeatherDailyUpdate);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}	
		catch(Exception e)
		{
			e.getMessage();
		}

	}


	// check connectivity
	public boolean isConnectingToInternet()
	{
		ConnectivityManager cm =
				(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
