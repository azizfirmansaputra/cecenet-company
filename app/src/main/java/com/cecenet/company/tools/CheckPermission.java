package com.cecenet.company.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class CheckPermission {
    public static int ACCESS_FINE_LOCATION  = 0;
    public static int READ_EXTERNAL_STORAGE = 1;

    public static boolean ACCESS_FINE_LOCATION(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean READ_EXTERNAL_STORAGE(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
            return false;
        }
        else{
            return true;
        }
    }
}