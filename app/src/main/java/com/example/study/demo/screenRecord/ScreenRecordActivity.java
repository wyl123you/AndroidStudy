package com.example.study.demo.screenRecord;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.study.R;


public class ScreenRecordActivity extends AppCompatActivity {

    private final String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_record);
        ActivityCompat.requestPermissions(this, permissions, 100);
    }

    public void onRecordSave(View view) {
        Intent intent = new Intent();
        intent.setClass(this, RecordSaveActivity.class);
        startActivity(intent);
    }

    public void onRecordPush(View view) {
        Intent intent = new Intent();
        intent.setClass(this, RecordPushActivity.class);
        startActivity(intent);
    }
}