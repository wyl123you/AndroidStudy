package com.example.study.demo.loadingview;

import android.view.View;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityLoadingViewBinding;

public class LoadingViewActivity extends BaseActivity<ActivityLoadingViewBinding> {

    @Override
    protected void initViews() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading_view;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "LoadingViewActivity";
    }


    public void onStart(View view) {
        mBinding.loadingView.startMoving();
    }

    public void onStop(View view) {
        mBinding.loadingView.stopMoving();
    }
}