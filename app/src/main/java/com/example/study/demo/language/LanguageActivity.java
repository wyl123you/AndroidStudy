package com.example.study.demo.language;

import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityLanguageBinding;
import com.example.study.manager.LanguageUtil;
import com.example.study.manager.MMKVUtil;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    private static final String TAG = "LanguageActivity";

    @Override
    protected void initViews() {

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