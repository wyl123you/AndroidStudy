package com.example.study.demo.baidumap;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityBaiduMapBinding;

public class BaiduMapActivity extends BaseActivity<ActivityBaiduMapBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_baidu_map;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "BaiduMapActivity";
    }

    @Override
    protected void initViews() {

    }
}
