package com.sample.anoop.anoop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class NetworkHistory extends AppCompatActivity {

    public static final String DATABASE_NAME = "mydatabase";
    public static SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_network_history);

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS network (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    network_type varchar(200) NOT NULL\n" +
                            ");"
            );

            showNetworkFromDatabase();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void showNetworkFromDatabase() {

        try{

            Cursor cursorNetwork = mDatabase.rawQuery("SELECT * FROM network", null);
            LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.linearlayoutNetworkHistory);

            if (cursorNetwork != null ) {
                if  (cursorNetwork.moveToFirst()) {
                    do {
                        String time = cursorNetwork.getString(cursorNetwork.getColumnIndex("time"));
                        String network_type = cursorNetwork.getString(cursorNetwork.getColumnIndex("network_type"));


                        LinearLayout parent = new LinearLayout(getApplicationContext());

                        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        parent.setOrientation(LinearLayout.HORIZONTAL);

                            // create a new textview
                            final TextView timeTextView = new TextView(this);
                            final TextView networkTextView = new TextView(this);

                            timeTextView.setText(time+"       ");
                            networkTextView.setText("         "+network_type);

                            timeTextView.setTextColor(Color.WHITE);
                            networkTextView.setTextColor(Color.WHITE);

                            // add the textview to the linearlayout
                            parent.addView(timeTextView);
                            parent.addView(networkTextView);

                        myLinearLayout.addView(parent);


                        System.out.println(time+" : "+network_type);
                    }while (cursorNetwork.moveToNext());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
