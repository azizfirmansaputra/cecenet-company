package com.cecenet.company.features.videos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.cecenet.company.R;
import com.cecenet.company.tools.CheckPermission;
import com.cecenet.company.tools.DefaultString;
import com.cecenet.company.tools.ProgressDialog;
import com.cecenet.company.tools.FileDetails;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideosActivity extends AppCompatActivity {
    ImageView IVUploadVideo;
    TextView txtDurationVideo;
    Button btnUploadVideo;
    RecyclerView RVActiveVideo, RVArchiveVideo;
    LinearLayout LLMenuActiveVideo, LLMenuArchiveVideo;
    ImageButton IBDeleteVideo, IBDeleteActiveVideo, IBArchiveVideo, IBDeleteArchiveVideo, IBActiveVideo, IBUnmuteVideo, IBMuteVideo;

    ArrayList<Videos> videosArrayListActive, videosArrayListArchive;
    VideosAdapter videosAdapterActive, videosAdapterArchive;
    LinearLayoutManager LLMActiveVideo, LLMArchiveVideo;
    ActivityResultLauncher<String> selectVideo;
    ProgressDialog progressDialog;
    String videoName, videoPath;

    public int selectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        IVUploadVideo           = findViewById(R.id.IVUploadVideo);
        txtDurationVideo        = findViewById(R.id.txtDurationVideo);
        btnUploadVideo          = findViewById(R.id.btnUploadVideo);
        RVActiveVideo           = findViewById(R.id.RVActiveVideo);
        RVArchiveVideo          = findViewById(R.id.RVArchiveVideo);
        LLMenuActiveVideo       = findViewById(R.id.LLMenuActiveVideo);
        LLMenuArchiveVideo      = findViewById(R.id.LLMenuArchiveVideo);
        IBDeleteVideo           = findViewById(R.id.IBDeleteVideo);
        IBDeleteActiveVideo     = findViewById(R.id.IBDeleteActiveVideo);
        IBArchiveVideo          = findViewById(R.id.IBArchiveVideo);
        IBDeleteArchiveVideo    = findViewById(R.id.IBDeleteArchiveVideo);
        IBActiveVideo           = findViewById(R.id.IBActiveVideo);
        IBUnmuteVideo           = findViewById(R.id.IBUnmuteVideo);
        IBMuteVideo             = findViewById(R.id.IBMuteVideo);

        progressDialog          = new ProgressDialog(this);

        videosArrayListActive   = new ArrayList<>();
        videosArrayListArchive  = new ArrayList<>();
        videosAdapterActive     = new VideosAdapter(this, videosArrayListActive);
        videosAdapterArchive    = new VideosAdapter(this, videosArrayListArchive);
        LLMActiveVideo          = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LLMArchiveVideo         = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        IBDeleteVideo.setVisibility(View.GONE);
        txtDurationVideo.setVisibility(View.GONE);
        LLMenuActiveVideo.setVisibility(View.GONE);
        LLMenuArchiveVideo.setVisibility(View.GONE);

        RVActiveVideo.setItemViewCacheSize(20);
        RVActiveVideo.setAdapter(videosAdapterActive);
        RVActiveVideo.setLayoutManager(LLMActiveVideo);
        RVActiveVideo.setItemAnimator(new DefaultItemAnimator());

        RVArchiveVideo.setItemViewCacheSize(20);
        RVArchiveVideo.setAdapter(videosAdapterArchive);
        RVArchiveVideo.setLayoutManager(LLMArchiveVideo);
        RVArchiveVideo.setItemAnimator(new DefaultItemAnimator());

        checkPermission();
        selectVideo();

        getVideo(true);
        getVideo(false);
        getVideoSoundSetting();
    }

    private void checkPermission(){
        btnUploadVideo.setOnClickListener(v -> {
            if(btnUploadVideo.getText().toString().equals(getString(R.string.pilih_video))){
                if(CheckPermission.READ_EXTERNAL_STORAGE(this)){
                    selectVideo.launch("video/*");
                }
            }
            else{
                uploadVideo();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CheckPermission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectVideo.launch("video/*");
        }
    }

    private void selectVideo(){
        selectVideo = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null){
                IVUploadVideo.setPadding(0, 0, 0, 0);
                Glide.with(this).asBitmap().load(result).into(IVUploadVideo);

                btnUploadVideo.setText(getString(R.string.upload_video));
                txtDurationVideo.setText(FileDetails.getDuration(this, result));

                IBDeleteVideo.setVisibility(View.VISIBLE);
                txtDurationVideo.setVisibility(View.VISIBLE);

                IVUploadVideo.setOnClickListener(v -> playVideo());
                IBDeleteVideo.setOnClickListener(v -> resetVideo());

                videoName   = FileDetails.getFileName(this, result);
                videoPath   = FileDetails.getFilePath(this, result);
            }
        });
    }

    private void resetVideo(){
        IVUploadVideo.setPadding(45, 45, 45, 45);
        IVUploadVideo.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.img_videos));

        txtDurationVideo.setText("");
        btnUploadVideo.setText(getString(R.string.pilih_video));

        IBDeleteVideo.setVisibility(View.GONE);
        txtDurationVideo.setVisibility(View.GONE);

        videoName   = "";
        videoPath   = "";
    }

    private void playVideo(){
        if(!videoName.isEmpty() && !videoPath.isEmpty()){
            IVUploadVideo.setTransitionName("PlayVideo");

            ActivityOptionsCompat AOC   = ActivityOptionsCompat.makeSceneTransitionAnimation(this, IVUploadVideo, "PlayVideo");
            Intent intentPlayVideo      = new Intent(this, VideosPlay.class);

            startActivity(intentPlayVideo.putExtra("VideoName", videoName).putExtra("VideoPath", videoPath), AOC.toBundle());
        }
    }

    private void uploadVideo(){
        progressDialog.setMessage(getString(R.string.proses_upload_sedang_berlangsung));
        progressDialog.setIcon(R.drawable.icon_upload);
        progressDialog.setTitle(getString(R.string.upload_video));
        progressDialog.setDeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.upload(DefaultString.URL_API_INSERT)
                .addMultipartParameter(DefaultString.video_name, videoName)
                .addMultipartFile(DefaultString.video_file, new File(videoPath))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                    progressDialog.setDeterminate(true);
                    progressDialog.setProgress(Integer.parseInt(String.valueOf((bytesUploaded * 100)/totalBytes)));
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Uploaded")){
                            progressDialog.setTitle(getString(R.string.berhasil_upload));
                            progressDialog.setAnimation(R.drawable.icon_check);
                            progressDialog.setMessage(getString(R.string.video_telah_berhasil_di_upload_));
                            selectPosition = -1;
                            selectionVideo(true, selectPosition);
                        }
                        else if(response.equals("Exist")){
                            progressDialog.setTitle(getString(R.string.gagal_upload));
                            progressDialog.setAnimation(R.drawable.icon_error);
                            progressDialog.setMessage(getString(R.string.file_video_sudah_ada_));
                        }
                        else{
                            progressDialog.setMessage(response);
                            progressDialog.setTitle(R.string.gagal_upload);
                            progressDialog.setAnimation(R.drawable.icon_error);
                        }

                        progressDialog.setDeterminate(false);
                        progressDialog.setPositiveButton(getString(R.string.ok), v -> {
                            RVActiveVideo.smoothScrollToPosition(videosArrayListActive.size());
                            progressDialog.dismiss();
                            resetVideo();
                        });
                    }

                    @Override
                    public void onError(ANError anError) {
                       progressDialogError(anError);
                    }
                });
    }

    private void progressDialogError(ANError anError){
        progressDialog.setPositiveButton(getString(R.string.ok), v -> progressDialog.dismiss());
        progressDialog.setAnimation(R.drawable.icon_error);
        progressDialog.setMessage(anError.getMessage());
        progressDialog.setDeterminate(false);
        progressDialog.setTitle(getString(R.string.error));
        progressDialog.show();
    }

    private void getVideo(boolean visible){
        if(visible){
            videosArrayListActive.clear();
        }
        else{
            videosArrayListArchive.clear();
        }

        AndroidNetworking.post(DefaultString.URL_API_SELECT)
                .addBodyParameter(DefaultString.video_name, "videos")
                .addBodyParameter(DefaultString.video_visible, visible ? "Yes" : "No")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                String video_id         = jsonObject.getString(DefaultString.video_id);
                                String video_name       = jsonObject.getString(DefaultString.video_name);
                                String video_visible    = jsonObject.getString(DefaultString.video_visible);

                                if(visible){
                                    videosArrayListActive.add(new Videos(video_id, video_name, video_visible));
                                    videosAdapterActive.notifyItemChanged(i);
                                }
                                else{
                                    videosArrayListArchive.add(new Videos(video_id, video_name, video_visible));
                                    videosAdapterArchive.notifyItemChanged(i);
                                }
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(anError);
                    }
                });
    }

    public void selectionVideo(boolean visible, int position){
        if(position != -1){
            if(visible){
                LLMenuActiveVideo.setVisibility(View.VISIBLE);

                if(LLMenuArchiveVideo.getVisibility() == View.VISIBLE){
                    getVideo(false);
                    LLMenuArchiveVideo.setVisibility(View.GONE);
                }

                IBArchiveVideo.setOnClickListener(v -> updateVideo(true, position));
                IBDeleteActiveVideo.setOnClickListener(v -> confirmDelete(true, position));
            }
            else{
                LLMenuArchiveVideo.setVisibility(View.VISIBLE);

                if(LLMenuActiveVideo.getVisibility() == View.VISIBLE){
                    getVideo(true);
                    LLMenuActiveVideo.setVisibility(View.GONE);
                }

                IBActiveVideo.setOnClickListener(v -> updateVideo(false, position));
                IBDeleteArchiveVideo.setOnClickListener(v -> confirmDelete(false, position));
            }
        }
        else{
            getVideo(true);
            getVideo(false);
            LLMenuActiveVideo.setVisibility(View.GONE);
            LLMenuArchiveVideo.setVisibility(View.GONE);
        }
    }

    private void confirmDelete(boolean visible, int position){
        progressDialog.setMessage(getString(R.string.apakah_yakin_ingin_menghapus_video_ini_));
        progressDialog.setAnimation(R.drawable.icon_delete);
        progressDialog.setIcon(R.drawable.img_videos);
        progressDialog.setTitle(getString(R.string.konfirmasi_hapus));
        progressDialog.setAnimationTint(Color.RED);
        progressDialog.setDeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        progressDialog.setNegativeButton(getString(R.string.tidak).toUpperCase(), v -> progressDialog.dismiss());
        progressDialog.setPositiveButton(getString(R.string.ya).toUpperCase(), v -> deleteVideo(visible, position));
    }

    private void deleteVideo(boolean visible, int position){
        progressDialog.setCancelable(false);
        String video_id, video_name;

        if(visible){
            video_id    = videosArrayListActive.get(position).getId();
            video_name  = videosArrayListActive.get(position).getName();
        }
        else{
            video_id    = videosArrayListArchive.get(position).getId();
            video_name  = videosArrayListArchive.get(position).getName();
        }

        AndroidNetworking.post(DefaultString.URL_API_DELETE)
                .addBodyParameter(DefaultString.video_id, video_id)
                .addBodyParameter(DefaultString.video_name, video_name)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Deleted")){
                            if(visible){
                                videosArrayListActive.remove(position);
                                LLMenuActiveVideo.setVisibility(View.GONE);
                                videosAdapterActive.notifyItemRemoved(position);
                            }
                            else{
                                videosArrayListArchive.remove(position);
                                LLMenuArchiveVideo.setVisibility(View.GONE);
                                videosAdapterArchive.notifyItemRemoved(position);
                            }
                            selectPosition = -1;
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.setMessage(response);
                            progressDialog.setTitle(getString(R.string.gagal_hapus));
                            progressDialog.setAnimation(R.drawable.icon_error);
                            progressDialog.setPositiveButton(getString(R.string.ok), v -> progressDialog.dismiss());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(anError);
                    }
                });
    }

    private void updateVideo(boolean visible, int position){
        String video_id, video_name;

        if(visible){
            video_id    = videosArrayListActive.get(position).getId();
            video_name  = videosArrayListActive.get(position).getName();
        }
        else{
            video_id    = videosArrayListArchive.get(position).getId();
            video_name  = videosArrayListArchive.get(position).getName();
        }

        AndroidNetworking.post(DefaultString.URL_API_UPDATE)
                .addBodyParameter(DefaultString.video_id, video_id)
                .addBodyParameter(DefaultString.video_name, video_name)
                .addBodyParameter(DefaultString.video_visible, visible ? "No" : "Yes")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Updated")){
                            if(visible){
                                videosArrayListActive.get(position).setVisible("No");
                                videosArrayListArchive.add(videosArrayListActive.get(position));
                                videosAdapterArchive.notifyItemChanged(videosArrayListArchive.size());
                                RVArchiveVideo.smoothScrollToPosition(videosArrayListArchive.size());

                                videosArrayListActive.remove(position);
                                LLMenuActiveVideo.setVisibility(View.GONE);
                                videosAdapterActive.notifyItemRemoved(position);
                            }
                            else{
                                videosArrayListArchive.get(position).setVisible("Yes");
                                videosArrayListActive.add(videosArrayListArchive.get(position));
                                videosAdapterActive.notifyItemChanged(videosArrayListActive.size());
                                RVActiveVideo.smoothScrollToPosition(videosArrayListActive.size());

                                videosArrayListArchive.remove(position);
                                LLMenuArchiveVideo.setVisibility(View.GONE);
                                videosAdapterArchive.notifyItemRemoved(position);
                            }
                            selectPosition = -1;
                        }
                        else{
                            progressDialog.setPositiveButton(getString(R.string.ok), v -> progressDialog.dismiss());
                            progressDialog.setAnimation(R.drawable.icon_error);
                            progressDialog.setTitle(getString(R.string.gagal_update));
                            progressDialog.setMessage(response);
                            progressDialog.show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(anError);
                    }
                });
    }

    private void getVideoSoundSetting(){
        AndroidNetworking.post(DefaultString.URL_API_SELECT)
                .addBodyParameter(DefaultString.setting_type, "Background")
                .addBodyParameter(DefaultString.setting_name, "Sound")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Mute Video")){
                            IBMuteVideo.setEnabled(false);
                            IBUnmuteVideo.setEnabled(true);
                            IBUnmuteVideo.setOnClickListener(v -> setVideoSoundSetting("Unmute Video"));
                            IBMuteVideo.setColorFilter(ContextCompat.getColor(VideosActivity.this, R.color.bgColorBlack));
                            IBUnmuteVideo.setColorFilter(ContextCompat.getColor(VideosActivity.this, R.color.bgColorGray));
                        }
                        else{
                            IBMuteVideo.setEnabled(true);
                            IBUnmuteVideo.setEnabled(false);
                            IBMuteVideo.setOnClickListener(v -> setVideoSoundSetting("Mute Video"));
                            IBMuteVideo.setColorFilter(ContextCompat.getColor(VideosActivity.this, R.color.bgColorGray));
                            IBUnmuteVideo.setColorFilter(ContextCompat.getColor(VideosActivity.this, R.color.bgColorBlack));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(anError);
                    }
                });
    }

    private void setVideoSoundSetting(String setting_content){
        AndroidNetworking.post(DefaultString.URL_API_UPDATE)
                .addBodyParameter(DefaultString.setting_type, "Background")
                .addBodyParameter(DefaultString.setting_name, "Sound")
                .addBodyParameter(DefaultString.setting_content, setting_content)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Updated")){
                            getVideoSoundSetting();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(anError);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(selectPosition != -1){
            selectPosition = -1;
            selectionVideo(true, selectPosition);
        }
        else{
            super.onBackPressed();
        }
    }
}