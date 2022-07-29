package com.example.madassignment1;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMap extends FragmentActivity implements OnMapReadyCallback {

    private com.google.android.gms.maps.GoogleMap mMap;
    String temp;
    double latitude, longitude;
    String country, address;
    String weather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        temp = getIntent().getStringExtra("temp");
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        country = getIntent().getStringExtra("country");
        weather = getIntent().getStringExtra("weather");
        address = getIntent().getStringExtra("address");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;

        if (temp != null) {

            //show marker on map
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(weather + "\n" + country + temp);
            markerOptions.position(new LatLng(latitude, longitude));
            mMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));


            //show info window that contain all info of weather
            mMap.setInfoWindowAdapter(new com.google.android.gms.maps.GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {


                    //assign data to info window
                    View v = getLayoutInflater().inflate(R.layout.infowindowlayout, null);

                    LatLng latLng = arg0.getPosition();
                    TextView tempDisplayTv = v.findViewById(R.id.tempDisplay);
                    TextView countryTv = v.findViewById(R.id.country);
                    TextView weatherTv = v.findViewById(R.id.weather);
                    TextView locationTv = v.findViewById(R.id.location);

                    tempDisplayTv.setText(temp);
                    countryTv.setText(country);
                    weatherTv.setText(weather);
                    locationTv.setText(address);

                    return v;

                }
            });
        }
    }

}