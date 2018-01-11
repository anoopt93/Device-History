package com.sample.anoop.anoop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Device_History extends AppCompatActivity {

    public static final String DATABASE_NAME = "mydatabase";
    public static SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_device__history);

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS device (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    device_name varchar(200) NOT NULL,\n" +
                            "    os_version varchar(200) NOT NULL\n" +
                            ");"
            );

            showDeviceFromDatabase();

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    private void showDeviceFromDatabase() {

        try {

            Cursor cursorDevice = mDatabase.rawQuery("SELECT * FROM device", null);
            LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.linearlayoutDeviceHistory);

            if (cursorDevice != null ) {
                if  (cursorDevice.moveToFirst()) {
                    do {
                        String time = cursorDevice.getString(cursorDevice.getColumnIndex("time"));
                        String device_name = cursorDevice.getString(cursorDevice.getColumnIndex("device_name"));
                        String os_version = cursorDevice.getString(cursorDevice.getColumnIndex("os_version"));


                        LinearLayout parent = new LinearLayout(getApplicationContext());

                        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        parent.setOrientation(LinearLayout.HORIZONTAL);

                        // create a new textview
                        final TextView timeTextView = new TextView(this);
                        final TextView devicenameTextView = new TextView(this);
                        final TextView osVersionTextView = new TextView(this);


                        timeTextView.setText(time+"     ");
                        devicenameTextView.setText(device_name+"     ");
                        osVersionTextView.setText(os_version);

                        timeTextView.setTextColor(Color.WHITE);
                        devicenameTextView.setTextColor(Color.WHITE);
                        osVersionTextView.setTextColor(Color.WHITE);

                        // add the textview to the linearlayout
                        parent.addView(timeTextView);
                        parent.addView(devicenameTextView);
                        parent.addView(osVersionTextView);

                        myLinearLayout.addView(parent);
                        System.out.println(time+" : "+device_name+" : "+os_version);
                    }while (cursorDevice.moveToNext());
                }
            }

        }catch (Exception e){

            e.printStackTrace();
        }

    }
}
