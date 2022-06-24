package com.cecenet.company.features.videos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cecenet.company.R;

import java.util.Objects;

public class VideosPlay extends AppCompatActivity {
    WebView WVPlayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        WVPlayVideo         = findViewById(R.id.WVPlayVideo);

        String VideoName    = getIntent().getStringExtra("VideoName");
        String VideoPath    = getIntent().getStringExtra("VideoPath");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(VideoName.toUpperCase());
        playVideo(VideoPath);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    private void playVideo(String videoPath){
        WVPlayVideo.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
        WVPlayVideo.getSettings().setLoadWithOverviewMode(true);
        WVPlayVideo.getSettings().setUseWideViewPort(true);
        WVPlayVideo.getSettings().setAllowFileAccess(true);
        WVPlayVideo.setBackgroundColor(Color.TRANSPARENT);
        WVPlayVideo.setInitialScale(1);
        WVPlayVideo.loadUrl(videoPath);
    }
}