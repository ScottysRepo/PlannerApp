package com.example.sseethoff.plannerapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.ealen.yahoo.weather.YahooAPI;


import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private  static final String TAG = "MainActivity";
    private CalendarView mCalendarView;
    private int answer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                String date = year + "/" + month + "/"+ dayOfMonth ;
                Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date);
                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
        // checks if permissions are allowed
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        String _Location;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    answer);
        }
        //gets current location, if it fails along the way it sets location as amarillo texas
        Location locations = null;
        List<String> providerList = null;
        try {
            locations = locationManager.getLastKnownLocation(provider);
            providerList = locationManager.getAllProviders();
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    _Location = listAddresses.get(0).getLocality();
                }else{
                    _Location = "Amarillo Texas";
                }
            } catch (IOException e) {
                e.printStackTrace();
                _Location = "Amarillo Texas";
            }
        }else{
            _Location = "Amarillo Texas";
        }
        //gets weather for current location if previous code got location
        // Yahoo Weather API used is from https://github.com/Ealenn/YahooWeather-API-Android
        YahooAPI Yahoo = new YahooAPI(_Location);
        String text = "";
        String text2 = "";
        try{
            Yahoo.syncData();
            text = Yahoo.Condition().getTemp() + "°";
            text2 = Yahoo.Condition().getText();
        } catch(Exception e){
            text = "Error!";
            text2 = "Error!";
        }
        TextView TempTextView = (TextView)findViewById(R.id.TempTextView);
        TempTextView.setText(text);
        TextView ConditionTextView = (TextView)findViewById(R.id.ConditionTextView);
        ConditionTextView.setText(text2);
    }
}