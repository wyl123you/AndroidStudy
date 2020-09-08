package com.example.study.demo.qrcode;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityCaptureDataBinding;

public class CaptureActivity extends BaseActivity<ActivityCaptureDataBinding> {
    @Override
    protected void initViews() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "CaptureActivity";
    }
}
