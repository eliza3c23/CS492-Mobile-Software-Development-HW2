package com.example.android.connectedweather.utils;

import android.net.Uri;
import java.io.Serializable;
import com.google.gson.Gson;


public class OpenWeatherMapUtils {
    private final static String OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private final static String OPEN_WEATHER_MAP_LOCATION_PRAM ="q";
    private final static String OPEN_WEATHER_MAP_LOCATION ="Corvallis,US";
    private final static String OPEN_WEATHER_MAP_UNIT ="units";
    private final static String OPEN_WEATHER_MAP_UNIT_VALUE = "metric";
    private final static String OPEN_WEATHER_MAP_API_KEY_PRAM = "APPID";
   
    private final static String OPEN_WEATHER_MAP_API_KEY_VALUE = "[Insert Token Here]";

    public final static String OPEN_WEATHER_MAP_WEATHER = "OpenWeatherMapUtils.Forecast";
    public final static String OPEN_WEATHER_MAP_WEATHER_ICON = "OpenWeatherMapUtils.WeatherInfo";
    public final static String OPEN_WEATHER_MAP_TEMP_UNIT = "C";

    //Description
    public static class WeatherInfo implements Serializable{
        public String main;
        public String icon;
    }

    public static class Temp implements Serializable{
        public float temp;
    }

    public static class Forecast implements Serializable{
        public String dt_txt;
        public Temp main;
        public WeatherInfo[] weather;
    }
    public static class ConnectWeatherResult{
        public Forecast[] list;
    }

    public static String buildOpenWeatherSearchURL() {
        return Uri.parse(OPEN_WEATHER_MAP_BASE_URL).buildUpon()
                .appendQueryParameter(OPEN_WEATHER_MAP_LOCATION_PRAM, OPEN_WEATHER_MAP_LOCATION)
                .appendQueryParameter(OPEN_WEATHER_MAP_UNIT,OPEN_WEATHER_MAP_UNIT_VALUE)
                .appendQueryParameter(OPEN_WEATHER_MAP_API_KEY_PRAM, OPEN_WEATHER_MAP_API_KEY_VALUE)
                .build()
                .toString();
    }

    public static Forecast[] parseConnectWeatherResult(String json){
        Gson gson = new Gson();
        ConnectWeatherResult results = gson.fromJson(json, ConnectWeatherResult.class);
        if(results !=  null && results.list != null){
            return results.list;
        }else{
            return null;
        }
    }
}
