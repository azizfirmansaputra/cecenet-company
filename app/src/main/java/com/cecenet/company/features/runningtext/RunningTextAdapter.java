package com.cecenet.company.features.runningtext;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cecenet.company.R;

import java.util.ArrayList;

public class RunningTextAdapter extends RecyclerView.Adapter<RunningTextAdapter.MyView> {
    private final Context context;
    private final ArrayList<RunningText> runningTextArrayList;
    private final RunningTextActivity runningTextActivity;
    private boolean doubleClickRunningText;

    public RunningTextAdapter(Context context, ArrayList<RunningText> runningTextArrayList) {
        this.context                = context;
        this.runningTextArrayList   = runningTextArrayList;
        this.runningTextActivity    = (RunningTextActivity)context;
        this.doubleClickRunningText = false;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        HorizontalScrollView HSVRunningText;
        TextView txtRunningText;

        public MyView(@NonNull View itemView) {
            super(itemView);

            HSVRunningText  = itemView.findViewById(R.id.HSVRunningText);
            txtRunningText  = itemView.findViewById(R.id.txtRunningText);
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_running_text_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        holder.txtRunningText.setTextColor(context.getColor(R.color.textColorBlack));
        holder.txtRunningText.setText(runningTextArrayList.get(position).getContent());
        holder.HSVRunningText.setBackgroundColor(context.getColor(android.R.color.transparent));

        holder.txtRunningText.setOnLongClickListener(v -> {
            holder.txtRunningText.setTextColor(context.getColor(R.color.textColorWhite));
            holder.HSVRunningText.setBackgroundColor(context.getColor(R.color.bgColorDarkBlue));
            selection(holder, runningTextArrayList.get(position).getVisible().equals("Yes"));

            return true;
        });

        holder.txtRunningText.setOnClickListener(v -> {
            if(runningTextActivity.selectPosition != -1){
                if(holder.txtRunningText.getCurrentTextColor() == context.getColor(R.color.textColorWhite)){
                    holder.txtRunningText.setTextColor(context.getColor(R.color.textColorBlack));
                    holder.HSVRunningText.setBackgroundColor(context.getColor(android.R.color.transparent));

                    runningTextActivity.selectPosition = -1;
                    runningTextActivity.selectionRunningText(runningTextArrayList.get(position).getVisible().equals("Yes"), runningTextActivity.selectPosition);
                }
                else{
                    holder.txtRunningText.setTextColor(context.getColor(R.color.textColorWhite));
                    holder.HSVRunningText.setBackgroundColor(context.getColor(R.color.bgColorDarkBlue));
                    selection(holder, runningTextArrayList.get(position).getVisible().equals("Yes"));
                }
            }
            else{
                if(doubleClickRunningText){
                    runningTextActivity.doubleClickRunningText(runningTextArrayList.get(position).getVisible().equals("Yes"), position);
                }

                doubleClickRunningText = true;
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleClickRunningText = false, 500);
            }
        });
    }

    private void selection(RunningTextAdapter.MyView holder, boolean visible){
        if(runningTextActivity.selectPosition != holder.getAdapterPosition()){
            notifyItemChanged(runningTextActivity.selectPosition);
            runningTextActivity.selectPosition = holder.getAdapterPosition();
        }

        runningTextActivity.selectionRunningText(visible, runningTextActivity.selectPosition);
    }

    @Override
    public int getItemCount() {
        return runningTextArrayList.size();
    }
}