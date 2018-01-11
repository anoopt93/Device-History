package com.sample.anoop.anoop;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by ANOOP on 06-Jan-18.
 */

public class MyService extends Service {

    private static String TAG = "MyService";

    public static final String DATABASE_NAME = "mydatabase";
    public static SQLiteDatabase mDatabase;

    File Internalpath;
    StatFs Internalstat;
    long InternalBlockSize = 0, InternalAvailableBlockSize = 0;

    File Externalpath;
    StatFs Externalstat;
    long ExternalBlockSize = 0, ExternalAvailableBlockSize = 0;
    private static final String ERROR = "Error" ;


    private Handler batteryHandler;
    private Runnable batteryRunnable;
    private  long batteryRunTime;

    private Handler storageHandler;
    private Runnable storageRunnable;
    private  long storageRunTime;

    private Handler weatherHandler;
    private Runnable weatherRunnable;
    private  long weatherRunTime;

    private Handler networkHandler;
    private Runnable networkRunnable;
    private  long networkRunTime;

    private Handler deviceHandler;
    private Runnable deviceRunnable;
    private  long deviceRunTime;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    String Latitude,Longitude;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        SharedPreferences pref = getSharedPreferences("Defualt_TimeIntervals",MODE_PRIVATE);
        long DefulatBatteryTime = pref.getLong("DefulatBatteryTime",0);
        long DefulatStorageTime = pref.getLong("DefulatStorageTime",0);
        long DefulatWeatherTime = pref.getLong("DefulatWeatherTime",0);
        long DefulatNetworkTime = pref.getLong("DefulatNetworkTime",0);
        long DefulatDeviceTime = pref.getLong("DefulatDeviceTime",0);

        SharedPreferences pref2 = getSharedPreferences("SP_TimeInterval",MODE_PRIVATE);
        long Battery_Interval = pref2.getLong("Battery_Interval", 0);
        long Storage_Interval = pref2.getLong("Storage_Interval", 0);
        long Weather_Interval = pref2.getLong("Weather_Interval", 0);
        long Network_Interval = pref2.getLong("Network_Interval", 0);
        long Device_Interval = pref2.getLong("Device_Interval", 0);

        if(Battery_Interval == 0){
            batteryRunTime = DefulatBatteryTime;
        } else {
            batteryRunTime = Battery_Interval;
        }

        if(Storage_Interval == 0){
            storageRunTime = DefulatStorageTime;
        } else{
            storageRunTime = Storage_Interval;
        }

        if(Weather_Interval == 0){
            weatherRunTime = DefulatWeatherTime;
        } else {
            weatherRunTime = Weather_Interval;
        }

        if (Network_Interval == 0){
            networkRunTime = DefulatNetworkTime;
        } else{
            networkRunTime = Network_Interval;
        }

        if(Device_Interval == 0){
            deviceRunTime = DefulatDeviceTime;
        } else {
            deviceRunTime = Device_Interval;
        }


        batteryLevelService();

        storageService();

        weatherService();

        networkService();

        deviceService();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (batteryHandler != null) {
            batteryHandler.removeCallbacks(batteryRunnable);
        }

        if (storageHandler != null) {
            storageHandler.removeCallbacks(storageRunnable);
        }

        if (weatherHandler != null) {
            weatherHandler.removeCallbacks(weatherRunnable);
        }

        if (networkHandler != null) {
            networkHandler.removeCallbacks(networkRunnable);
        }

        if (deviceHandler != null) {
            deviceHandler.removeCallbacks(deviceRunnable);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart");
    }


    public void batteryLevelService(){

        batteryHandler = new Handler();
        batteryRunnable = new Runnable() {
            @Override
            public void run() {

                try {

                    String battery_percentage = String.valueOf(BatteryPercentage.getBatteryPercentage(getApplicationContext()));
                    addBatteryLevel(getCurrentTime(),battery_percentage);
                    batteryHandler.postDelayed(batteryRunnable, batteryRunTime);

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        batteryHandler.post(batteryRunnable);

    }

    public void storageService(){

        // storage service
        storageHandler = new Handler();
        storageRunnable = new Runnable() {
            @Override
            public void run() {



                try {
                    Internalpath = Environment.getExternalStorageDirectory();
                    Internalstat = new StatFs(Internalpath.getPath());
                }catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Externalpath = new File("/mnt/extSdCard");
                    Externalstat = new StatFs(Externalpath.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }



                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {


                        InternalBlockSize = Internalstat.getBlockSizeLong();
                        InternalAvailableBlockSize = Internalstat.getAvailableBlocksLong();
                        String InternalMemoryAvailable = (formatSize(InternalAvailableBlockSize * InternalBlockSize));
                        addInternalMemory(getCurrentTime(),InternalMemoryAvailable);
                        if (externalMemoryAvailable()) {
                            try {

                                ExternalBlockSize = Externalstat.getBlockSizeLong();
                                ExternalAvailableBlockSize = Externalstat.getAvailableBlocksLong();
                                String ExternalMemoryAvailable = (formatSize(ExternalAvailableBlockSize * ExternalBlockSize));
                                addExternalMemory(getCurrentTime(),ExternalMemoryAvailable);
                            } catch ( Exception e){
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {


                        InternalBlockSize = Internalstat.getBlockSize();
                        InternalAvailableBlockSize = Internalstat.getAvailableBlocks();
                        String InternalMemoryAvailable = (formatSize(InternalAvailableBlockSize * InternalBlockSize));
                        addInternalMemory(getCurrentTime(),InternalMemoryAvailable);
                        if (externalMemoryAvailable()) {
                            try {

                                ExternalBlockSize = Externalstat.getBlockSize();
                                ExternalAvailableBlockSize = Externalstat.getAvailableBlocks();
                                String ExternalMemoryAvailable = (formatSize(ExternalAvailableBlockSize * ExternalBlockSize));
                                addExternalMemory(getCurrentTime(),ExternalMemoryAvailable);

                            }catch ( Exception e){
                                e.printStackTrace();
                            }


                        } else {

                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }


                storageHandler.postDelayed(storageRunnable, storageRunTime);

            }
        };
        storageHandler.post(storageRunnable);
    }

    public void weatherService(){

        // weather service
        weatherHandler = new Handler();
        weatherRunnable = new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(getApplicationContext(),"Weather : ",Toast.LENGTH_SHORT).show();
                //####################################### find user location ###########################################

                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission)
                            != MockPackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{mPermission},
                                REQUEST_CODE_PERMISSION);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                gps = new GPSTracker(getApplicationContext());

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Latitude = String.valueOf(latitude);
                    Longitude = String.valueOf(longitude);

                }else{
                    gps.showSettingsAlert();
                }


                try {
                    Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                        public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                            //Toast.makeText(getApplicationContext(),"Weather : "+weather_temperature,Toast.LENGTH_SHORT).show();

                            String temp="";
                            temp = weather_temperature.replaceAll("\u00b0","");
                            addWeather(getCurrentTime(),temp);
                        }
                    });

                    asyncTask.execute(Latitude, Longitude);

                } catch (Exception e) {

                }





                weatherHandler.postDelayed(weatherRunnable, weatherRunTime);
            }
        };
        weatherHandler.post(weatherRunnable);
    }

    public void networkService(){

        networkHandler = new Handler();
        networkRunnable = new Runnable() {
            @Override
            public void run() {

                try {

                    String network_type = checkNetworkType();
                    addNetwork(getCurrentTime(),network_type);
                    networkHandler.postDelayed(networkRunnable, networkRunTime);

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        networkHandler.post(networkRunnable);
    }

    public void deviceService(){

        deviceHandler = new Handler();
        deviceRunnable = new Runnable() {
            @Override
            public void run() {

                try {

                    String devicename = android.os.Build.MODEL;
                    String myVersion = Build.VERSION.RELEASE;

                    addDevice(getCurrentTime(),devicename, myVersion);
                    deviceHandler.postDelayed(deviceRunnable, deviceRunTime);

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        deviceHandler.post(deviceRunnable);
    }



    public  boolean externalMemoryAvailable() {
        //return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        String state = Environment.getExternalStorageState();
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageAvailable == true
                && mExternalStorageWriteable == true) {
            return true;
        } else {
            return false;
        }
    }

    public  String formatSize(float size) {


        String suffix = null;

        if (size >= 1024) {
            suffix = "";
            size /= 1024;
            if (size >= 1024) {
                suffix = "";
                size /= 1024;
            }
            if (size >= 1024) {
                suffix = "";
                size /= 1024;
            }
        }


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println(df.format(size));

        String res = df.format(size);
        StringBuilder resultBuffer = new StringBuilder(res);
        //StringBuilder resultBuffer = new StringBuilder(Float.toString(size));


        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public String getCurrentTime(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String currentTime = sdf.format(cal.getTime());
        return currentTime;
    }

    public String checkNetworkType() {
        String network_type= "";
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                 network_type = activeNetwork.getTypeName();

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                network_type = activeNetwork.getTypeName();

            } else {
                network_type = "no network";
            }
        }
        return  network_type;
    }


// Methods to Save details to SQLite database


    public void addBatteryLevel(String time, String battery_level) {

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS battery (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    battery_level varchar(200) NOT NULL\n" +
                            ");"
            );

            String insertSQL = "INSERT INTO battery \n" +
                    "(time, battery_level)\n" +
                    "VALUES \n" +
                    "(?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{time, battery_level});
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void addWeather(String time, String weather) {

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS weather (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    weather varchar(200) NOT NULL\n" +
                            ");"
            );

            String insertSQL = "INSERT INTO weather \n" +
                    "(time, weather)\n" +
                    "VALUES \n" +
                    "(?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{time, weather});
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void addInternalMemory(String time, String internal) {

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS internal (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    memory varchar(200) NOT NULL\n" +
                            ");"
            );

            String insertSQL = "INSERT INTO internal \n" +
                    "(time, memory)\n" +
                    "VALUES \n" +
                    "(?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{time, internal});

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addExternalMemory(String time, String external) {

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS external (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    memory varchar(200) NOT NULL\n" +
                            ");"
            );

            String insertSQL = "INSERT INTO external \n" +
                    "(time, memory)\n" +
                    "VALUES \n" +
                    "(?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{time, external});
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void addNetwork(String time, String network_type){

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS network (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    network_type varchar(200) NOT NULL\n" +
                            ");"
            );

            String insertSQL = "INSERT INTO network \n" +
                    "(time, network_type)\n" +
                    "VALUES \n" +
                    "(?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{time, network_type});
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addDevice(String time, String device_name, String os_version){

        try {

            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            mDatabase.execSQL(
                    "CREATE TABLE IF NOT EXISTS device (\n" +
                            "    time varchar(200) NOT NULL,\n" +
                            "    device_name varchar(200) NOT NULL,\n" +
                            "    os_version varchar(200) NOT NULL\n" +
                            ");"
            );

            String insertSQL = "INSERT INTO device \n" +
                    "(time, device_name, os_version)\n" +
                    "VALUES \n" +
                    "(?, ?, ?);";


            mDatabase.execSQL(insertSQL, new String[]{time, device_name, os_version});
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
