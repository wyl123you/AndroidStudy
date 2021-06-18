package com.example.study.demo.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityCamera2Binding;

import java.io.IOException;

public class Camera2Activity extends BaseActivity<ActivityCamera2Binding> implements TextureView.SurfaceTextureListener {

    private static final String TAG = "Camera2Activity";

    private TextureView textureView;


    @Override
    protected void initViews() {
        textureView = findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(this);

        int flag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera2;
    }

    @Override
    protected boolean useDataBinding() {
        return false;
    }

    @Override
    protected String getTag() {
        return null;
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable");

        Camera camera = Camera.open(0);
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        textureView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                previewSize.width, previewSize.height));
        Log.d(TAG, "width: " + previewSize.width);
        Log.d(TAG, "height: " + previewSize.height);
        try {
            camera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
//        textureView.setAlpha(1.0f);
        textureView.setRotation(90.0f);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }
}