package com.example.madassignment1;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {

    private String temp;
    private static String location;
    private String country;
    private String weather;

    public static WeatherData fromJson(JSONObject jsonObject){
        try{
            WeatherData weatherData = new WeatherData();
            //weatherData.location = jsonObject.getString("name");
            weatherData.weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            weatherData.location = jsonObject.getString("name");
            weatherData.country = jsonObject.getJSONObject("sys").getString("country");
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int)Math.rint(tempResult);
            weatherData.temp=Integer.toString(roundedValue);
            return weatherData;
        }
        catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getTemp(){
        return temp+"°C";
    }

    public static String getLocation(){
        return location;
    }

    public String getWeather(){
        return weather;
    }

    public String getCountry(){
        return country;
    }


}
