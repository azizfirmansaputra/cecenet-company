package com.cecenet.company.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class CheckWifi {

    public static String Connection(Context context){
        ConnectivityManager CM  = context.getSystemService(ConnectivityManager.class);
        NetworkCapabilities NC  = CM.getNetworkCapabilities(CM.getActiveNetwork());
        LinkProperties LP       = CM.getLinkProperties(CM.getActiveNetwork());

        if(NC != null){
            if(NC.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                if(LP.getLinkAddresses().get(1).toString().startsWith(DefaultString.IPAddress)){
                    return DefaultString.WifiRasPi;
                }
                else{
                    return DefaultString.NoWifiRasPi;
                }
            }
            else{
                return DefaultString.NoWifi;
            }
        }
        else{
            return DefaultString.NoConnection;
        }
    }

    private static String getSSID(){
        AndroidNetworking.post(DefaultString.URL_API_SELECT)
                .addBodyParameter(DefaultString.setting_type, "Wifi")
                .addBodyParameter(DefaultString.setting_name, "SSID")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Preferences.setSSID(response);
                    }

                    @Override
                    public void onError(ANError anError) { }
                });

        return Preferences.getSSID();
    }
}