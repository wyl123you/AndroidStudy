package com.example.study.demo.language;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityLanguageBinding;
import com.example.study.manager.LanguageUtil;
import com.example.study.manager.MMKVUtil;

import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    private static final String TAG = "LanguageActivity";

    @Override
    protected void initViews() {
        //获取系统当前使用的语言
        String lan = Locale.getDefault().getLanguage();
        //获取区域
        String country = Locale.getDefault().getCountry();
        //设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn.
        Log.d(TAG, "当前系统语言环境");
        Log.d(TAG, "Language: " + lan);
        Log.d(TAG, "Country: " + country);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "LanguageActivity";
    }

    public void chinese(View view) {
        MMKVUtil.setLanguage("zh_CN");
        switchLanguage(MMKVUtil.getLanguage());
    }

    public void english(View view) {
        MMKVUtil.setLanguage("en");
        switchLanguage(MMKVUtil.getLanguage());
    }

    private void switchLanguage(String language) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LanguageUtil.switchLanguage(this, language);
        }
        Intent intent = new Intent(this, this.getClass());
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}