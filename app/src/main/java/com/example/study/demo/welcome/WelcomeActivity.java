package com.example.study.demo.welcome;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;

import com.example.study.MainActivity;
import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityWelcomeBinding;

import java.text.MessageFormat;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {

    private PackageManager pm;

    private ComponentName default_name;
    private ComponentName test1;
    private ComponentName test2;

    @Override
    protected void initViews() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                mBinding.text.setText(MessageFormat.format("{0}", l));
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }.start();

        pm = getPackageManager();

        default_name = getComponentName();

        test1 = new ComponentName(getBaseContext(), "com.example.study.TestIcon1");
        test2 = new ComponentName(getBaseContext(), "com.example.study.TestIcon2");

//        disableComponent(default_name);
//        disableComponent(test2);
//        enableComponent(test1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "WelcomeActivity";
    }

    private void enableComponent(ComponentName componentName) {
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName) {
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}