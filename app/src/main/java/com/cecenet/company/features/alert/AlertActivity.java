package com.cecenet.company.features.alert;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cecenet.company.R;
import com.cecenet.company.tools.DefaultString;
import com.cecenet.company.tools.ProgressDialog;
import com.cecenet.company.tools.Editor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlertActivity extends AppCompatActivity {
    RecyclerView RVAlert;
    FloatingActionButton FABAddAlert;

    TimePicker TPAlertDialog;
    ImageButton IBCalenderAlertDialog;
    ImageView IVCloseAlertDialog, IVSaveAlertDialog;
    LinearLayout LLLabelAlertDialog, LLSoundAlertDialog, LLAnimationAlertDialog;
    TextView txtTitleAlertDialog, txtDateAlertDialog, txtLabelAlertDialog, txtSoundAlertDialog, txtAnimationAlertDialog;
    CheckBox CBMondayAlertDialog, CBTuesdayAlertDialog, CBWednesdayAlertDialog, CBThursdayAlertDialog, CBFridayAlertDialog, CBSaturdayAlertDialog, CBSundayAlertDialog;

    ActivityResultLauncher<Intent> showEditor, selectSound, selectAnimation;
    ArrayList<String> mediaArrayList;
    ArrayList<Alert> alertArrayList;
    ProgressDialog progressDialog;
    LinearLayoutManager LLMAlert;
    AlertAdapter alertAdapter;
    Dialog dialog;

    public int selectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        RVAlert         = findViewById(R.id.RVAlert);
        FABAddAlert     = findViewById(R.id.FABAddAlert);

        alertArrayList  = new ArrayList<>();
        dialog          = new Dialog(this);
        progressDialog  = new ProgressDialog(this);
        alertAdapter    = new AlertAdapter(this, alertArrayList);
        LLMAlert        = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RVAlert.setItemViewCacheSize(20);
        RVAlert.setAdapter(alertAdapter);
        RVAlert.setLayoutManager(LLMAlert);
        RVAlert.setItemAnimator(new DefaultItemAnimator());

        dialog.setContentView(R.layout.view_alert_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getAlert();
        addAlert();
        showEditor();
    }

    private void getAlert(){
        alertArrayList.clear();

        AndroidNetworking.post(DefaultString.URL_API_SELECT)
                .addBodyParameter(DefaultString.reminder_id, "")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject       = jsonArray.getJSONObject(i);
                                String reminder_id          = jsonObject.getString(DefaultString.reminder_id);
                                String reminder_date        = jsonObject.getString(DefaultString.reminder_date);
                                String reminder_time        = jsonObject.getString(DefaultString.reminder_time);
                                String reminder_content     = jsonObject.getString(DefaultString.reminder_content);
                                String reminder_sound       = jsonObject.getString(DefaultString.reminder_sound);
                                String reminder_animation   = jsonObject.getString(DefaultString.reminder_animation);
                                String reminder_status      = jsonObject.getString(DefaultString.reminder_status);

                                alertArrayList.add(new Alert(reminder_id, reminder_date, reminder_time, reminder_content, reminder_sound, reminder_animation, reminder_status));
                                alertAdapter.notifyItemChanged(i);
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

    private void progressDialogError(String title, String message){
        progressDialog.setPositiveButton(getString(R.string.ok), v -> progressDialog.dismiss());
        progressDialog.setAnimation(R.drawable.icon_error);
        progressDialog.setDeterminate(false);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.show();
    }





    private void addAlert(){
        FABAddAlert.setOnClickListener(v -> {
            if(selectPosition == -1){
                showDialogAlert(selectPosition);
            }
            else{

            }
        });
    }

    public void showDialogAlert(int position){
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();

        TPAlertDialog           = dialog.findViewById(R.id.TPAlertDialog);
        IBCalenderAlertDialog   = dialog.findViewById(R.id.IBCalenderAlertDialog);
        IVCloseAlertDialog      = dialog.findViewById(R.id.IVCloseAlertDialog);
        IVSaveAlertDialog       = dialog.findViewById(R.id.IVSaveAlertDialog);
        txtTitleAlertDialog     = dialog.findViewById(R.id.txtTitleAlertDialog);
        txtDateAlertDialog      = dialog.findViewById(R.id.txtDateAlertDialog);
        txtLabelAlertDialog     = dialog.findViewById(R.id.txtLabelAlertDialog);
        txtSoundAlertDialog     = dialog.findViewById(R.id.txtSoundAlertDialog);
        txtAnimationAlertDialog = dialog.findViewById(R.id.txtAnimationAlertDialog);
        CBMondayAlertDialog     = dialog.findViewById(R.id.CBMondayAlertDialog);
        CBTuesdayAlertDialog    = dialog.findViewById(R.id.CBTuesdayAlertDialog);
        CBWednesdayAlertDialog  = dialog.findViewById(R.id.CBWednesdayAlertDialog);
        CBThursdayAlertDialog   = dialog.findViewById(R.id.CBThursdayAlertDialog);
        CBFridayAlertDialog     = dialog.findViewById(R.id.CBFridayAlertDialog);
        CBSaturdayAlertDialog   = dialog.findViewById(R.id.CBSaturdayAlertDialog);
        CBSundayAlertDialog     = dialog.findViewById(R.id.CBSundayAlertDialog);
        LLLabelAlertDialog      = dialog.findViewById(R.id.LLLabelAlertDialog);
        LLSoundAlertDialog      = dialog.findViewById(R.id.LLSoundAlertDialog);
        LLAnimationAlertDialog  = dialog.findViewById(R.id.LLAnimationAlertDialog);

        mediaArrayList                  = new ArrayList<>();
        ArrayList<String> weekArrayList = new ArrayList<>();
        Calendar calendar               = Calendar.getInstance();
        CheckBox[] CBWeekAlertDialog    = {CBMondayAlertDialog, CBTuesdayAlertDialog, CBWednesdayAlertDialog, CBThursdayAlertDialog, CBFridayAlertDialog, CBSaturdayAlertDialog, CBSundayAlertDialog};

        TPAlertDialog.setIs24HourView(true);
        getWeek(CBWeekAlertDialog, weekArrayList, calendar);

        for(CheckBox checkBox : CBWeekAlertDialog){
            checkBox.setChecked(false);
        }

        if(position == -1){
            txtTitleAlertDialog.setText(getString(R.string.tambah_alert));
            txtAnimationAlertDialog.setText(getString(R.string.animasi));
            txtLabelAlertDialog.setText(getString(R.string.alert));
            txtSoundAlertDialog.setText(getString(R.string.diam));

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            TPAlertDialog.setHour(Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date().getTime())));
            TPAlertDialog.setMinute(Integer.parseInt(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date().getTime())));
            txtDateAlertDialog.setText(new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault()).format(new Date().getTime()));
        }
        else{
            txtTitleAlertDialog.setText(getString(R.string.edit_alert));
            txtSoundAlertDialog.setText(alertArrayList.get(position).getSound());
            txtAnimationAlertDialog.setText(alertArrayList.get(position).getAnimation());
            txtLabelAlertDialog.setText(Html.fromHtml(alertArrayList.get(position).getContent(), Html.FROM_HTML_MODE_LEGACY));

            TPAlertDialog.setHour(Integer.parseInt(alertArrayList.get(position).getTime().substring(0, 2)));
            TPAlertDialog.setMinute(Integer.parseInt(alertArrayList.get(position).getTime().substring(3)));

            if(alertArrayList.get(position).getDate().equals("Every Day")){
                for(CheckBox checkBox : CBWeekAlertDialog){
                    checkBox.setChecked(true);
                }
            }
            else if(alertArrayList.get(position).getDate().contains(",")){
                for(int i = 0; i < alertArrayList.get(position).getDate().split(",").length; i++){
                    CBWeekAlertDialog[Integer.parseInt(alertArrayList.get(position).getDate().split(",")[i]) - 1].setChecked(true);
                }
            }
            else{
                calendar.set(Calendar.YEAR, Integer.parseInt(alertArrayList.get(position).getDate().substring(6)));
                calendar.set(Calendar.MONTH, Integer.parseInt(alertArrayList.get(position).getDate().substring(3, 5)));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(alertArrayList.get(position).getDate().substring(0, 2)));

                txtDateAlertDialog.setText(new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault()).format(calendar.getTime()));
            }
        }

        IVCloseAlertDialog.setOnClickListener(v -> dialog.cancel());
        IBCalenderAlertDialog.setOnClickListener(v -> showDatePickerDialog(calendar, CBWeekAlertDialog));
        LLLabelAlertDialog.setOnClickListener(v -> showEditor.launch(new Intent(this, Editor.class).putExtra(Editor.Text, Html.fromHtml(txtLabelAlertDialog.getText().toString(), Html.FROM_HTML_MODE_LEGACY))));




        IVSaveAlertDialog.setOnClickListener(v -> {});
        LLSoundAlertDialog.setOnClickListener(v -> {});
        LLAnimationAlertDialog.setOnClickListener(v -> {});
    }

    private void showEditor(){
        showEditor = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                txtLabelAlertDialog.setText(Html.fromHtml(result.getData().getStringExtra(Editor.Text), Html.FROM_HTML_MODE_LEGACY));
            }
        });
    }






    private void getWeek(CheckBox[] CBWeekAlertDialog, ArrayList<String> weekArrayList, Calendar calendar){
        for(int i = 1; i < 8; i++){
            ArrayList<String> dayArrayList  = new ArrayList<>();
            String numberOfDay              = String.valueOf(i);

            CBWeekAlertDialog[i - 1].setText(alertAdapter.reformatDateTime("u", "EEE", String.valueOf(i)));
            CBWeekAlertDialog[i - 1].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    weekArrayList.add(numberOfDay);
                }
                else{
                    weekArrayList.remove(numberOfDay);
                }

                Collections.sort(weekArrayList);
                dayArrayList.clear();

                if(weekArrayList.size() == 7){
                    txtDateAlertDialog.setText(getString(R.string.setiap_hari));
                }
                else if(weekArrayList.size() == 0){
                    txtDateAlertDialog.setText(new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault()).format(calendar.getTime()));
                }
                else{
                    for(int j = 0; j < weekArrayList.size(); j++){
                        dayArrayList.add(alertAdapter.reformatDateTime("u", "EEE", weekArrayList.get(j)));
                    }
                    txtDateAlertDialog.setText(dayArrayList.toString().replace("[", "").replace("]", ""));
                }
            });
        }
    }

    private void showDatePickerDialog(Calendar calendar, CheckBox[] CBWeekAlertDialog){
        DatePickerDialog DPD = new DatePickerDialog(this);

        DPD.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        DPD.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        DPD.setCanceledOnTouchOutside(false);
        DPD.setCancelable(true);
        DPD.show();

        DPD.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            txtDateAlertDialog.setText(new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault()).format(calendar.getTime()));

            for(CheckBox checkBox : CBWeekAlertDialog){
                checkBox.setChecked(false);
            }
        });
    }
}