package com.sample.anoop.anoop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StorageHistory extends AppCompatActivity {

    public static final String DATABASE_NAME = "mydatabase";
    public static SQLiteDatabase mDatabase;

    BarChart chartInternal,chartExternal ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    ArrayList<BarEntry> BARENTRY2 ;
    ArrayList<String> BarEntryLabels2 ;
    BarDataSet Bardataset2 ;
    BarData BARDATA2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_storage_history);


        chartInternal = (BarChart) findViewById(R.id.chartInternal);
        chartExternal = (BarChart) findViewById(R.id.chartExternal);

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS internal (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    memory varchar(200) NOT NULL\n" +
                            ");"
            );

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS external (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    memory varchar(200) NOT NULL\n" +
                            ");"
            );

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        showInternalFromDatabase();
        showExternalFromDatabase();


        Bardataset = new BarDataSet(BARENTRY, "Internal Memory");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chartInternal.setData(BARDATA);
        chartInternal.animateY(3000);


        Bardataset2 = new BarDataSet(BARENTRY2, "External Memory");
        BARDATA2 = new BarData(BarEntryLabels2, Bardataset2);
        Bardataset2.setColors(ColorTemplate.COLORFUL_COLORS);
        chartExternal.setData(BARDATA2);
        chartExternal.animateY(3000);



    }

    private void showInternalFromDatabase() {

        try {

            BARENTRY = new ArrayList<>();
            BarEntryLabels = new ArrayList<String>();

            Cursor cursorInternal = mDatabase.rawQuery("SELECT * FROM internal", null);

            if (cursorInternal != null ) {
                if  (cursorInternal.moveToFirst()) {
                    do {
                        String time = cursorInternal.getString(cursorInternal.getColumnIndex("time"));
                        String internal_memory = cursorInternal.getString(cursorInternal.getColumnIndex("memory"));
                        Float internal_memory_float = Float.parseFloat(internal_memory);

                        BARENTRY.add(new BarEntry(internal_memory_float, cursorInternal.getPosition()));
                        BarEntryLabels.add(time);

                        System.out.println(time+" : "+internal_memory_float);
                    }while (cursorInternal.moveToNext());
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showExternalFromDatabase() {

        try {

            BARENTRY2 = new ArrayList<>();
            BarEntryLabels2 = new ArrayList<String>();

            Cursor cursorExternal = mDatabase.rawQuery("SELECT * FROM external", null);

            if (cursorExternal != null ) {
                if  (cursorExternal.moveToFirst()) {
                    do {
                        String time = cursorExternal.getString(cursorExternal.getColumnIndex("time"));
                        String external_memory = cursorExternal.getString(cursorExternal.getColumnIndex("memory"));
                        Float external_memory_float = Float.parseFloat(external_memory);

                        BARENTRY2.add(new BarEntry(external_memory_float, cursorExternal.getPosition()));
                        BarEntryLabels2.add(time);

                        System.out.println(time+" : "+external_memory_float);
                    }while (cursorExternal.moveToNext());
                }
            }


        } catch (Exception e){

            e.printStackTrace();
        }

    }
}
