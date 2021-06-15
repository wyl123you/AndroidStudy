package com.example.study.demo.screenRecord.service;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.util.Log;

public class RecordSaveService extends Thread {

    private static final String TAG = "MediaRecordService";

    private final int mWidth;
    private final int mHeight;
    private final int mBitRate;
    private final int mDpi;
    private final String mDstPath;
    private final int FRAME_RATE = 60;
    private MediaRecorder mMediaRecorder;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    public RecordSaveService(int width, int height, int bitrate, int dpi, MediaProjection mp, String dstPath) {
        this.mWidth = width;
        this.mHeight = height;
        this.mBitRate = bitrate;
        this.mDpi = dpi;
        this.mMediaProjection = mp;
        this.mDstPath = dstPath;
    }

    @Override
    public void run() {
        try {
            initMediaRecorder();
            //在mediaRecorder.prepare()方法后调用
            mVirtualDisplay = mMediaProjection.createVirtualDisplay(TAG + "-display", mWidth, mHeight, mDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, mMediaRecorder.getSurface(), null, null);
            Log.d(TAG, "created virtual display: " + mVirtualDisplay);
            mMediaRecorder.start();
            Log.d(TAG, "mediaRecorder start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化MediaRecorder
     */
    public void initMediaRecorder() {
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
        //把setVideoSize和setVideoFrameRate放到设置编码和格式的代码后面（亲测可行）
        mMediaRecorder.setVideoSize(720, 1280);
        mMediaRecorder.setVideoFrameRate(FRAME_RATE);
        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);

        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "media recorder" + mBitRate + "kps");
    }

    public void release() {
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
        Log.d(TAG, "release");
    }
}