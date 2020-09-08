package com.example.study.demo.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.study.MainActivity;
import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityWelcomeBinding;


public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                mBinding.text.setText(l + "");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }.start();


    }

    @Override
    protected void initViews() {

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
}