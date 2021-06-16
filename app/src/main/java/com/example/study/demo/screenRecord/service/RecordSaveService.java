package com.example.study.demo.screenRecord.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.study.MainActivity;
import com.example.study.R;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_CANCELED;

public class RecordSaveService extends Service {

    private static final String TAG = "RecordSaveService";

    private int mWidth;
    private int mHeight;
    private int mBitRate;
    private int mDpi;
    private String mDstPath;
    private MediaRecorder mMediaRecorder;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager manager;
    private VirtualDisplay mVirtualDisplay;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mDpi = dm.densityDpi;
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(@NotNull Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        createNotificationChannel();

        int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
        Log.d(TAG, "resultCode: " + resultCode);

        Intent data = intent.getParcelableExtra("data");
        Log.d(TAG, "data: " + data);

        mDstPath = intent.getStringExtra("path");
        Log.d(TAG, "path: " + mDstPath);

        new Thread(() -> {
            prepareMediaRecorder();
            manager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mMediaProjection = manager.getMediaProjection(resultCode, data);
            mVirtualDisplay = mMediaProjection.createVirtualDisplay(TAG + "-display", mWidth, mHeight, mDpi,
                    0, mMediaRecorder.getSurface(), null, null);
            Log.d(TAG, "created virtual display: " + mVirtualDisplay);
            mMediaRecorder.start();
            Log.d(TAG, "mediaRecorder start");
        }).start();
        //super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    public void prepareMediaRecorder() {
        //MediaRecorder官方文档导读
        //https://blog.csdn.net/qq_32175491/article/details/78664821
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setOutputFile(mDstPath);
        //setVideoSize是设置视频分辨率，跟设备硬件有关，若手机不支持则会报该错误
        mMediaRecorder.setVideoSize(720, 1280);
        mMediaRecorder.setVideoFrameRate(60);
        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);

        mMediaRecorder.setOnErrorListener((mr, what, extra) -> {
            Log.d(TAG, "what: " + what);
            Log.d(TAG, "extra: " + extra);
        });

        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaProjection.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        if (manager != null) {
            manager = null;
        }
        Log.d(TAG, "onDestroy");
        stopForeground(true);
    }

    private void createNotificationChannel() {
        Intent intent = new Intent(); //点击后跳转的界面，可以设置跳转数据
        intent.setClass(this, MainActivity.class);

        //获取一个Notification构造器
        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.face)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("SMI InstantView") //设置下拉列表里的标题
                .setSmallIcon(R.drawable.face) // 设置状态栏内的小图标
                .setContentText("is running......") //设置上下文内容
                .setWhen(System.currentTimeMillis() + 2000); //设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);
    }
}