package com.example.study.demo.screenRecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.study.R;
import com.example.study.demo.screenRecord.service.RecordSaveService;

public class RecordSaveActivity extends AppCompatActivity {

    public static final String TAG = "RecordSaveActivity";

    private TextView status;

    private boolean recording;

    private int screenWidth;
    private int screenHeight;
    private int dpi;

    private Intent recordSaveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_save);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, 10);

        status = findViewById(R.id.status);
        status.setText(R.string.start);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayMetrics dm = getResources().getDisplayMetrics();

        Log.d(TAG, "缩放因子density: " + dm.density);
        Log.d(TAG, "缩放因子scaledDensity: " + dm.scaledDensity);
        Log.d(TAG, "屏幕密度: " + (dpi = dm.densityDpi) + "(每寸像素：120/160/240/320）");

        Log.d(TAG, "x方向屏幕密度: " + dm.xdpi);
        Log.d(TAG, "y方向屏幕密度: " + dm.ydpi);

        float density = dm.density;
        screenWidth = (int) (dm.widthPixels / density + 0.5f); // 屏幕宽（px，如：480px）
        screenHeight = (int) (dm.heightPixels / density + 0.5f); // 屏幕高（px，如：800px）
        Log.d(TAG, "屏幕高:" + screenHeight + "dp");
        Log.d(TAG, "屏幕宽:" + screenWidth + "dp");

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.d(TAG, "屏幕高:" + screenWidth + "px");
        Log.d(TAG, "屏幕宽:" + screenHeight + "px");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                recordSaveService = new Intent();
                recordSaveService.setClass(this, RecordSaveService.class);
                recordSaveService.putExtra("resultCode", resultCode);
                recordSaveService.putExtra("data", data);
                recordSaveService.putExtra("path", getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/RecordSave.mp4");
                startService(recordSaveService);
                recording = true;
            }
        } else {
            Toast.makeText(this, "用户拒绝授权,屏幕录制失败", Toast.LENGTH_LONG).show();
        }
    }

    public void onSwitch(View view) {
        if (recording) {
            stopService(recordSaveService);
        } else {
            MediaProjectionManager mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, 1000);
        }
    }
}