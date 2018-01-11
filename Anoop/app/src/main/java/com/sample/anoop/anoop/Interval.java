package com.sample.anoop.anoop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Interval extends AppCompatActivity {

    EditText ETBatteryMinute,ETStorageMinute,ETWeatherMinute,ETNetworkMinute,ETDeviceMinute;
    Button BtnBattery,BtnStorage,BtnWeather,BtnNetwork,BtnDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_interval);


        ETBatteryMinute = (EditText) findViewById(R.id.et_battery);
        ETStorageMinute = (EditText) findViewById(R.id.et_storage);
        ETWeatherMinute = (EditText) findViewById(R.id.et_weather);
        ETNetworkMinute = (EditText) findViewById(R.id.et_network);
        ETDeviceMinute = (EditText) findViewById(R.id.et_device);

        BtnBattery = (Button) findViewById(R.id.btn_battery);
        BtnStorage = (Button) findViewById(R.id.btn_storage);
        BtnWeather = (Button) findViewById(R.id.btn_weather);
        BtnNetwork = (Button) findViewById(R.id.btn_network);
        BtnDevice = (Button) findViewById(R.id.btn_device);




        BtnBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int battery_minute = 0;
                try {

                    battery_minute = Integer.parseInt(ETBatteryMinute.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }

                if("".equals(ETBatteryMinute.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                }else if(battery_minute == 0 ){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                } else if(battery_minute == 00) {
                    Toast.makeText(getApplicationContext(), "Enter a time in minute", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences pre = getSharedPreferences("SP_TimeInterval", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putLong("Battery_Interval",1000*60*battery_minute);
                    editor.commit();ETBatteryMinute.setText(null);

                    try {

                        Intent myServiceStop = new Intent(Interval.this, MyService.class);
                        stopService(myServiceStop);
                        Intent myServiceStart = new Intent(Interval.this, MyService.class);
                        startService(myServiceStart);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Time Interval Set to "+battery_minute+" minutes for Battery History",Toast.LENGTH_SHORT).show();
                }

            }
        });


        BtnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int storage_minute = 0;
                try {

                    storage_minute = Integer.parseInt(ETStorageMinute.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }

                if("".equals(ETStorageMinute.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                }else if(storage_minute == 0 ){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                } else if(storage_minute == 00) {
                    Toast.makeText(getApplicationContext(), "Enter a time in minute", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences pre = getSharedPreferences("SP_TimeInterval", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putLong("Storage_Interval",1000*60*storage_minute);
                    editor.commit();
                    ETStorageMinute.setText(null);

                    try {

                        Intent myServiceStop = new Intent(Interval.this, MyService.class);
                        stopService(myServiceStop);
                        Intent myServiceStart = new Intent(Interval.this, MyService.class);
                        startService(myServiceStart);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Time Interval Set to "+storage_minute+" minutes for Storage History",Toast.LENGTH_SHORT).show();
                }

            }
        });


        BtnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int weather_minute = 0;
                try {

                    weather_minute = Integer.parseInt(ETWeatherMinute.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }

                if("".equals(ETWeatherMinute.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                }else if(weather_minute == 0 ){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                } else if(weather_minute == 00) {
                    Toast.makeText(getApplicationContext(), "Enter a time in minute", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences pre = getSharedPreferences("SP_TimeInterval", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putLong("Weather_Interval",1000*60*weather_minute);
                    editor.commit();
                    ETWeatherMinute.setText(null);

                    try {

                        Intent myServiceStop = new Intent(Interval.this, MyService.class);
                        stopService(myServiceStop);
                        Intent myServiceStart = new Intent(Interval.this, MyService.class);
                        startService(myServiceStart);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Time Interval Set to "+weather_minute+" minutes for Weather History",Toast.LENGTH_SHORT).show();
                }

            }
        });

        BtnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int network_minute = 0;
                try {

                    network_minute = Integer.parseInt(ETNetworkMinute.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }

                if("".equals(ETNetworkMinute.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                }else if(network_minute == 0 ){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                } else if(network_minute == 00) {
                    Toast.makeText(getApplicationContext(), "Enter a time in minute", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences pre = getSharedPreferences("SP_TimeInterval", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putLong("Network_Interval",1000*60*network_minute);
                    editor.commit();
                    ETNetworkMinute.setText(null);

                    try {

                        Intent myServiceStop = new Intent(Interval.this, MyService.class);
                        stopService(myServiceStop);
                        Intent myServiceStart = new Intent(Interval.this, MyService.class);
                        startService(myServiceStart);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Time Interval Set to "+network_minute+" minutes for Network History",Toast.LENGTH_SHORT).show();
                }

            }
        });

        BtnDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int device_minute = 0;
                try {

                    device_minute = Integer.parseInt(ETDeviceMinute.getText().toString());
                } catch (Exception e){
                    e.printStackTrace();
                }

                if("".equals(ETDeviceMinute.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                }else if(device_minute == 0 ){
                    Toast.makeText(getApplicationContext(),"Enter a time in minute",Toast.LENGTH_SHORT).show();
                } else if(device_minute == 00) {
                    Toast.makeText(getApplicationContext(), "Enter a time in minute", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences pre = getSharedPreferences("SP_TimeInterval", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putLong("Device_Interval",1000*60*device_minute);
                    editor.commit();
                    ETDeviceMinute.setText(null);

                    try {

                        Intent myServiceStop = new Intent(Interval.this, MyService.class);
                        stopService(myServiceStop);
                        Intent myServiceStart = new Intent(Interval.this, MyService.class);
                        startService(myServiceStart);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Time Interval Set to "+device_minute+" minutes for Device History",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}
