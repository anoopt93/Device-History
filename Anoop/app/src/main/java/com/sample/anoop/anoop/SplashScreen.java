package com.sample.anoop.anoop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        if (AppStatus.getInstance(SplashScreen.this).isOnline()) {

            new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    SharedPreferences pre = getSharedPreferences("Defualt_TimeIntervals", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putLong("DefulatBatteryTime",1000*60*2);
                    editor.putLong("DefulatStorageTime",1000*60*2);
                    editor.putLong("DefulatWeatherTime", 1000*60*2);
                    editor.putLong("DefulatNetworkTime",1000*60*2);
                    editor.putLong("DefulatDeviceTime",1000*60*2);
                    editor.commit();

                    Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);


        } else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
            alertDialogBuilder.setTitle("No Internet");
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher_icon);
            alertDialogBuilder
                    .setMessage("Connect to internet and Try again.")
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            finish();

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }
}
