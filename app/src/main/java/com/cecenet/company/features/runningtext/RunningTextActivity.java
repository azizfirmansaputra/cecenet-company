package com.cecenet.company.features.runningtext;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cecenet.company.R;
import com.cecenet.company.tools.DefaultString;
import com.cecenet.company.tools.ProgressDialog;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RunningTextActivity extends AppCompatActivity {
    EditText ETRunningText;
    ImageButton IBCancelEditRunningText, IBDeleteActiveRunningText, IBDeleteArchiveRunningText, IBActiveRunningText, IBArchiveRunningText;
    Button btnAddRunningText;
    RecyclerView RVActiveRunningText, RVArchiveRunningText;
    LinearLayout LLMenuActiveRunningText, LLMenuArchiveRunningText;

    ArrayList<RunningText> runningTextArrayListActive, runningTextArrayListArchive;
    RunningTextAdapter runningTextAdapterActive, runningTextAdapterArchive;
    LinearLayoutManager LLMActiveRunningText, LLMArchiveRunningText;
    InputMethodManager inputMethodManager;
    ProgressDialog progressDialog;
    String id, content, visible;
    int position;

    public int selectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_text);

        ETRunningText               = findViewById(R.id.ETRunningText);
        IBCancelEditRunningText     = findViewById(R.id.IBCancelEditRunningText);
        IBDeleteActiveRunningText   = findViewById(R.id.IBDeleteActiveRunningText);
        IBDeleteArchiveRunningText  = findViewById(R.id.IBDeleteArchiveRunningText);
        IBActiveRunningText         = findViewById(R.id.IBActiveRunningText);
        IBArchiveRunningText        = findViewById(R.id.IBArchiveRunningText);
        btnAddRunningText           = findViewById(R.id.btnAddRunningText);
        RVActiveRunningText         = findViewById(R.id.RVActiveRunningText);
        RVArchiveRunningText        = findViewById(R.id.RVArchiveRunningText);
        LLMenuActiveRunningText     = findViewById(R.id.LLMenuActiveRunningText);
        LLMenuArchiveRunningText    = findViewById(R.id.LLMenuArchiveRunningText);

        progressDialog              = new ProgressDialog(this);
        inputMethodManager          = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        runningTextArrayListActive  = new ArrayList<>();
        runningTextArrayListArchive = new ArrayList<>();
        runningTextAdapterActive    = new RunningTextAdapter(this, runningTextArrayListActive);
        runningTextAdapterArchive   = new RunningTextAdapter(this, runningTextArrayListArchive);
        LLMActiveRunningText        = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LLMArchiveRunningText       = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        IBCancelEditRunningText.setVisibility(View.GONE);
        LLMenuActiveRunningText.setVisibility(View.GONE);
        LLMenuArchiveRunningText.setVisibility(View.GONE);

        RVActiveRunningText.setItemViewCacheSize(20);
        RVActiveRunningText.setAdapter(runningTextAdapterActive);
        RVActiveRunningText.setLayoutManager(LLMActiveRunningText);
        RVActiveRunningText.setItemAnimator(new DefaultItemAnimator());
        RVActiveRunningText.addItemDecoration(new DividerItemDecoration(this, LLMActiveRunningText.getOrientation()));

        RVArchiveRunningText.setItemViewCacheSize(20);
        RVArchiveRunningText.setAdapter(runningTextAdapterArchive);
        RVArchiveRunningText.setLayoutManager(LLMArchiveRunningText);
        RVArchiveRunningText.setItemAnimator(new DefaultItemAnimator());
        RVArchiveRunningText.addItemDecoration(new DividerItemDecoration(this, LLMArchiveRunningText.getOrientation()));

        addRunningText();
        getRunningText(true);
        getRunningText(false);
    }

    private void addRunningText(){
        btnAddRunningText.setOnClickListener(v -> {
            if(!ETRunningText.getText().toString().trim().isEmpty()){
                if(btnAddRunningText.getText().toString().equals(getString(R.string.tambah_running_text))){
                    addUpdateDeleteRunningText(DefaultString.URL_API_INSERT, "", ETRunningText.getText().toString(), "Yes");
                }
                else{
                    addUpdateDeleteRunningText(DefaultString.URL_API_UPDATE, id, ETRunningText.getText().toString(), visible);
                }
            }
        });
    }

    private void addUpdateDeleteRunningText(String URL, String id, String content, String visible){
        AndroidNetworking.post(URL)
                .addBodyParameter(DefaultString.running_text_id, id)
                .addBodyParameter(DefaultString.running_text_content, content)
                .addBodyParameter(DefaultString.running_text_visible, visible)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        switch(response){
                            case "Added":
                                selectPosition = -1;
                                selectionRunningText(true, selectPosition);

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if(runningTextArrayListActive.size() != 0){
                                        resetETRunningText();
                                        RVActiveRunningText.smoothScrollToPosition(runningTextArrayListActive.size());
                                    }
                                }, 500);

                                break;
                            case "Updated":
                                if(selectPosition != -1){
                                    if(visible.equals("Yes")){
                                        runningTextArrayListArchive.get(position).setVisible(visible);
                                        runningTextArrayListActive.add(runningTextArrayListArchive.get(position));
                                        runningTextAdapterActive.notifyItemChanged(runningTextArrayListActive.size());
                                        RVActiveRunningText.smoothScrollToPosition(runningTextArrayListActive.size());

                                        runningTextArrayListArchive.remove(position);
                                        LLMenuArchiveRunningText.setVisibility(View.GONE);
                                        runningTextAdapterArchive.notifyItemRemoved(position);
                                    }
                                    else{
                                        runningTextArrayListActive.get(position).setVisible(visible);
                                        runningTextArrayListArchive.add(runningTextArrayListActive.get(position));
                                        runningTextAdapterArchive.notifyItemChanged(runningTextArrayListArchive.size());
                                        RVArchiveRunningText.smoothScrollToPosition(runningTextArrayListArchive.size());

                                        runningTextArrayListActive.remove(position);
                                        LLMenuActiveRunningText.setVisibility(View.GONE);
                                        runningTextAdapterActive.notifyItemRemoved(position);
                                    }
                                    selectPosition = -1;
                                }
                                else{
                                    if(visible.equals("Yes")){
                                        runningTextArrayListActive.get(position).setContent(content);
                                        runningTextAdapterActive.notifyItemChanged(position);
                                        RVActiveRunningText.smoothScrollToPosition(position);
                                    }
                                    else{
                                        runningTextArrayListArchive.get(position).setContent(content);
                                        runningTextAdapterArchive.notifyItemChanged(position);
                                        RVArchiveRunningText.smoothScrollToPosition(position);
                                    }
                                    resetETRunningText();
                                }

                                break;
                            case "Deleted":
                                if(visible.equals("Yes")){
                                    runningTextArrayListActive.remove(position);
                                    LLMenuActiveRunningText.setVisibility(View.GONE);
                                    runningTextAdapterActive.notifyItemRemoved(position);
                                }
                                else{
                                    runningTextArrayListArchive.remove(position);
                                    LLMenuArchiveRunningText.setVisibility(View.GONE);
                                    runningTextAdapterArchive.notifyItemRemoved(position);
                                }
                                selectPosition = -1;
                                progressDialog.dismiss();

                                break;
                            default:
                                progressDialogError(getString(R.string.gagal), response);
                                break;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(getString(R.string.error), anError.getMessage());
                    }
                });
    }

    private void resetETRunningText(){
        if(getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        btnAddRunningText.setText(getString(R.string.tambah_running_text));
        IBCancelEditRunningText.setVisibility(View.GONE);
        ETRunningText.getText().clear();
        ETRunningText.clearFocus();
    }

    private void progressDialogError(String title, String message){
        progressDialog.setPositiveButton(getString(R.string.ok), v -> progressDialog.dismiss());
        progressDialog.setAnimation(R.drawable.icon_error);
        progressDialog.setDeterminate(false);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.show();
    }

    private void getRunningText(boolean visible){
        if(visible){
            runningTextArrayListActive.clear();
        }
        else{
            runningTextArrayListArchive.clear();
        }

        AndroidNetworking.post(DefaultString.URL_API_SELECT)
                .addBodyParameter(DefaultString.running_text_id, "")
                .addBodyParameter(DefaultString.running_text_visible, visible ? "Yes" : "No")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject       = jsonArray.getJSONObject(i);
                                String running_text_id      = jsonObject.getString(DefaultString.running_text_id);
                                String running_text_content = jsonObject.getString(DefaultString.running_text_content);
                                String running_text_visible = jsonObject.getString(DefaultString.running_text_visible);

                                if(visible){
                                    runningTextArrayListActive.add(new RunningText(running_text_id, running_text_content, running_text_visible));
                                    runningTextAdapterActive.notifyItemChanged(i);
                                }
                                else{
                                    runningTextArrayListArchive.add(new RunningText(running_text_id, running_text_content, running_text_visible));
                                    runningTextAdapterArchive.notifyItemChanged(i);
                                }
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialogError(getString(R.string.error), anError.getMessage());
                    }
                });
    }

    public void doubleClickRunningText(boolean visible, int position){
        if(visible){
            getDataRunningText(true, position);
            ETRunningText.setText(runningTextArrayListActive.get(position).getContent());
        }
        else{
            getDataRunningText(false, position);
            ETRunningText.setText(runningTextArrayListArchive.get(position).getContent());
        }

        ETRunningText.clearFocus();
        btnAddRunningText.setText(getString(R.string.edit_running_text));
        IBCancelEditRunningText.setVisibility(View.VISIBLE);
        IBCancelEditRunningText.setOnClickListener(v -> resetETRunningText());
    }

    private void getDataRunningText(boolean visible, int position){
        if(visible){
            this.position   = position;
            this.id         = runningTextArrayListActive.get(position).getId();
            this.content    = runningTextArrayListActive.get(position).getContent();
            this.visible    = runningTextArrayListActive.get(position).getVisible();
        }
        else{
            this.position   = position;
            this.id         = runningTextArrayListArchive.get(position).getId();
            this.content    = runningTextArrayListArchive.get(position).getContent();
            this.visible    = runningTextArrayListArchive.get(position).getVisible();
        }
    }

    public void selectionRunningText(boolean visible, int position){
        if(position != -1){
            if(visible){
                LLMenuActiveRunningText.setVisibility(View.VISIBLE);

                if(LLMenuArchiveRunningText.getVisibility() == View.VISIBLE){
                    getRunningText(false);
                    LLMenuArchiveRunningText.setVisibility(View.GONE);
                }

                getDataRunningText(true, position);
                IBDeleteActiveRunningText.setOnClickListener(v -> confirmDelete());
                IBArchiveRunningText.setOnClickListener(v -> addUpdateDeleteRunningText(DefaultString.URL_API_UPDATE, id, content, "No"));
            }
            else{
                LLMenuArchiveRunningText.setVisibility(View.VISIBLE);

                if(LLMenuActiveRunningText.getVisibility() == View.VISIBLE){
                    getRunningText(true);
                    LLMenuActiveRunningText.setVisibility(View.GONE);
                }

                getDataRunningText(false, position);
                IBDeleteArchiveRunningText.setOnClickListener(v -> confirmDelete());
                IBActiveRunningText.setOnClickListener(v -> addUpdateDeleteRunningText(DefaultString.URL_API_UPDATE, id, content, "Yes"));
            }
            resetETRunningText();
        }
        else{
            getRunningText(true);
            getRunningText(false);
            LLMenuActiveRunningText.setVisibility(View.GONE);
            LLMenuArchiveRunningText.setVisibility(View.GONE);
        }
    }

    private void confirmDelete(){
        progressDialog.setMessage(getString(R.string.apakah_yakin_ingin_menghapus_running_text_ini_));
        progressDialog.setAnimation(R.drawable.icon_delete);
        progressDialog.setIcon(R.drawable.icon_add_text);
        progressDialog.setTitle(getString(R.string.konfirmasi_hapus));
        progressDialog.setAnimationTint(Color.RED);
        progressDialog.setDeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        progressDialog.setNegativeButton(getString(R.string.tidak).toUpperCase(), v -> progressDialog.dismiss());
        progressDialog.setPositiveButton(getString(R.string.ya).toUpperCase(), v -> addUpdateDeleteRunningText(DefaultString.URL_API_DELETE, id, content, visible));
    }

    @Override
    public void onBackPressed() {
        if(selectPosition != -1){
            selectPosition = -1;
            selectionRunningText(true, selectPosition);
        }
        else{
            super.onBackPressed();
        }
    }
}