package com.example.study.demo.screenRecord;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.demo.screenRecord.service.RecordPushService;

import java.io.File;

public class RecordPushActivity extends AppCompatActivity {

    public static final String TAG = "RecordPushActivity";

    private MediaProjectionManager mProjectionManager;

    private Button status;

    private boolean recording;

    private RecordPushService recordPush;

    private int screenWidth;
    private int screenHeight;
    private int dpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_push);

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
            MediaProjection mediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                Log.d(TAG, "media projection is null");
                return;
            }
            File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS);
            } else {
                file = Environment.getExternalStorageDirectory();
            }
            file = new File(file, "11111.mp4");

            if (requestCode == 1000) {
                recordPush = new RecordPushService(screenWidth, screenHeight, 6000000, dpi, mediaProjection, file.getAbsolutePath());
                Log.d(TAG, "Path: " + file.getAbsolutePath());
                recordPush.start();
                status.setText(R.string.stop);
                recording = true;
            }
        } else {
            Toast.makeText(this, "用户拒绝授权,屏幕录制失败", Toast.LENGTH_LONG).show();
        }
    }

    public void onSwitch(View view) {
        if (recording) {
            recordPush.quit();
            status.setText(R.string.start);
            recording = false;
        } else {
            mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, 1000);
        }
    }
}