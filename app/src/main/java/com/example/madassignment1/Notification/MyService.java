package com.example.madassignment1.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.madassignment1.WeatherData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class MyService extends Service {
    Double latitude;
    Double longitude;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartAlarm();
        return Service.START_STICKY;
    }

    private void StartAlarm() {
        DoNetworking();
        Intent intent = new Intent(getApplicationContext(), ReminderAlarmService.class);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 00, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 6000, pendingIntent);

        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 6000, pendingIntent);

        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 6000, pendingIntent);

        }
    }

    private void DoNetworking() {
        final String ID = "ce5c6a5de007e0c6fb63bcadfe5d7bc7";
        final String url = "https://api.openweathermap.org/data/2.5/weather";

        RequestParams params = new RequestParams();
        params.put("lat", latitude);
        params.put("lon", longitude);
        params.put("appid", ID);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                WeatherData weatherData = WeatherData.fromJson(response);
                if (weatherData.getWeather() != null) {
                    Common.weather = weatherData.getWeather();
                    Common.country = weatherData.getCountry();
                    Common.temp = weatherData.getTemp();

                } else {
                    Toast.makeText(MyService.this, "Weather data is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);

            }
        });
    }

//    public void getSaharPref() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String lat = preferences.getString("latitude", "1.290270");
//        String longd = preferences.getString("longitude", "103.851959");
//        latitude = Double.parseDouble(lat);
//        longitude = Double.parseDouble(longd);
//
//    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
