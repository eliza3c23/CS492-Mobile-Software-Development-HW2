package com.example.android.connectedweather;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import com.example.android.connectedweather.utils.NetworkUtils;
import com.example.android.connectedweather.utils.OpenWeatherMapUtils;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnForecastItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mForecastListRV;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private ForecastAdapter mForecastAdapter;

    private static final String[] dummyForecastData = {
            "Sunny and Warm - 75F",
            "Partly Cloudy - 72F",
            "Mostly Sunny - 73F",
            "Partly Cloudy - 70F",
            "Occasional Showers - 65F",
            "Showers - 63F",
            "Occasional Showers - 64F",
            "Rainy - 62F",
            "Rainy - 61F",
            "Hurricane - 65F",
            "Windy and Clear - 70F",
            "Sunny and Warm - 77F",
            "Sunny and Warm - 81F"
    };

    private static final String[] dummyDetailedForecastData = {
            "Not a cloud in the sky today, with lows around 52F and highs near 75F.",
            "Clouds gathering in the late afternoon and slightly cooler than the day before, with lows around 49F and highs around 72F",
            "Scattered clouds all day with lows near 52F and highs near 73F",
            "Increasing cloudiness as the day goes on with some cooling; lows near 48F and highs near 70F",
            "Showers beginning in the morning and popping up intermittently throughout the day; lows near 46F and highs near 65F",
            "Showers scattered throughout the day, with lows near 46F and highs of 63F",
            "Showers increasing in intensity towards evening, with lows near 46F and highs near 64F",
            "Steady rain all day; lows near 47F and highs near 62F",
            "More steady rain, building in intensity towards evening; lows near 47F and highs near 61F",
            "Very, very strong winds and heavy rain; make sure you're wearing your raincoat today; lows near 50F and highs near 65F",
            "Rain ending in the very early morning, then clearing, with residual strong winds; lows near 61F and highs around 70F",
            "Beautiful day, with nothing but sunshine; lows near 55F and highs around 77F",
            "Another gorgeous day; lows near 56F and highs around 81F"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mForecastListRV = findViewById(R.id.rv_forecast_list);
        mErrorMessage = findViewById(R.id.tv_error_msg); /*Need to declare error_msg in xml*/
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mForecastListRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastListRV.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastListRV.setAdapter(mForecastAdapter);

        doOpenWeatherSearch();
    }
    private void doOpenWeatherSearch(){
        String url = OpenWeatherMapUtils.buildOpenWeatherSearchURL();
        Log.d(TAG,"Querying for weather data:" + url);
        new OpenWeatherSearchTask().execute(url);
    }

    @Override
    public void onForecastItemClick(String detailedForecast,String forecastIcon){
        Intent intent = new Intent(this, WeatherDetails.class );
        Bundle extras = new Bundle();
        extras.putString(OpenWeatherMapUtils.OPEN_WEATHER_MAP_WEATHER,detailedForecast);
        extras.putString(OpenWeatherMapUtils.OPEN_WEATHER_MAP_WEATHER_ICON,forecastIcon);
        intent.putExtras(extras);
        startActivity(intent);
    }

/*-----------------------------*/
    class OpenWeatherSearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        //NO UI operations in background
        protected String doInBackground(String... param) {
            String url = param[0];
            String s = null;
            try {
                s = NetworkUtils.doHttpGet(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                super.onPostExecute(s);
                mErrorMessage.setVisibility(View.INVISIBLE);
                mForecastListRV.setVisibility(View.VISIBLE);
                OpenWeatherMapUtils.Forecast[] forecasts = OpenWeatherMapUtils.parseConnectWeatherResult(s);
                ArrayList<String> results = new ArrayList<String>();
                ArrayList<String> icons = new ArrayList<String>();

                for (OpenWeatherMapUtils.Forecast forecast : forecasts) {

                    String icon = "";
                    String result = forecast.dt_txt + "-";
                    for (OpenWeatherMapUtils.WeatherInfo description : forecast.weather) {
                        icon = description.icon;
                        result = result + description.main + "-";
                    }
                    result = result + String.valueOf(forecast.main.temp) + OpenWeatherMapUtils.OPEN_WEATHER_MAP_TEMP_UNIT;
                    results.add(result);
                    icons.add(icon);
                }
                mForecastAdapter.updateForecastData(results,icons);
            }
            else{
                mErrorMessage.setVisibility(View.VISIBLE);
                mForecastListRV.setVisibility(View.INVISIBLE);
            }
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        switch (items.getItemId()) {
            case R.id.show_map:
                mapLocation();
                return true;
            default:
                return super.onOptionsItemSelected(items);
        }
    }

    private void mapLocation(){
        Uri intentLocation = Uri.parse("geo:0,0?q=Corvallis US");
        Intent intent = new Intent(Intent.ACTION_VIEW,intentLocation);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}

/*This two lines might add back!!!!*/
/*
           super.onPostExecute(s);
                   //mLoadingIndicator.setVisibility(View.INVISIBLE);
                   if (s != null) {
                   mErrorMessage.setVisibility(View.INVISIBLE);
                   mForecastListRV.setVisibility(View.VISIBLE);
                   OpenWeatherMapUtils.Forecast[] f = OpenWeatherMapUtils.parseConnectWeatherResult(s);
                   ArrayList<String> results = new ArrayList<String>();
        ArrayList<String> icons = new ArrayList<String>();

        for (OpenWeatherMapUtils.Forecast forecastInfo : f) {
        String icon = "";
        String result = forecastInfo.dt_txt + "-";
        for (OpenWeatherMapUtils.WeatherInfo details : forecastInfo.weather) {
        icon = details.icon;
        result = result + details.main + "-";
        }
        result = result + String.valueOf(forecastInfo.main.temp) + OpenWeatherMapUtils.OPEN_WEATHER_MAP_TEMP_UNIT;
        results.add(result);
        icons.add(icon);
        }
        mForecastAdapter.updateForecastData(results,icons);
        } else {
        mErrorMessage.setVisibility(View.VISIBLE);
        mForecastListRV.setVisibility(View.INVISIBLE);
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
                   */