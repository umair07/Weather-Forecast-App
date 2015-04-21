package com.coeusassignmentone.weatherforecast.datamodel;
// Data Model for Weather Updates 

public class WeatherDetailsModel 
{

	// variables 
	private String weatherDetailTemperature;
	private String weatherDetaiWindSpeed;
	private String weatherDetailAtmoshpherePressure;
	private String weatherDetailCityName;
	private String weatherDetailDateTime;
	private String weatherDetailType;

	public String getWeatherDetailTemperature() {
		return weatherDetailTemperature;
	}
	public void setWeatherDetailTemperature(String weatherDetailTemperature) {
		this.weatherDetailTemperature = weatherDetailTemperature;
	}
	public String getWeatherDetaiWindSpeed() {
		return weatherDetaiWindSpeed;
	}
	public void setWeatherDetaiWindSpeed(String weatherDetaiWindSpeed) {
		this.weatherDetaiWindSpeed = weatherDetaiWindSpeed;
	}
	public String getWeatherDetailAtmoshpherePressure() {
		return weatherDetailAtmoshpherePressure;
	}
	public void setWeatherDetailAtmoshpherePressure(
			String weatherDetailAtmoshpherePressure) {
		this.weatherDetailAtmoshpherePressure = weatherDetailAtmoshpherePressure;
	}
	public String getWeatherDetailCityName() {
		return weatherDetailCityName;
	}
	public void setWeatherDetailCityName(String weatherDetailCityName) {
		this.weatherDetailCityName = weatherDetailCityName;
	}
	public String getWeatherDetailDateTime() {
		return weatherDetailDateTime;
	}
	public void setWeatherDetailDateTime(String weatherDetailDateTime) {
		this.weatherDetailDateTime = weatherDetailDateTime;
	}
	public String getWeatherDetailType() {
		return weatherDetailType;
	}
	public void setWeatherDetailType(String weatherDetailType) {
		this.weatherDetailType = weatherDetailType;
	}
	

}
