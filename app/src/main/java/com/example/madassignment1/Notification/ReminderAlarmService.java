package com.example.madassignment1.Notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.madassignment1.MainActivity;
import com.example.madassignment1.MyDb;
import com.example.madassignment1.PageSelection;
import com.example.madassignment1.R;
import com.example.madassignment1.Weather;
import com.example.madassignment1.WeatherData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();
    private static final String CHANNEL_ID = "com.example.madassignment1.channel_id";
    int notification_id = 1;
    final String ID = "ce5c6a5de007e0c6fb63bcadfe5d7bc7";
    final String url = "https://api.openweathermap.org/data/2.5/weather";

    MyDb myDb;
    Bitmap largeIcon;


    public ReminderAlarmService() {
        super(TAG);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getExtras() == null) {
            return;
        } else {
            myDb = new MyDb(this);
            Toast.makeText(this, "Wkwkwkwkwkw", Toast.LENGTH_SHORT).show();
            myDb.addWeather(new Weather(System.currentTimeMillis()+"",Common.temp,Common.weather,Common.country,Common.latitude+"",Common.longitude+"",false));



            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.image);
            Intent myIntent = new Intent(this, PageSelection.class);
            myIntent.putExtra("locationId", 00);

            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 00, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentTitle(Common.weather)
                    .setContentText(Common.temp + "\n" + Common.country)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setSmallIcon(R.drawable.image)

                    .setLargeIcon(largeIcon)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setLights(Color.YELLOW, 200, 200)
                    .setDeleteIntent(createOnDismissedIntent(this, 00))
                    .setContentIntent(pendingIntent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID);
            }

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channelName", importance);
                channel.setDescription("This is notification");
                channel.setShowBadge(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(notification_id, builder.build());
            notification_id++;

             }
    }

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this,
                        notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }

//    private void DoNetworking(RequestParams params) {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                WeatherData weatherData = WeatherData.fromJson(response);
//                if (weatherData.getWeather() != null) {
//                    weather = weatherData.getWeather();
//                    country = weatherData.getWeather();
//                    temp = weatherData.getTemp();
//
//
//                }else {
//                    Toast.makeText(ReminderAlarmService.this, "Nuu", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                //super.onSuccess(statusCode, headers, response);
//                Toast.makeText(ReminderAlarmService.this, "Nuu", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}

