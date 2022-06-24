package com.cecenet.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cecenet.company.features.alert.AlertActivity;
import com.cecenet.company.features.runningtext.RunningTextActivity;
import com.cecenet.company.features.videos.VideosActivity;
import com.cecenet.company.tools.ProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

@SuppressWarnings("ConstantConditions")
public class FeaturesActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtMainHeader;
    RadioGroup RGLanguage;
    Button btnTemplate, btnIdentity, btnVideos, btnRunningText, btnAlert, btnAnnouncement, btnQueue;
    FloatingActionButton FABSetting;

    BottomSheetDialog BSD;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        txtMainHeader   = findViewById(R.id.txtMainHeader);
        RGLanguage      = findViewById(R.id.RGLanguage);
        btnTemplate     = findViewById(R.id.btnTemplate);
        btnIdentity     = findViewById(R.id.btnIdentity);
        btnVideos       = findViewById(R.id.btnVideos);
        btnRunningText  = findViewById(R.id.btnRunningText);
        btnAlert        = findViewById(R.id.btnAlert);
        btnAnnouncement = findViewById(R.id.btnAnnouncement);
        btnQueue        = findViewById(R.id.btnQueue);
        FABSetting      = findViewById(R.id.FABSetting);

        BSD             = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
        progressDialog  = new ProgressDialog(this);

        btnTemplate.setOnClickListener(this);
        btnIdentity.setOnClickListener(this);
        btnVideos.setOnClickListener(this);
        btnRunningText.setOnClickListener(this);
        btnAlert.setOnClickListener(this);
        btnAnnouncement.setOnClickListener(this);
        btnQueue.setOnClickListener(this);

        txtMainHeader.setText(R.string.fitur_fitur);
        RGLanguage.setVisibility(View.GONE);

        BSD.setContentView(R.layout.view_bottom_setting);
        BSD.setCanceledOnTouchOutside(false);
        BSD.setDismissWithAnimation(true);
        showSetting();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnTemplate){

        }
        else if(v.getId() == R.id.btnIdentity){

        }
        else if(v.getId() == R.id.btnVideos){
            startActivity(new Intent(this, VideosActivity.class));
        }
        else if(v.getId() == R.id.btnRunningText){
            startActivity(new Intent(this, RunningTextActivity.class));
        }
        else if(v.getId() == R.id.btnAlert){
            startActivity(new Intent(this, AlertActivity.class));
        }
        else if(v.getId() == R.id.btnAnnouncement){

        }
        else if(v.getId() == R.id.btnQueue){

        }
    }

    private void showSetting(){
        FABSetting.setOnClickListener(v -> {
            BSD.setOnShowListener(dialog -> {
                FABSetting.setVisibility(View.GONE);
                BSD.findViewById(R.id.FABBottomSetting).animate().rotation(180f).setDuration(500).start();
            });

            BSD.setOnCancelListener(dialog -> {
                FABSetting.setVisibility(View.VISIBLE);
                BSD.findViewById(R.id.FABBottomSetting).animate().rotation(-180f).setDuration(500).start();
            });

            BSD.show();
            setOnClickSetting();
        });
    }

    private void setOnClickSetting(){
        FloatingActionButton FABBottomSetting   = BSD.findViewById(R.id.FABBottomSetting);
        Button btnGeneralSetting                = BSD.findViewById(R.id.btnGeneralSetting);
        Button btnPrivacySetting                = BSD.findViewById(R.id.btnPrivacySetting);
        Button btnWifiSetting                   = BSD.findViewById(R.id.btnWifiSetting);
        Button btnRefresh                       = BSD.findViewById(R.id.btnRefresh);
        Button btnRestart                       = BSD.findViewById(R.id.btnRestart);
        Button btnShutdown                      = BSD.findViewById(R.id.btnShutdown);
        Button btnLogout                        = BSD.findViewById(R.id.btnLogout);

        FABBottomSetting.setOnClickListener(v -> BSD.cancel());
    }
}