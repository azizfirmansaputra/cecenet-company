package com.cecenet.company.features.alert;

import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cecenet.company.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.MyView> {
    private final Context context;
    private final ArrayList<Alert> alertArrayList;
    private final AlertActivity alertActivity;

    public AlertAdapter(Context context, ArrayList<Alert> alertArrayList) {
        this.context        = context;
        this.alertArrayList = alertArrayList;
        this.alertActivity  = (AlertActivity)context;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        TextView txtTimeAlert, txtContentAlert, txtDateAlert;
        SwitchCompat SCStatusAlert;

        public MyView(@NonNull View itemView) {
            super(itemView);

            txtTimeAlert    = itemView.findViewById(R.id.txtTimeAlert);
            txtContentAlert = itemView.findViewById(R.id.txtContentAlert);
            txtDateAlert    = itemView.findViewById(R.id.txtDateAlert);
            SCStatusAlert   = itemView.findViewById(R.id.SCStatusAlert);
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_alert_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {



        holder.txtDateAlert.setText(dateAlert(position));
        holder.txtTimeAlert.setText(alertArrayList.get(position).getTime());
        holder.SCStatusAlert.setChecked(alertArrayList.get(position).getStatus().equals("Yes"));
        holder.txtContentAlert.setText(Html.fromHtml(alertArrayList.get(position).getContent(), Html.FROM_HTML_MODE_LEGACY));

        holder.txtDateAlert.setTextColor(context.getColor(R.color.textColorDarkBlue));
        holder.txtTimeAlert.setTextColor(context.getColor(R.color.textColorDarkBlue));
        holder.txtContentAlert.setTextColor(context.getColor(R.color.textColorDarkBlue));

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        holder.itemView.setBackgroundResource(typedValue.resourceId);

        holder.itemView.setOnLongClickListener(v -> {
            holder.txtDateAlert.setTextColor(context.getColor(R.color.textColorWhite));
            holder.txtTimeAlert.setTextColor(context.getColor(R.color.textColorWhite));
            holder.txtContentAlert.setTextColor(context.getColor(R.color.textColorWhite));
            holder.itemView.setBackgroundColor(context.getColor(R.color.bgColorLightBlue));

            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            alertActivity.showDialogAlert(position);
        });
    }

    public String dateAlert(int position){
        if(alertArrayList.get(position).getDate().equals("Every Day")){
            return context.getString(R.string.setiap_hari);
        }
        else if(alertArrayList.get(position).getDate().contains(",")){
            try{
                String dateAlert            = "";
                String[] dateAlertArray     = alertArrayList.get(position).getDate().split(",");
                SimpleDateFormat inputSDF   = new SimpleDateFormat("u", Locale.getDefault());
                SimpleDateFormat outputSDF  = new SimpleDateFormat("EEE", Locale.getDefault());

                for(int i = 0; i < dateAlertArray.length; i++){
                    Date dateAlertFormat    = inputSDF.parse(dateAlertArray[i]);
                    String separatorComa    = i != dateAlertArray.length - 1 ? ", " : "";
                    String outputDate       = outputSDF.format(Objects.requireNonNull(dateAlertFormat));
                    dateAlert               = String.format("%s%s%s", dateAlert, outputDate, separatorComa);
                }

                return dateAlert;
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        }
        else{
            try{
                SimpleDateFormat inputSDF   = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                SimpleDateFormat outputSDF  = new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault());
                Date dateAlertFormat        = inputSDF.parse(alertArrayList.get(position).getDate());

                return outputSDF.format(Objects.requireNonNull(dateAlertFormat));
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        }

        return "";
    }

    public String reformatDateTime(String inputFormat, String outputFormat, String date){
        try{
            SimpleDateFormat inputSDF   = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat outputSDF  = new SimpleDateFormat(outputFormat, Locale.getDefault());
            Date dateTimeFormat         = inputSDF.parse(date);

            return outputSDF.format(Objects.requireNonNull(dateTimeFormat));
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public int getItemCount() {
        return alertArrayList.size();
    }
}