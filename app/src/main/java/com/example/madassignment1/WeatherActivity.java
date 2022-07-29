package com.example.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    String temp;
    double latitude, longitude;
    String country;
    String weather;
    TextView tempDisplayTv, countryTv, weatherTv, location;
    Button btnSaveOnGoogleMap;
    String address = "not found";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        //get weather info from previous activity using intent
        temp = getIntent().getStringExtra("temp");
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        country = getIntent().getStringExtra("country");
        weather = getIntent().getStringExtra("weather");
        getAddress(latitude, longitude);


        // init member veribale to access button and texView
        location = findViewById(R.id.location);
        tempDisplayTv = findViewById(R.id.tempDisplay);
        countryTv = findViewById(R.id.country);
        weatherTv = findViewById(R.id.weather);
        btnSaveOnGoogleMap = findViewById(R.id.btnSaveOnGoogleMap);

        //assign data to textView

        tempDisplayTv.setText(temp);
        countryTv.setText(country);
        weatherTv.setText(weather);
        location.setText(address);


        //set Click listener on button
        btnSaveOnGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //send same  data to next page that we received from previous activity
                Intent intent = new Intent(WeatherActivity.this, GoogleMap.class);
                intent.putExtra("temp", temp);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("country", country);
                intent.putExtra("weather", weather);
                intent.putExtra("address", address);
                startActivity(intent);
            }
        });
    }



    //get location address by latitude and longitude using geoCoder APi
    public void getAddress(double lat, double lng) {

        Geocoder geocoder = new Geocoder(WeatherActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
//            String address = obj.getCountryName();
//            String add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();

            address = obj.getAddressLine(0) + ", " + obj.getCountryName();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}