package com.example.madassignment1;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.madassignment1.Notification.Common;
import com.example.madassignment1.Notification.MyService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PageSelection extends AppCompatActivity {

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    final long mintime = 5000;
    final float distance = 1000;
    final int req_code = 101;
    String LocationProvider = LocationManager.GPS_PROVIDER;
    boolean isPermissionGranted;
    final String ID = "ce5c6a5de007e0c6fb63bcadfe5d7bc7";
    final String url = "https://api.openweathermap.org/data/2.5/weather";

    Button btnSHowWeather, btnHistory;
    ProgressDialog progressDialog;
    boolean isLoadedAlready;

    ImageView cartImageView;
    boolean isLocationRequestByUser;
    TextView counterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_selection);

        counterTextView = findViewById(R.id.counterTextView);
        //init progress/loading bar
        progressDialog = new ProgressDialog(this);

        //get access to button
        btnSHowWeather = findViewById(R.id.btnSHowWeather);

        // PROCEED FROM THE LANDING PAGE
        Intent navToMenu = getIntent();

        // RECEIVE INFORMATION FROM THE LANDING PAGE
        String location = navToMenu.getStringExtra("location");

        // BUTTON FINDERS
        Button navToWarnings = findViewById(R.id.button);
        Button navToGoogleMap = findViewById(R.id.button2);
        cartImageView = findViewById(R.id.cartImageView);

        navToWarnings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent navToWarn = new Intent(PageSelection.this, WeatherWarning.class);
                startActivity(navToWarn);
            }
        });

        // TO PROCEED TO THE PAGE SELECTION PAGE
        navToGoogleMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent navToGoogleMap = new Intent(PageSelection.this, GoogleMap.class);

                // PASS IN INFO TO PASS TO THE NEXT PAGE
                navToGoogleMap.putExtra("realTimeLocation", location);
                startActivity(navToGoogleMap);
            }
        });

        CheckLocationPermission();

        //set click listener on load location
        btnSHowWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show current location
                isLocationRequestByUser = true;
                CheckLocationPermission();
            }
        });


        //set click Listener on notification button
        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PageSelection.this, HistoryActivity.class));
            }
        });




        //refresh after every 5 second
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                counterTextView.setText(new MyDb(PageSelection.this).getWeather().size()+"");
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void CheckBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(packageName)) {

            } else {
                BatteryOptimizationPermission();
            }
        } else {

        }
    }

    private void BatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + packageName));
                startActivityForResult(intent, 1010101);
            }
        }
    }

    public void SowNotification() {
        Intent intent = new Intent(PageSelection.this, MyService.class);
        stopService(intent);
        startService(intent);
    }


    //check location permission
    private void CheckLocationPermission() {
        if (ActivityCompat.checkSelfPermission(PageSelection.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PageSelection.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PageSelection.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            CheckBatteryOptimizationPermission();
            //check location permission if granted then get location
            getCurrentLocationWeather();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check permission is granted or not
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CheckBatteryOptimizationPermission();
                getCurrentLocationWeather();
                isPermissionGranted = true;

            } else {
                isPermissionGranted = false;
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //call weather api and get weather info using latitude and longitude
    private void DoNetworking(RequestParams params, double latitude, double longitude) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                WeatherData weatherData = WeatherData.fromJson(response);
                if (isLocationRequestByUser) {
                    updateUI(weatherData, latitude, longitude);
                }


                Common.latitude = latitude;
                Common.longitude = longitude;
                Common.country = weatherData.getCountry();
                Common.temp = weatherData.getTemp();
                Common.weather = weatherData.getWeather();
                SowNotification();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
            }
        });
    }

    //after successfully loaded info open new activity
    private void updateUI(WeatherData weatherData, double latitude, double longitude) {
        progressDialog.dismiss();
        Intent intent = new Intent(PageSelection.this, WeatherActivity.class);
        intent.putExtra("temp", weatherData.getTemp());
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("country", weatherData.getCountry());
        intent.putExtra("weather", weatherData.getWeather());

        Common.weather = weatherData.getWeather();
        Common.country = weatherData.getCountry();
        Common.temp = weatherData.getTemp();
        startActivity(intent);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    // THIS IS TO GET THE REAL TIME LOCATION
    private void getCurrentLocationWeather() {
        isLoadedAlready = false;
        if (isLocationRequestByUser) {
            progressDialog.setMessage("Loading Current Location Weather");
            progressDialog.show();
        }


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled == true) {
                    if (ActivityCompat.checkSelfPermission(PageSelection.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PageSelection.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) PageSelection.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, req_code);
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mintime, distance, this);
                    if (mLocationManager != null) {

                        //get current location
                        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        RequestParams params = new RequestParams();
                        params.put("lat", latitude);
                        params.put("lon", longitude);
                        params.put("appid", ID);

                        //we need to get location only once ,its getting location after every second but
                        //  if we allow to get location it will open weather api again and again that caused crashing
                        if (!isLoadedAlready) {
                            isLoadedAlready = true;
                            DoNetworking(params, latitude, longitude);
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PageSelection.this, "Location Cant fetched", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(PageSelection.this, "Please Turn on GPS", Toast.LENGTH_SHORT).show();
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

    public void SaveSharePref(Double latitude, Double longitude) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("latitude ", latitude + "");
        editor.putString("longitude", longitude + "");
        Common.latitude = latitude;
        Common.longitude = longitude;
        editor.apply();
    }
}