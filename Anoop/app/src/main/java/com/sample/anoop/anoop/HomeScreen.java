package com.sample.anoop.anoop;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.test.mock.MockPackageManager;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    String Latitude,Longitude;

    Typeface weatherFont;

    File Internalpath;
    StatFs Internalstat;
    long InternalBlockSize = 0, InternalAvailableBlockSize = 0;

    File Externalpath;
    StatFs Externalstat;
    long ExternalBlockSize = 0, ExternalAvailableBlockSize = 0;



    private static final String ERROR = "Error" ;
    private TextView batteryTxt,DeviceTxt,OsTxt,NetworkTypeTxt,InternalMemoryTxt,ExternalMemoryTxt,WeatherTxt;


    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                    WeatherTxt.setText(weather_temperature);
                }
            });
            //asyncTask.execute("10.6783", "76.0537");

            asyncTask.execute(Latitude, Longitude);

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");

        batteryTxt = (TextView) this.findViewById(R.id.battery_level);
        DeviceTxt = (TextView) findViewById(R.id.device_name);
        OsTxt = (TextView) findViewById(R.id.os);
        NetworkTypeTxt = (TextView) findViewById(R.id.network_type);
        InternalMemoryTxt = (TextView) findViewById(R.id.internal_memory);
        ExternalMemoryTxt = (TextView) findViewById(R.id.external_memory);
        WeatherTxt = (TextView)findViewById(R.id.weather);

        batteryTxt.setText(BatteryPercentage.getBatteryPercentage(getApplicationContext())+"%");

        try {
            Internalpath = Environment.getExternalStorageDirectory();
            Internalstat = new StatFs(Internalpath.getPath());
        }catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        try {
            Externalpath = new File("/mnt/extSdCard");
            Externalstat = new StatFs(Externalpath.getPath());
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            ExternalMemoryTxt.setText("No Memory card");
        }

        try {
            if (isMyServiceRunning(MyService.class)) {
                System.out.println("Service already running");
            } else {
                startService(new Intent(HomeScreen.this, MyService.class));
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        try {

            String devicename = android.os.Build.MODEL;
            String myVersion = Build.VERSION.RELEASE;

            DeviceTxt.setText(devicename);
            OsTxt.setText(myVersion);

            checkNetworkType();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        try {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){

                InternalBlockSize = Internalstat.getBlockSizeLong();
                InternalAvailableBlockSize = Internalstat.getAvailableBlocksLong();
                String InternalMemoryAvailable = formatSize(InternalAvailableBlockSize * InternalBlockSize);
                InternalMemoryTxt.setText(InternalMemoryAvailable);

                if(externalMemoryAvailable()){
                    ExternalBlockSize = Externalstat.getBlockSizeLong();
                    ExternalAvailableBlockSize = Externalstat.getAvailableBlocksLong();
                    ExternalMemoryTxt.setText(formatSize(ExternalAvailableBlockSize * ExternalBlockSize));
                } else {
                    ExternalMemoryTxt.setText("No Memory card");
                }


            } else {

                InternalBlockSize = Internalstat.getBlockSize();
                InternalAvailableBlockSize = Internalstat.getAvailableBlocks();
                String InternalMemoryAvailable = formatSize(InternalAvailableBlockSize * InternalBlockSize);
                InternalMemoryTxt.setText(InternalMemoryAvailable);
                if(externalMemoryAvailable()){
                    ExternalBlockSize = Externalstat.getBlockSize();
                    ExternalAvailableBlockSize = Externalstat.getAvailableBlocks();
                    ExternalMemoryTxt.setText(formatSize(ExternalAvailableBlockSize * ExternalBlockSize));
                } else {
                    ExternalMemoryTxt.setText("No Memory card");
                }


            }



        }catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //####################################### find user location ###########################################

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSTracker(HomeScreen.this);

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

                    WeatherTxt.setText(weather_temperature);
                }
            });
            //asyncTask.execute("10.6783", "76.0537");

            asyncTask.execute(Latitude, Longitude);

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        CardView CardViewBatteryLevel = (CardView) findViewById(R.id.cardview_battery_level);
        CardView CardViewDevice = (CardView) findViewById(R.id.cardview_device);
        CardView CardViewNetwork = (CardView) findViewById(R.id.cardview_network);
        CardView CardViewStorage = (CardView) findViewById(R.id.cardview_storage);
        CardView CardViewWeather = (CardView) findViewById(R.id.cardview_weather);
        CardView CardViewInterval = (CardView) findViewById(R.id.cardview_interval);


        CardViewBatteryLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),BatteryLevel.class);
                startActivity(i);
            }
        });

        CardViewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Device.class);
                startActivity(i);
            }
        });

        CardViewNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Network.class);
                startActivity(i);
            }
        });

        CardViewStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Storage.class);
                startActivity(i);
            }
        });

        CardViewInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Interval.class);
                startActivity(i);
            }
        });

        CardViewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }


    public void checkNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                NetworkTypeTxt.setText(activeNetwork.getTypeName());

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                NetworkTypeTxt.setText(activeNetwork.getTypeName());
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_battery) {
            Intent i =new Intent(getApplicationContext(),BatteryLevel.class);
            startActivity(i);
        } else if (id == R.id.nav_device) {
            Intent i =new Intent(getApplicationContext(),Device.class);
            startActivity(i);
        } else if (id == R.id.nav_network) {
            Intent i =new Intent(getApplicationContext(),Network.class);
            startActivity(i);
        } else if (id == R.id.nav_storage) {
            Intent i =new Intent(getApplicationContext(),Storage.class);
            startActivity(i);
        } else if (id == R.id.nav_weather) {
            Intent i =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_github) {

            try{

                String url = "https://github.com/anoopt93";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (id == R.id.nav_whatsapp) {

            try {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9947425501"));
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
                if (size >= 1024) {
                    suffix = " GB";
                    size /= 1024;
                }
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println(df.format(size));

        String res = df.format(size);
        StringBuilder resultBuffer = new StringBuilder(res);
        //StringBuilder resultBuffer = new StringBuilder(Float.toString(size));

        /*
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        */

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
