package com.example.study.demo.systemUI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.study.R;
import com.umeng.commonsdk.debug.I;

import java.util.List;

public class SystemUIActivity extends AppCompatActivity {

    private String TAG = "SystemUIActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //刘海屏适配必须要在设置中  设置为自动
        Log.d(TAG, "onCreate: "+isCutOut(this));
        openFullScreenModel(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_system_uiactivity);

        Log.d(TAG, "onCreate: ");

        int nightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        if (nightMode ==Configuration.UI_MODE_NIGHT_NO){
            Log.d(TAG, "非夜间模式");
        }else if (nightMode == Configuration.UI_MODE_NIGHT_YES){
            Log.d(TAG, "夜间模式");
        }else if (nightMode == Configuration.UI_MODE_NIGHT_UNDEFINED){
            Log.d(TAG, "不明确");
        }


        // 这样就可以根据用户的喜好来设置是否启用夜间模式,注意点:
        // AppCompatDelegate类中还有一个方法：setLocalNightMode(int mode)，
        // 这个方法作用在当前组件，Activity中使用 getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)设置。
        // 比如在Application中设置为日间模式，
        // 而在当前Activity中调用getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES),
        // 该Activity会显示为夜间模式,覆盖原来的模式。

        // AppCompatDelegate.setDefaultNightMode(int mode);
        // 只作用于新生成的组件，对原先处于任务栈中的Activity不起作用。
        // 如果直接在Activity的onCreate()中调用这句代码，
        // 那当前的Activity中直接生效，不需要再调用recreate(),
        // 但我们通常是在监听器中调用这句代码，那就需要调用recreate()，
        // 否则对当前Activity无效(新生成的Activity还是生效的)。
        // 当然可以提前在onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)中
        // 保存好相关属性值,重建时使用
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }

    //@RequiresApi(api = Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();

        int a = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//布局放到导航栏,状态栏后面
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏导航栏
                | View.SYSTEM_UI_FLAG_FULLSCREEN//隐藏状态栏
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        Window window = getWindow();
        View view = window.getDecorView();
        window.setStatusBarColor(Color.RED);
        window.setNavigationBarColor(Color.YELLOW);
        view.setSystemUiVisibility(a);
//
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//
//        //下面图1
////        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
//        //下面图2
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        //下面图3
////        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
//        getWindow().setAttributes(lp);
    }

    public void openFullScreenModel(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = act.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            act.getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            aaa();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void aaa(){
        final View decorView = getWindow().getDecorView();

        decorView.postDelayed(() -> {
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            if (rootWindowInsets == null) {
                Log.e("TAG", "rootWindowInsets为空了");
                return;
            }
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
           if (displayCutout!=null){
               Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
               Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
               Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
               Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());

               List<Rect> rects = displayCutout.getBoundingRects();
               if (rects == null || rects.size() == 0) {
                   Log.e("TAG", "不是刘海屏");
               } else {
                   Log.e("TAG", "刘海屏数量:" + rects.size());
                   for (Rect rect : rects) {
                       Log.e("TAG", "刘海屏区域：" + rect);
                   }
               }
           }
        },1000);
    }

    public boolean isCutOut(Activity ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ctx.getWindow() == null || ctx.getWindow().getDecorView().getRootWindowInsets() == null) {
                return false;
            }
            DisplayCutout cutout = ctx.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            return cutout != null && cutout.getSafeInsetTop() != 0;
        } else {
            return false;
        }
    }
}