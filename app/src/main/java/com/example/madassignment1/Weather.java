package com.example.madassignment1;

public class Weather {
    String id;
    String temp;
    String weather;
    String country;
    String latitude;
    String longitude;
    boolean status;

    public Weather(String id, String temp, String weather, String country, String latitude, String longitude, boolean status) {
        this.id = id;
        this.temp = temp;
        this.weather = weather;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public Weather() {
    }


    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
