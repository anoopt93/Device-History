package com.sample.anoop.anoop;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;

public class Storage extends AppCompatActivity {

    ImageView ImgInternal,ImgExternal;
    TextView TxtInternal,TxtExternal;
    Button BtnStorageHistory;

    File Internalpath;
    StatFs Internalstat;
    long InternalBlockSize = 0, InternalAvailableBlockSize = 0;

    File Externalpath;
    StatFs Externalstat;
    long ExternalBlockSize = 0, ExternalAvailableBlockSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_storage);

        ImgInternal = (ImageView) findViewById(R.id.img_internal);
        ImgExternal = (ImageView) findViewById(R.id.img_external);
        TxtInternal = (TextView) findViewById(R.id.txt_internal);
        TxtExternal = (TextView) findViewById(R.id.txt_external);
        BtnStorageHistory = (Button) findViewById(R.id.btn_storage_history);

        BtnStorageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),StorageHistory.class);
                startActivity(i);
            }
        });

        try {
            Internalpath = Environment.getExternalStorageDirectory();
            Internalstat = new StatFs(Internalpath.getPath());
        }catch (Exception e) {
           // Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        try {
            Externalpath = new File("/mnt/extSdCard");
            Externalstat = new StatFs(Externalpath.getPath());
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            TxtExternal.setText("No Memory card");
        }



        try {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){

                InternalBlockSize = Internalstat.getBlockSizeLong();
                InternalAvailableBlockSize = Internalstat.getAvailableBlocksLong();
                String InternalMemoryAvailable = formatSize(InternalAvailableBlockSize * InternalBlockSize);
                TxtInternal.setText(InternalMemoryAvailable);

                if(externalMemoryAvailable()){
                    ExternalBlockSize = Externalstat.getBlockSizeLong();
                    ExternalAvailableBlockSize = Externalstat.getAvailableBlocksLong();
                    TxtExternal.setText(formatSize(ExternalAvailableBlockSize * ExternalBlockSize));
                } else {
                    TxtExternal.setText("No Memory card");
                }


            } else {

                InternalBlockSize = Internalstat.getBlockSize();
                InternalAvailableBlockSize = Internalstat.getAvailableBlocks();
                String InternalMemoryAvailable = formatSize(InternalAvailableBlockSize * InternalBlockSize);
                TxtInternal.setText(InternalMemoryAvailable);
                if(externalMemoryAvailable()){
                    ExternalBlockSize = Externalstat.getBlockSize();
                    ExternalAvailableBlockSize = Externalstat.getAvailableBlocks();
                    TxtExternal.setText(formatSize(ExternalAvailableBlockSize * ExternalBlockSize));
                } else {
                    TxtExternal.setText("No Memory card");
                }


            }



        }catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

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

}
