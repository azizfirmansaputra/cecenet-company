package com.cecenet.company.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.cecenet.company.R;

public class Preferences {
    private static SharedPreferences SP;

    public static String Key_Language   = "Language";
    public static String Key_SSID       = "SSID";
    public static String Key_Password   = "Password";

    public static void getSP(Context context){
        SP = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static void setLanguage(String languageCode){
        SP.edit().putString(Key_Language, languageCode).apply();
    }

    public static String getLanguage(){
        return SP.getString(Key_Language, "");
    }

    public static void setSSID(String SSID){
        SP.edit().putString(Key_SSID, SSID).apply();
    }

    public static String getSSID(){
        return SP.getString(Key_SSID, "");
    }
}