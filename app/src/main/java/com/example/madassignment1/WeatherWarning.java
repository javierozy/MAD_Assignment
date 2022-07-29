package com.example.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class WeatherWarning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent i = getIntent();

        String Url = "https://www.nea.gov.sg/weather";

        WebView wv = findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(Url);

        ImageView backButton = findViewById(R.id.button3);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();

                // PROCEED TO THE PREVIOUS PAGE
                Intent backIntent = new Intent(WeatherWarning.this, PageSelection.class);
                startActivity(backIntent);
            }
        });
    }
}
