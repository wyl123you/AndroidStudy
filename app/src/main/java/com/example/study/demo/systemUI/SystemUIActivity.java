package com.example.study.demo.systemUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.study.R;
import com.umeng.commonsdk.debug.I;

public class SystemUIActivity extends AppCompatActivity {

    private String TAG = "SystemUIActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_uiactivity);

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int a = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//布局放到导航栏,状态栏后面
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏导航栏
//                |View.SYSTEM_UI_FLAG_FULLSCREEN//隐藏状态栏
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                ;

        Window window = getWindow();

        View view = window.getDecorView();


        view.setSystemUiVisibility(a);
    }
}