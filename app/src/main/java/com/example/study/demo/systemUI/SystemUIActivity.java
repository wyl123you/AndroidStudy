package com.example.study.demo.systemUI;

import android.app.Activity;
import android.content.Context;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;

import java.lang.reflect.Method;
import java.util.List;

public class SystemUIActivity extends AppCompatActivity {

    private String TAG = "SystemUIActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_uiactivity);
        int nightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        if (nightMode == Configuration.UI_MODE_NIGHT_NO) {
            Log.d(TAG, "非夜间模式");
        } else if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            Log.d(TAG, "夜间模式");
        } else if (nightMode == Configuration.UI_MODE_NIGHT_UNDEFINED) {
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

        //刘海屏适配必须要在设置中  设置为自动
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        } else {
            if (Build.BRAND.toLowerCase().contains("vivo")) {
                Log.d(TAG, "您的手机是vivo");
                Log.d(TAG, "您的手机是刘海屏" + hasNotchAtVivo(this));
                //vivo在设置–显示与亮度–第三方应用显示比例中可以切换是否全屏显示还是安全区域显示。
                //vivo不提供接口获取刘海尺寸，目前vivo的刘海宽为100dp,高为27dp。
            }
            //https://blog.csdn.net/u011810352/article/details/80587531?utm_term=android区域控制屏幕显示&utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~sobaiduweb~default-5-80587531&spm=3001.4430
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();

        int a = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//布局放到导航栏,状态栏后面
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏导航栏
                | View.SYSTEM_UI_FLAG_FULLSCREEN//隐藏状态栏
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        Window window = getWindow();
        View view = window.getDecorView();
        window.setStatusBarColor(Color.RED);
        window.setNavigationBarColor(Color.YELLOW);
        view.setSystemUiVisibility(a);

//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            isCutOut();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void isCutOut() {
        final View decorView = getWindow().getDecorView();

        decorView.postDelayed(() -> {
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            if (rootWindowInsets == null) {
                Log.e("TAG", "rootWindowInsets为空了");
                return;
            }
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
            if (displayCutout != null) {
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
        }, 1000);
    }

//    public boolean isCutOut(Activity ctx) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            if (ctx.getWindow() == null || ctx.getWindow().getDecorView().getRootWindowInsets() == null) {
//                return false;
//            }
//            DisplayCutout cutout = ctx.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
//            return cutout != null && cutout.getSafeInsetTop() != 0;
//        } else {
//            return false;
//        }
//    }

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }


}