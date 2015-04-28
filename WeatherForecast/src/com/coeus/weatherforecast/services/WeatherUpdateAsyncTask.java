package com.coeus.weatherforecast.services;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.coeus.weatherforecast.datamodel.WeatherDetailsModel;
import com.coeus.weatherforecast.fragmens.DashBoardFragment;
import com.coeus.weatherforecast.handlers.ServiceHandler;
import com.coeus.weatherforecast.listener.UpdateWeatherDetailsListener;

public class WeatherUpdateAsyncTask extends AsyncTask<String, Void, String>{

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
	protected String doInBackground(String... params) {
		
		
		String jsonResponseString="";
		if(params[0]==null)
		{
			//DashBoardFragment.loc_latitude, DashBoardFragment.loc_longitude
			apiUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+fetchCityNameUsingGoogleMap(DashBoardFragment.loc_latitude, DashBoardFragment.loc_longitude)+"%2Cnull%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		}
		else
		{
			apiUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+params[0]+"%2Cnull%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		}
		ServiceHandler serviceHandler = new ServiceHandler();
		try {
			if(isConnectingToInternet())
			{
			 jsonResponseString = serviceHandler.makeServiceCall(
					apiUrl,
					ServiceHandler.GET);
			 SharedPrefStoreData("apiResponse",jsonResponseString);
			}
			else
			{
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(context);
				jsonResponseString = sharedPreferences.getString("apiResponse", "");
				
			}
			
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
		


		return apiResponse.toString();
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(result!=null)
		{
		try {
			Intent sendWeatherDailyUpdate = new Intent("weatherdailyupdate");
			sendWeatherDailyUpdate.putExtra("city",weatherDetailsModel.getWeatherDetailCityName());
			sendWeatherDailyUpdate.putExtra("temp",weatherDetailsModel.getWeatherDetailTemperature());
			sendWeatherDailyUpdate.putExtra("speed",weatherDetailsModel.getWeatherDetaiWindSpeed());
			sendWeatherDailyUpdate.putExtra("pressure",weatherDetailsModel.getWeatherDetailAtmoshpherePressure());
			sendWeatherDailyUpdate.putExtra("lastBuildDate",weatherDetailsModel.getWeatherDetailDateTime());
			sendWeatherDailyUpdate.putExtra("text",weatherDetailsModel.getWeatherDetailType());
			context.sendBroadcast(sendWeatherDailyUpdate);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		}
		else
		{
			Toast.makeText(context, "Inavlid input or Service not available", Toast.LENGTH_SHORT).show();
		}
		mProgressDialog.dismiss();
	}
	// check connectivity
	public boolean isConnectingToInternet()
	{
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	public void SharedPrefStoreData(String tag, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(tag, value);
		editor.commit();
	}
	
	// if Service not available this code will get the location 
		 private String fetchCityNameUsingGoogleMap(double latitude, double longitude )
	     {
			 final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(DashBoardFragment.class.getName());

	         String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","
	                 + longitude + "&sensor=false&language=fr";

	         try
	         {
	             JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
	                     new BasicResponseHandler()));

	             // many nested loops.. not great -> use expression instead
	             // loop among all results
	             JSONArray results = (JSONArray) googleMapResponse.get("results");
	             for (int i = 0; i < results.length(); i++)
	             {
	                 // loop among all addresses within this result
	                 JSONObject result = results.getJSONObject(i);
	                 if (result.has("address_components"))
	                 {
	                     JSONArray addressComponents = result.getJSONArray("address_components");
	                     // loop among all address component to find a 'locality' or 'sublocality'
	                     for (int j = 0; j < addressComponents.length(); j++)
	                     {
	                    	 if(j==3)
	                    	 {
	                         JSONObject addressComponent = addressComponents.getJSONObject(j);
	                         if (result.has("types"))
	                         {
	                             JSONArray types = addressComponent.getJSONArray("types");

	                             // search for locality and sublocality
	                             String cityName = null;

	                             for (int k = 0; k < types.length(); k++)
	                             {
	                                 if ("locality".equals(types.getString(k)) && cityName == null)
	                                 {
	                                     if (addressComponent.has("long_name"))
	                                     {
	                                         cityName = addressComponent.getString("long_name");
	                                     }
	                                     else if (addressComponent.has("short_name"))
	                                     {
	                                         cityName = addressComponent.getString("short_name");
	                                     }
	                                 }
	                                 if ("sublocality".equals(types.getString(k)))
	                                 {
	                                     if (addressComponent.has("long_name"))
	                                     {
	                                         cityName = addressComponent.getString("long_name");
	                                     }
	                                     else if (addressComponent.has("short_name"))
	                                     {
	                                         cityName = addressComponent.getString("short_name");
	                                     }
	                                 }
	                             }
	                             if (cityName != null)
	                             {
	                                 return cityName;
	                             }
	                         }
	                    	 }
	                     }
	                 }
	             }
	         }
	         catch (Exception ignored)
	         {
	             ignored.printStackTrace();
	         }
	         return null;
	     }

}
