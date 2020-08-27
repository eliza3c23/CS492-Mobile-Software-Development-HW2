package com.example.android.connectedweather;

import android.content.Intent;
import androidx.core.app.ShareCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


import com.example.android.connectedweather.utils.OpenWeatherMapUtils;



public class WeatherDetails extends AppCompatActivity{
    private TextView mDetailWeatherTV;
    private ImageView mWeatherIconIV;
    private String mWeather;
    private String mWeatherIcon;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcase_item_detail);

        mDetailWeatherTV = findViewById(R.id.tv_detail_forecast);
        mWeatherIconIV = findViewById(R.id.iv_weather_icon);

        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null){
            Bundle extra = intent.getExtras();
            mWeather = extra.getString(OpenWeatherMapUtils.OPEN_WEATHER_MAP_WEATHER);
            mWeatherIcon = extra.getString(OpenWeatherMapUtils.OPEN_WEATHER_MAP_WEATHER_ICON);
            mDetailWeatherTV.setText(mWeather);
            Glide.with(getBaseContext()).load("https://openweathermap.org/img/w/"+mWeatherIcon+".png").into(mWeatherIconIV);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.weather_details_activity,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        switch (items.getItemId()) {
            case R.id.share:
                shareWeatherInfo();
                return true;

            default:
                return super.onOptionsItemSelected(items);
        }

    }
    private void shareWeatherInfo(){
        if(mWeather != null){
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(mWeather)
                    .setChooserTitle("How do you want to share this?")
                    .startChooser();

        }
    }
}
