package com.cecenet.company.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.cecenet.company.R;

import java.util.Locale;

public class ProgressDialog extends Dialog {
    LinearLayout LLHeaderDialog, LLDeterminateDialog, LLActionDialog;
    TextView txtTitleDialog, txtMessageDialog, txtProgressDialog;
    ImageView IVIconDialog, IVLoadingDialog;
    ProgressBar PBLoadingDialog;
    Button btnCancelDialog, btnNegativeDialog, btnPositiveDialog;

    boolean determinate;
    int icon, progress, animation, animationTint;
    String title, message, txtCancel, txtNegative, txtPositive;
    View.OnClickListener clickListenerCancel, clickListenerNegative, clickListenerPositive;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_progress_dialog);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LLHeaderDialog      = findViewById(R.id.LLHeaderDialog);
        LLDeterminateDialog = findViewById(R.id.LLDeterminateDialog);
        LLActionDialog      = findViewById(R.id.LLActionDialog);
        txtTitleDialog      = findViewById(R.id.txtTitleDialog);
        txtMessageDialog    = findViewById(R.id.txtMessageDialog);
        txtProgressDialog   = findViewById(R.id.txtProgressDialog);
        IVIconDialog        = findViewById(R.id.IVIconDialog);
        IVLoadingDialog     = findViewById(R.id.IVLoadingDialog);
        PBLoadingDialog     = findViewById(R.id.PBLoadingDialog);
        btnCancelDialog     = findViewById(R.id.btnCancelDialog);
        btnNegativeDialog   = findViewById(R.id.btnNegativeDialog);
        btnPositiveDialog   = findViewById(R.id.btnPositiveDialog);

        txtTitleDialog.setText(title);
        txtMessageDialog.setText(message);
        IVIconDialog.setImageResource(icon);
        PBLoadingDialog.setProgress(progress);
        txtProgressDialog.setText(String.format(Locale.getDefault(), "%d%%", progress));

        btnCancelDialog.setText(txtCancel);
        btnNegativeDialog.setText(txtNegative);
        btnPositiveDialog.setText(txtPositive);
        btnCancelDialog.setOnClickListener(clickListenerCancel);
        btnNegativeDialog.setOnClickListener(clickListenerNegative);
        btnPositiveDialog.setOnClickListener(clickListenerPositive);

        if(getTitle().isEmpty() && getIcon() == 0){
            LLHeaderDialog.setVisibility(View.GONE);
        }

        if(getAnimation() != 0){
            Glide.with(getContext()).asGif().load(animation).error(animation).into(IVLoadingDialog);
        }
        else{
            Glide.with(getContext()).asGif().load(R.drawable.img_loading).into(IVLoadingDialog);
        }

        if(getAnimationTint() != 0){
            IVLoadingDialog.setColorFilter(animationTint);
        }

        if(isDeterminate()){
            IVLoadingDialog.setVisibility(View.GONE);
        }
        else{
            LLDeterminateDialog.setVisibility(View.GONE);
        }

        checkActionDialogVisibility();
    }

    public void setTitle(String title) {
        this.title = title;

        if(txtTitleDialog != null){
            if(!title.isEmpty()){
                txtTitleDialog.setText(title);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(int icon) {
        this.icon = icon;

        if(IVIconDialog != null){
            if(icon != 0){
                IVIconDialog.setImageResource(icon);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setDeterminate(boolean determinate) {
        this.determinate = determinate;

        if(IVLoadingDialog != null && LLDeterminateDialog != null){
            if(determinate){
                IVLoadingDialog.setVisibility(View.GONE);
                LLDeterminateDialog.setVisibility(View.VISIBLE);
            }
            else{
                IVLoadingDialog.setVisibility(View.VISIBLE);
                LLDeterminateDialog.setVisibility(View.GONE);
            }
        }
    }

    public boolean isDeterminate() {
        return determinate;
    }

    public void setAnimation(int animation) {
        this.animation = animation;

        if(IVLoadingDialog != null){
            if(animation != 0){
                Glide.with(getContext()).asGif().load(animation).error(animation).into(IVLoadingDialog);
            }
            else{
                Glide.with(getContext()).asGif().load(R.drawable.img_loading).into(IVLoadingDialog);
            }
        }
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimationTint(int animationTint) {
        this.animationTint = animationTint;

        if(IVLoadingDialog != null){
            if(animationTint != 0){
                IVLoadingDialog.setColorFilter(animationTint);
            }
            else{
                IVLoadingDialog.clearColorFilter();
            }
        }
    }

    public int getAnimationTint() {
        return animationTint;
    }

    public void setMessage(String message) {
        this.message = message;

        if(txtMessageDialog != null){
            if(!message.isEmpty()){
                txtMessageDialog.setText(message);
            }
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;

        if(PBLoadingDialog != null && txtProgressDialog != null){
            PBLoadingDialog.setProgress(progress);
            txtProgressDialog.setText(String.format(Locale.getDefault(), "%d%%", progress));
        }
    }

    public void setCancelButton(String txtCancel, View.OnClickListener clickListenerCancel) {
        this.txtCancel              = txtCancel;
        this.clickListenerCancel    = clickListenerCancel;

        if(LLActionDialog != null && btnCancelDialog != null){
            if(clickListenerCancel != null){
                btnCancelDialog.setText(txtCancel);
                LLActionDialog.setVisibility(View.VISIBLE);
                btnCancelDialog.setVisibility(View.VISIBLE);
                btnCancelDialog.setOnClickListener(clickListenerCancel);
            }

            checkActionDialogVisibility();
        }
    }

    public void setNegativeButton(String txtNegative, View.OnClickListener clickListenerNegative) {
        this.txtNegative            = txtNegative;
        this.clickListenerNegative  = clickListenerNegative;

        if(LLActionDialog != null && btnNegativeDialog != null){
            if(clickListenerNegative != null){
                btnNegativeDialog.setText(txtNegative);
                LLActionDialog.setVisibility(View.VISIBLE);
                btnNegativeDialog.setVisibility(View.VISIBLE);
                btnNegativeDialog.setOnClickListener(clickListenerNegative);
            }

            checkActionDialogVisibility();
        }
    }

    public void setPositiveButton(String txtPositive, View.OnClickListener clickListenerPositive) {
        this.txtPositive            = txtPositive;
        this.clickListenerPositive  = clickListenerPositive;

        if(LLActionDialog != null && btnPositiveDialog != null){
            if(clickListenerPositive != null){
                btnPositiveDialog.setText(txtPositive);
                LLActionDialog.setVisibility(View.VISIBLE);
                btnPositiveDialog.setVisibility(View.VISIBLE);
                btnPositiveDialog.setOnClickListener(clickListenerPositive);
            }

            checkActionDialogVisibility();
        }
    }

    private void checkActionDialogVisibility(){
        if(clickListenerCancel == null){
            btnCancelDialog.setVisibility(View.GONE);
        }

        if(clickListenerNegative == null){
            btnNegativeDialog.setVisibility(View.GONE);
        }

        if(clickListenerPositive == null){
            btnPositiveDialog.setVisibility(View.GONE);
        }

        if(clickListenerCancel == null && clickListenerNegative == null && clickListenerPositive == null){
            LLActionDialog.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        setIcon(0);
        setTitle("");
        setMessage("");
        setProgress(0);
        setAnimation(0);
        setAnimationTint(0);
        setDeterminate(false);

        setCancelButton("", null);
        setPositiveButton("", null);
        setNegativeButton("", null);
    }
}