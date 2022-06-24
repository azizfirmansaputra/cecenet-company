package com.cecenet.company.tools;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cecenet.company.R;

import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

public class Editor extends AppCompatActivity {
    EditText ETInsertURLEditor, ETInsertWidthEditor, ETInsertHeightEditor;
    LinearLayout LLFontSizeEditor, LLColorEditor, LLInsertEditor;
    ImageButton IBInsertOKEditor;
    TextView txtFontSizeEditor;
    SeekBar SBFontSizeEditor;
    RichEditor richEditor;
    Button btnOKEditor;

    ActivityResultLauncher<String> selectMedia;
    ArrayList<String> mediaArrayList;
    String inputMedia;
    int fontSize;

    public static String Text   = "Text";
    public static String Media  = "Media";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ETInsertURLEditor       = findViewById(R.id.ETInsertURLEditor);
        ETInsertWidthEditor     = findViewById(R.id.ETInsertWidthEditor);
        ETInsertHeightEditor    = findViewById(R.id.ETInsertHeightEditor);
        LLFontSizeEditor        = findViewById(R.id.LLFontSizeEditor);
        LLColorEditor           = findViewById(R.id.LLColorEditor);
        LLInsertEditor          = findViewById(R.id.LLInsertEditor);
        IBInsertOKEditor        = findViewById(R.id.IBInsertOKEditor);
        txtFontSizeEditor       = findViewById(R.id.txtFontSizeEditor);
        SBFontSizeEditor        = findViewById(R.id.SBFontSizeEditor);
        richEditor              = findViewById(R.id.richEditor);
        btnOKEditor             = findViewById(R.id.btnOKEditor);

        mediaArrayList          = new ArrayList<>();
        inputMedia              = "";
        fontSize                = 4;

        richEditor.focusEditor();
        richEditor.setEditorFontSize(17);
        richEditor.getSettings().setAllowFileAccess(true);
        richEditor.setHtml(getIntent().getStringExtra(Text));
        richEditor.setEditorFontColor(R.color.textColorBlack);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setEditorBackgroundColor(getColor(R.color.bgColorGray));

        setOnDoneEditor();
        setFunctionsEditor();
        setSelectMediaEditor();
        setOnTextChangeEditor();
    }

    private void setOnDoneEditor(){
        btnOKEditor.setOnClickListener(v -> {
            if(!richEditor.getHtml().trim().isEmpty()){
                setResult(RESULT_OK, new Intent().putExtra(Text, richEditor.getHtml()).putExtra(Media, mediaArrayList));
                finishAfterTransition();
            }
        });
    }

    private void setFunctionsEditor(){
        findViewById(R.id.IBUndoEditor).setOnClickListener(v -> richEditor.undo());
        findViewById(R.id.IBRedoEditor).setOnClickListener(v -> richEditor.redo());

        findViewById(R.id.IBBoldEditor).setOnClickListener(v -> richEditor.setBold());
        findViewById(R.id.IBItalicEditor).setOnClickListener(v -> richEditor.setItalic());
        findViewById(R.id.IBUnderlineEditor).setOnClickListener(v -> richEditor.setUnderline());
        findViewById(R.id.IBStrikethroughEditor).setOnClickListener(v -> richEditor.setStrikeThrough());
        findViewById(R.id.IBSubscriptEditor).setOnClickListener(v -> richEditor.setSubscript());
        findViewById(R.id.IBSuperscriptEditor).setOnClickListener(v -> richEditor.setSuperscript());
        findViewById(R.id.IBFontSizeEditor).setOnClickListener(v -> setFontSizeEditor());
        findViewById(R.id.IBTextColorEditor).setOnClickListener(v -> setColorEditor(findViewById(R.id.IBTextColorEditor)));
        findViewById(R.id.IBBackgroundColorEditor).setOnClickListener(v -> setColorEditor(findViewById(R.id.IBBackgroundColorEditor)));

        findViewById(R.id.RBJustifyLeftEditor).setOnClickListener(v -> richEditor.setAlignLeft());
        findViewById(R.id.RBJustifyCenterEditor).setOnClickListener(v -> richEditor.setAlignCenter());
        findViewById(R.id.RBJustifyRightEditor).setOnClickListener(v -> richEditor.setAlignRight());
        findViewById(R.id.IBListBulletsEditor).setOnClickListener(v -> richEditor.setBullets());
        findViewById(R.id.IBListNumbersEditor).setOnClickListener(v -> richEditor.setNumbers());
        findViewById(R.id.IBIndentEditor).setOnClickListener(v -> richEditor.setIndent());
        findViewById(R.id.IBOutdentEditor).setOnClickListener(v -> richEditor.setOutdent());

        findViewById(R.id.IBHeading1Editor).setOnClickListener(v -> richEditor.setHeading(1));
        findViewById(R.id.IBHeading2Editor).setOnClickListener(v -> richEditor.setHeading(2));
        findViewById(R.id.IBHeading3Editor).setOnClickListener(v -> richEditor.setHeading(3));
        findViewById(R.id.IBHeading4Editor).setOnClickListener(v -> richEditor.setHeading(4));
        findViewById(R.id.IBHeading5Editor).setOnClickListener(v -> richEditor.setHeading(5));
        findViewById(R.id.IBHeading6Editor).setOnClickListener(v -> richEditor.setHeading(6));

        findViewById(R.id.IBInsertImageEditor).setOnClickListener(v -> checkPermission("image/*"));
        findViewById(R.id.IBInsertVideoEditor).setOnClickListener(v -> checkPermission("video/*"));
        findViewById(R.id.IBInsertAudioEditor).setOnClickListener(v -> checkPermission("audio/*"));
        findViewById(R.id.IBInsertYoutubeEditor).setOnClickListener(v -> setInsertEditor("youtube"));
        findViewById(R.id.IBInsertLinkEditor).setOnClickListener(v -> setInsertEditor("link"));

        findViewById(R.id.IBQuoteEditor).setOnClickListener(v -> richEditor.setBlockquote());
        findViewById(R.id.IBCheckBoxEditor).setOnClickListener(v -> richEditor.insertTodo());
    }

    private void setFontSizeEditor(){
        SBFontSizeEditor.setMax(7);
        SBFontSizeEditor.setProgress(fontSize);
        txtFontSizeEditor.setText(String.valueOf(SBFontSizeEditor.getProgress()));
        setVisibleFunctionEditor(LLFontSizeEditor, View.VISIBLE, View.GONE, View.GONE);

        SBFontSizeEditor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontSize = progress;
                richEditor.setFontSize(progress);
                txtFontSizeEditor.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setVisibleFunctionEditor(LinearLayout linearLayout, int fontSize, int color, int insert){
        if(linearLayout.getVisibility() == View.GONE){
            LLFontSizeEditor.setVisibility(fontSize);
            LLInsertEditor.setVisibility(insert);
            LLColorEditor.setVisibility(color);
        }
        else{
            LLFontSizeEditor.setVisibility(View.GONE);
            LLInsertEditor.setVisibility(View.GONE);
            LLColorEditor.setVisibility(View.GONE);
        }
    }

    private void setColorEditor(ImageButton IB){
        int[] colorPaletteArray         = {0xFFB21F35, 0xFFD82735, 0xFFFF7435, 0xFFFFA135, 0xFFFFCB35, 0xFFFFF735, 0xFF00753A, 0xFF009E47, 0xFF16DD36, 0xFF0052A5, 0xFF0079E7, 0xFF06A9FC, 0xFF681E7E, 0xFF7D3CB5, 0xFF000000};
        ImageButton[] ImageButtonColor  = new ImageButton[colorPaletteArray.length];
        LinearLayout.LayoutParams LLLP  = new LinearLayout.LayoutParams(50, 50);

        LLColorEditor.removeAllViews();
        LLColorEditor.setVisibility(View.GONE);
        LLLP.setMargins(5, 5, 5, 5);
        setVisibleFunctionEditor(LLColorEditor, View.GONE, View.VISIBLE, View.GONE);

        for(int i = 0; i < ImageButtonColor.length; i++){
            int colorPalette    = colorPaletteArray[i];
            ImageButtonColor[i] = new ImageButton(this);

            ImageButtonColor[i].setBackgroundColor(colorPalette);
            ImageButtonColor[i].setLayoutParams(LLLP);

            ImageButtonColor[i].setOnClickListener(v -> {
                if(IB.getId() == R.id.IBTextColorEditor){
                    richEditor.setTextColor(colorPalette);
                }
                else if(IB.getId() == R.id.IBBackgroundColorEditor){
                    richEditor.setTextBackgroundColor(colorPalette);
                }

                IB.setImageTintList(ColorStateList.valueOf(colorPalette));
                setVisibleFunctionEditor(LLColorEditor, View.GONE, View.GONE, View.GONE);
            });

            LLColorEditor.addView(ImageButtonColor[i]);
        }
    }

    private void checkPermission(String input){
        inputMedia = input;

        if(CheckPermission.READ_EXTERNAL_STORAGE(this)){
            selectMedia.launch(input);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CheckPermission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectMedia.launch(inputMedia);
        }
    }

    private void setSelectMediaEditor(){
        selectMedia = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(!inputMedia.isEmpty() && result != null){
                setInsertEditor(inputMedia);
                ETInsertURLEditor.setText(FileDetails.getFilePath(this, result));
            }
        });
    }

    private void setInsertEditor(String media){
        switch(media){
            case "image/*":
            case "video/*":
            case "youtube":
                ETInsertHeightEditor.setInputType(InputType.TYPE_CLASS_NUMBER);
                ETInsertHeightEditor.setHint(getString(R.string.masukkan_tinggi));
                ETInsertHeightEditor.getText().clear();

                ETInsertHeightEditor.setVisibility(View.VISIBLE);
                ETInsertWidthEditor.setVisibility(View.VISIBLE);
                break;

            case "audio/*":
                ETInsertWidthEditor.setVisibility(View.GONE);
                ETInsertHeightEditor.setVisibility(View.GONE);
                break;

            case "link":
                ETInsertHeightEditor.setInputType(InputType.TYPE_CLASS_TEXT);
                ETInsertHeightEditor.setHint(getString(R.string.masukkan_label_link));
                ETInsertHeightEditor.getText().clear();

                ETInsertHeightEditor.setVisibility(View.VISIBLE);
                ETInsertWidthEditor.setVisibility(View.GONE);
                break;
        }

        IBInsertOKEditor.setOnClickListener(v -> {
            String alt      = getString(R.string.gambar_error);
            String url      = ETInsertURLEditor.getText().toString();
            String width    = ETInsertWidthEditor.getText().toString().isEmpty() ? "150" : ETInsertWidthEditor.getText().toString();
            String height   = ETInsertHeightEditor.getText().toString().isEmpty() ? "200" : ETInsertHeightEditor.getText().toString();
            String title    = ETInsertHeightEditor.getText().toString().isEmpty() ? ETInsertURLEditor.getText().toString() : ETInsertHeightEditor.getText().toString();

            switch(media){
                case "image/*":
                    richEditor.insertImage(url, alt, Integer.parseInt(width), Integer.parseInt(height));
                    break;

                case "video/*":
                    richEditor.insertVideo(url, Integer.parseInt(width), Integer.parseInt(height));
                    break;

                case "audio/*":
                    richEditor.insertAudio(url);
                    break;

                case "youtube":
                    richEditor.insertYoutubeVideo(url, Integer.parseInt(width), Integer.parseInt(height));
                    break;

                case "link":
                    richEditor.insertLink(url, title);
                    break;
            }

            mediaArrayList.add(url);
            ETInsertURLEditor.getText().clear();
            setVisibleFunctionEditor(LLInsertEditor, View.GONE, View.GONE, View.GONE);
        });

        ETInsertURLEditor.getText().clear();
        LLInsertEditor.setVisibility(View.GONE);
        setVisibleFunctionEditor(LLInsertEditor, View.GONE, View.GONE, View.VISIBLE);
    }

    private void setOnTextChangeEditor(){
        richEditor.setOnTextChangeListener(text -> {
            if(text.isEmpty()){
                fontSize = 4;
                mediaArrayList.clear();
                richEditor.setFontSize(fontSize);
                SBFontSizeEditor.setProgress(fontSize);

                ((RadioButton)findViewById(R.id.RBJustifyLeftEditor)).setChecked(true);
                ((ImageButton)findViewById(R.id.IBTextColorEditor)).setImageTintList(ColorStateList.valueOf(getColor(R.color.textColorWhite)));
                ((ImageButton)findViewById(R.id.IBBackgroundColorEditor)).setImageTintList(ColorStateList.valueOf(getColor(R.color.textColorWhite)));
            }

            setVisibleFunctionEditor(LLFontSizeEditor, View.GONE, View.GONE, View.GONE);
        });
    }
}