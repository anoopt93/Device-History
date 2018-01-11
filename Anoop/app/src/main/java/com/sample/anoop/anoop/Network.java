package com.sample.anoop.anoop;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Network extends AppCompatActivity {

    ImageView ImgNetwork;
    TextView TxtNetworkType;
    Button BtnNetworkHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_network);

        ImgNetwork = (ImageView) findViewById(R.id.img_network);
        TxtNetworkType = (TextView)findViewById(R.id.network_type);
        BtnNetworkHistory = (Button) findViewById(R.id.btn_network_history);

        BtnNetworkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),NetworkHistory.class);
                startActivity(i);
            }
        });

        try{
            checkNetworkType();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void checkNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                TxtNetworkType.setText(activeNetwork.getTypeName());
                ImgNetwork.setBackgroundResource(R.drawable.wifi);

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                TxtNetworkType.setText(activeNetwork.getTypeName());
                ImgNetwork.setBackgroundResource(R.drawable.signal);
            }
        }
    }
}
