package com.example.study.demo.touchListener;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityTouchListenerBinding;

public class TouchListenerActivity extends BaseActivity<ActivityTouchListenerBinding> {

    private int a = 0;
    private int b = 100;

    private int progress;

    @Override
    protected void initViews() {

        Log.d(TAG, "width:" + getResources().getDisplayMetrics().widthPixels);
        Log.d(TAG, "height:" + getResources().getDisplayMetrics().heightPixels);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        //点击文字抖动
        mBinding.shakeText.setOnClickListener(view -> {
            Animation ashe = AnimationUtils.loadAnimation(TouchListenerActivity.this, R.anim.shake);
            mBinding.shakeText.startAnimation(ashe);
        });

        mBinding.setProgresss.setOnClickListener(view -> {
            int a = Integer.parseInt(mBinding.shimmer.getText().toString().trim());
            if (a <= 1000) {
                setProgress(mBinding.progress, a);
            } else {
                Toast.makeText(TouchListenerActivity.this, "超出范围", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setProgress(View view, int progress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mBinding.progress, "progress", progress);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_touch_listener;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "TouchListenerActivity";
    }

}
