package com.sample.anoop.anoop;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Device extends AppCompatActivity {

    Button BtnDeviceHistory;
    TextView TxtDeviceName,TxtOsVersion, TxtOsName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_device);

        TxtDeviceName = (TextView) findViewById(R.id.device_name);
        TxtOsVersion = (TextView) findViewById(R.id.os_version);
        TxtOsName = (TextView) findViewById(R.id.os_name);
        BtnDeviceHistory = (Button) findViewById(R.id.btn_device_history);

        BtnDeviceHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Device_History.class);
                startActivity(i);
            }
        });

        try {

            String devicename = android.os.Build.MODEL;
            String myVersion = Build.VERSION.RELEASE;

            TxtDeviceName.setText(devicename);
            TxtOsVersion.setText(myVersion);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        try {

            String osName = Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
            TxtOsName.setText(osName);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
