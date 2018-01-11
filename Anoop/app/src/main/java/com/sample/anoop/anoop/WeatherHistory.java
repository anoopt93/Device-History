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

public class WeatherHistory extends AppCompatActivity {

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
        setContentView(R.layout.activity_weather_history);

        chart = (BarChart) findViewById(R.id.chart1);

        try {
            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS weather (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    battery_level varchar(200) NOT NULL\n" +
                            ");"
            );

        } catch (Exception e){

            e.printStackTrace();
        }

        showWeatherFromDatabase();


        Bardataset = new BarDataSet(BARENTRY, "Temprature");

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);

        chart.animateY(3000);

    }

    private void showWeatherFromDatabase() {

        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();

        Cursor cursorWeather = mDatabase.rawQuery("SELECT * FROM weather", null);

        if (cursorWeather != null ) {
            if  (cursorWeather.moveToFirst()) {
                do {
                    String time = cursorWeather.getString(cursorWeather.getColumnIndex("time"));
                    String weather = cursorWeather.getString(cursorWeather.getColumnIndex("weather"));
                    Float weather_float = Float.parseFloat(weather);

                    BARENTRY.add(new BarEntry(weather_float, cursorWeather.getPosition()));
                    BarEntryLabels.add(time);

                    System.out.println(time+" : "+weather);
                }while (cursorWeather.moveToNext());
            }
        }

    }
}
