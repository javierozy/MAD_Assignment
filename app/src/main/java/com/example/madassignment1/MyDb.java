package com.example.madassignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MyDb extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "My_DB";
    static final String TABLE_WEATHER = "table_weather";
    Context context;


    public MyDb(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE_LOCATION_CONTACT = "CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER + "("
                + "id_auto" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id" + " TEXT, "
                + "temper" + " TEXT, "
                + "weather" + " TEXT, "
                + "country" + " TEXT, "
                + "latitude" + " TEXT, "
                + "longitude" + " TEXT, "
                + "status" + " BOOLEAN " + ")";

        db.execSQL(CREATE_TABLE_LOCATION_CONTACT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(db);

    }

    public List<Weather> getWeather() {
        SQLiteDatabase db = getWritableDatabase();
        List<Weather> lisWeather = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {


                String id = cursor.getString(1);
                String temper = cursor.getString(2);
                String weather = cursor.getString(3);
                String country = cursor.getString(4);
                String latitude = cursor.getString(5);
                String longitude = cursor.getString(6);
                boolean status = cursor.getInt(7) > 0;

                lisWeather.add(new Weather(id,temper,weather,country,latitude,longitude,status));
            } while (cursor.moveToNext());
        }
        return lisWeather;
    }


//    public Category getCategoryById(String categoryId) {
//        SQLiteDatabase db = getWritableDatabase();
//        Category category = null;
//
//        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE "
//                + "categoryId" + "= '" + categoryId + "'";
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String catId = cursor.getString(1);
//                String categoryName = cursor.getString(2);
//                String categoryIcon = cursor.getString(3);
//                category = new Category(catId, categoryName, categoryIcon);
//
//            } while (cursor.moveToNext());
//        }
//        return category;
//    }


    public void addWeather(Weather weather) {

        Toast.makeText(context, "Wkingg", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", weather.getId());
        values.put("temper", weather.getTemp());
        values.put("weather", weather.getWeather());
        values.put("country", weather.getCountry());
        values.put("latitude", weather.getLatitude());
        values.put("longitude", weather.getLongitude());
        values.put("status", weather.isStatus());

        long insert = db.insert(TABLE_WEATHER, null, values);
        if (insert == -1) {
            Toast.makeText(context, "Weather  not Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Weather Added!", Toast.LENGTH_SHORT).show();
        }
        db.close(); // Closing database connection
    }


}
