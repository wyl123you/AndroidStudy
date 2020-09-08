package com.example.study.demo.theme;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityThemeBinding;

public class ThemeActivity extends BaseActivity<ActivityThemeBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_theme;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "ThemeActivity";
    }

    @Override
    protected void initViews() {

        Window window = getWindow();
        View view = window.getDecorView();

        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//状态栏不会被隐藏但activity布局会扩展到状态栏所在位置
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//导航栏不会被隐藏但activity布局会扩展到导航栏所在位置| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                 );
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(Color.TRANSPARENT);



//        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.setNavigationBarColor(Color.RED);
    }
}
