package com.example.study.viewPager3D;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.Activity3dViewpagerBinding;

public class ViewPager3DActivity extends BaseActivity<Activity3dViewpagerBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_3d_viewpager;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "ViewPager3DActivity";
    }

    @Override
    protected void initViews() {

    }
}