package com.cecenet.company;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cecenet.company.tools.CheckPermission;
import com.cecenet.company.tools.CheckWifi;
import com.cecenet.company.tools.DefaultString;
import com.cecenet.company.tools.Language;
import com.cecenet.company.tools.Preferences;

public class CheckRegistrationActivity extends AppCompatActivity {
    TextView txtMainHeader;
    RadioGroup RGLanguage;
    RadioButton RBIndonesian, RBEnglish;
    ImageView IVLoadingStep1, IVLoadingStep2, IVLoadingStep3, IVLoadingStep4, IVLoadingStep5;
    ImageView IVFinishStep1, IVFinishStep2, IVFinishStep3, IVFinishStep4, IVFinishStep5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_registration);

        txtMainHeader   = findViewById(R.id.txtMainHeader);
        RGLanguage      = findViewById(R.id.RGLanguage);
        RBIndonesian    = findViewById(R.id.RBIndonesian);
        RBEnglish       = findViewById(R.id.RBEnglish);
        IVLoadingStep1  = findViewById(R.id.IVLoadingStep1);
        IVLoadingStep2  = findViewById(R.id.IVLoadingStep2);
        IVLoadingStep3  = findViewById(R.id.IVLoadingStep3);
        IVLoadingStep4  = findViewById(R.id.IVLoadingStep4);
        IVLoadingStep5  = findViewById(R.id.IVLoadingStep5);
        IVFinishStep1   = findViewById(R.id.IVFinishStep1);
        IVFinishStep2   = findViewById(R.id.IVFinishStep2);
        IVFinishStep3   = findViewById(R.id.IVFinishStep3);
        IVFinishStep4   = findViewById(R.id.IVFinishStep4);
        IVFinishStep5   = findViewById(R.id.IVFinishStep5);

        Glide.with(this).asGif().load(R.drawable.img_loading).into(IVLoadingStep1);
        Glide.with(this).asGif().load(R.drawable.img_loading).into(IVLoadingStep2);
        Glide.with(this).asGif().load(R.drawable.img_loading).into(IVLoadingStep3);
        Glide.with(this).asGif().load(R.drawable.img_loading).into(IVLoadingStep4);
        Glide.with(this).asGif().load(R.drawable.img_loading).into(IVLoadingStep5);

        txtMainHeader.setText(getString(R.string.selamat_datang));

        checkPermission();
        getLanguage();
    }

    private void checkPermission(){
        if(CheckPermission.ACCESS_FINE_LOCATION(this)){
            if(CheckWifi.Connection(this).equals(DefaultString.WifiRasPi)){
                startActivity(new Intent(this, FeaturesActivity.class));
                finishAfterTransition();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CheckPermission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(CheckWifi.Connection(this).equals(DefaultString.WifiRasPi)){
                startActivity(new Intent(this, FeaturesActivity.class));
                finishAfterTransition();
            }
        }
    }

    public void getLanguage(){
        if(Preferences.getLanguage().equals("en")){
            RBEnglish.setChecked(true);
        }
        else{
            RBIndonesian.setChecked(true);
        }

        setLanguage();
    }

    public void setLanguage(){
        RGLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            if(RBIndonesian.isChecked()){
                Preferences.setLanguage("id");
                restartActivity();
            }
            else if(RBEnglish.isChecked()){
                Preferences.setLanguage("en");
                restartActivity();
            }
        });
    }

    private void restartActivity(){
        startActivity(new Intent(this, CheckRegistrationActivity.class));
        overridePendingTransition(0, 0);
        finishAfterTransition();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.getLanguage(newBase));
    }
}