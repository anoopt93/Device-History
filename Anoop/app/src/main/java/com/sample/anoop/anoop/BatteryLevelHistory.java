package com.sample.anoop.anoop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BatteryLevelHistory extends AppCompatActivity {

    public static final String DATABASE_NAME = "mydatabase";
    public static SQLiteDatabase mDatabase;
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_battery_level_history);

        chart = (BarChart) findViewById(R.id.chart1);

        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS battery (\n" +
                        "    time varchar(200) NOT NULL,\n" +
                        "    battery_level varchar(200) NOT NULL\n" +
                        ");"
        );

        showBatteryFromDatabase();


        Bardataset = new BarDataSet(BARENTRY, "Battery Percentage");

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);

        chart.animateY(3000);

    }

    private void showBatteryFromDatabase() {

        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();

        Cursor cursorBattery = mDatabase.rawQuery("SELECT * FROM battery", null);

        if (cursorBattery != null ) {
            if  (cursorBattery.moveToFirst()) {
                do {
                    String time = cursorBattery.getString(cursorBattery.getColumnIndex("time"));
                    String battery_level = cursorBattery.getString(cursorBattery.getColumnIndex("battery_level"));
                    Float battery_percentage = Float.parseFloat(battery_level);

                    BARENTRY.add(new BarEntry(battery_percentage, cursorBattery.getPosition()));
                    BarEntryLabels.add(time);

                    System.out.println(time+" : "+battery_level);
                }while (cursorBattery.moveToNext());
            }
        }

    }
}
