package com.example.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // This allows us to retrieve weather data from our selected application
    final String ID = "ce5c6a5de007e0c6fb63bcadfe5d7bc7";
    final String url = "https://api.openweathermap.org/data/2.5/weather";


    final long mintime = 5000;
    final float distance = 1000;
    final int req_code = 101;

    TextView Location, Weather, Temperature, Country;
    ImageView RefreshBtn;
    Button WeatherWarning;
    String LocationProvider = LocationManager.GPS_PROVIDER;
    ImageButton Menu;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Weather = findViewById(R.id.weather);
        Temperature = findViewById(R.id.tempDisplay);
        Location = findViewById(R.id.location);
        Country = findViewById(R.id.country);
        RefreshBtn = findViewById(R.id.button1);
        Menu = findViewById(R.id.imageView2);

        // TO REFRESH THE WEATHER DATA
        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refresh(view);
            }
        });

        // TO PROCEED TO THE PAGE SELECTION PAGE
        Menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent navToMenu = new Intent(MainActivity.this, PageSelection.class);
                // PASS IN INFO TO PASS TO THE NEXT PAGE
                navToMenu.putExtra("location", WeatherData.getLocation());
                startActivity(navToMenu);
            }
        });
    }

    // TO REFRESH THE PAGE
    public void refresh(View view) {
        Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(refresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent backIntent = getIntent();
        getCurrentLocationWeather();
    }

    // THIS IS TO GET THE REAL TIME LOCATION
    private void getCurrentLocationWeather() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled == true){
                    if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions((Activity) MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, req_code);
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mintime, distance, this);

                    Log.d("GPS Enabled", "GPS Enabled");
                    if(mLocationManager != null){
                        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location != null){
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            RequestParams params = new RequestParams();
                            params.put("lat", latitude);
                            params.put("lon", longitude);
                            params.put("appid", ID);
                            DoNetworking(params);
                        }
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
              //  LocationListener.super.onProviderDisabled(provider);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, req_code);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationProvider, mintime, distance, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==req_code){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Location get Success", Toast.LENGTH_SHORT).show();
                getCurrentLocationWeather();
            }
            else{
                //permission denied
            }
        }
    }

    private void DoNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();

                WeatherData weatherData = WeatherData.fromJson(response);
                Toast.makeText(MainActivity.this, ""+weatherData.getWeather(), Toast.LENGTH_SHORT).show();
                updateUI(weatherData);
                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
            }
        });
    }

    private void updateUI(WeatherData weatherData){
        Temperature.setText(weatherData.getTemp());
        Location.setText(weatherData.getLocation());
        Weather.setText("The weather is " + weatherData.getWeather() + " in");
        Country.setText(weatherData.getCountry());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}

