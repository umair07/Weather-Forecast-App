package com.coeusassignmentone.weatherforecast.backgroundservices;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.coeusassignmentone.weatherforecast.datamodel.WeatherDetailsModel;
import com.coeusassignmentone.weatherforecast.handlers.ServiceHandler;
import com.coeusassignmentone.weatherforecast.listener.UpdateWeatherDetailsListener;

public class WeatherUpdateAsyncTask extends AsyncTask<String, Void, Void>{

	//variables
	private ProgressDialog mProgressDialog;
	private Context context;
	private String apiResponse = "";
	private String apiUrl = "";
	private JSONObject 	jsonObject;
	UpdateWeatherDetailsListener updateWeatherDetailsListener;
	WeatherDetailsModel weatherDetailsModel;
	//constructor
	public WeatherUpdateAsyncTask(Context context) {
		super();
		this.context = context;
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle("Connecting");
		mProgressDialog.setMessage("Please wait ...");
		mProgressDialog.show();
	}
	//background task
	@Override
	protected Void doInBackground(String... params) {
		
		apiUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+params[0]+"%2Cnull%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		ServiceHandler serviceHandler = new ServiceHandler();
		try {
			String jsonResponseString = serviceHandler.makeServiceCall(
					apiUrl,
					ServiceHandler.GET);
			apiResponse = jsonResponseString;
			jsonObject = new JSONObject(apiResponse);
			JSONObject query =jsonObject.getJSONObject("query");
			JSONObject results = query.getJSONObject("results");
			JSONObject channel =results.getJSONObject("channel");
			JSONObject location =channel.getJSONObject("location");
			JSONObject item =channel.getJSONObject("item");
			JSONObject condition =item.getJSONObject("condition");
			JSONObject wind =channel.getJSONObject("wind");
			JSONObject atmosphere =channel.getJSONObject("atmosphere");
			
			weatherDetailsModel =  new WeatherDetailsModel();
			
			weatherDetailsModel.setWeatherDetailCityName(location.getString("city"));
			weatherDetailsModel.setWeatherDetailTemperature(condition.getString("temp"));
			weatherDetailsModel.setWeatherDetaiWindSpeed(wind.getString("speed"));
			weatherDetailsModel.setWeatherDetailAtmoshpherePressure(atmosphere.getString("pressure"));
			weatherDetailsModel.setWeatherDetailDateTime(channel.getString("lastBuildDate"));
			weatherDetailsModel.setWeatherDetailType(condition.getString("text"));
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		


		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		try {
			Intent sendWeatherDailyUpdate = new Intent("weatherdailyupdate");
			sendWeatherDailyUpdate.putExtra("city",weatherDetailsModel.getWeatherDetailCityName());
			sendWeatherDailyUpdate.putExtra("temp",weatherDetailsModel.getWeatherDetailTemperature());
			sendWeatherDailyUpdate.putExtra("speed",weatherDetailsModel.getWeatherDetaiWindSpeed());
			sendWeatherDailyUpdate.putExtra("pressure",weatherDetailsModel.getWeatherDetailAtmoshpherePressure());
			sendWeatherDailyUpdate.putExtra("lastBuildDate",weatherDetailsModel.getWeatherDetailDateTime());
			sendWeatherDailyUpdate.putExtra("text",weatherDetailsModel.getWeatherDetailType());
			context.sendBroadcast(sendWeatherDailyUpdate);
			mProgressDialog.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
