package com.cecenet.company.tools;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.getSP(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());
    }
}