package com.example.study.demo.touchListener;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.CheckBox;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityTouchListenerBinding;
import com.example.study.manager.FastBlurUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

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

        CheckBox checkBox;

//        ThreadPool1.SimpleTask<Object> task = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "1");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground: 1  " + (a++));
//                }
//                return null;
//            }
//        };
//
//        ThreadPool1.SimpleTask<?> task1 = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "2");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground: 2  " + (b--));
//                }
//                return null;
//            }
//        };
//
//        ThreadPool1.SimpleTask<?> task3 = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "3");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground:3  " + (b--));
//                }
//                return null;
//            }
//        };
//
//        ThreadPool1.SimpleTask<?> task4 = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "4");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground:43  " + (b--));
//                }
//                return null;
//            }
//        };
//
////        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ThreadPool1.executeBySingle(task);
//                ThreadPool1.executeBySingle(task1);
//                ThreadPool1.executeBySingle(task3);
//                ThreadPool1.executeBySingle(task4);
//            }
//        });

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
            }
        };


        //点击文字抖动
        mBinding.shakeText.setOnClickListener(view -> {
            Animation ashe = AnimationUtils.loadAnimation(TouchListenerActivity.this, R.anim.shake);
            mBinding.shakeText.startAnimation(ashe);

            new Thread(() -> {
                Bitmap bitmap = FastBlurUtil.getBlurBackgroundDrawer(TouchListenerActivity.this);


                File ddd = new File(Environment.getExternalStorageDirectory() + "/a/bitmap.jpg");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(ddd);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();


            //下载apk
            new Thread(() -> {
                try {
                    URL apk = new URL("https://res.zhuagewawa.com/cloudy/cover_hw_01.jpg");
                    HttpsURLConnection coon = (HttpsURLConnection) apk.openConnection();
                    coon.setDoInput(true);
                    InputStream is = coon.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory() + "/apk.jpg");

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] bytes = new byte[1024];
                    int len;

                    while ((len = is.read(bytes)) != -1) {
                        Log.d(TAG, "onClick: " + len);
                        fileOutputStream.write(bytes, 0, len);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();


            progress = new Random().nextInt(100);

            setProgress(mBinding.progress, progress);


        });

        ArrayList<View> lineViews = new ArrayList<>();//保存一行中的所有的View;

        //文字动态闪光
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
