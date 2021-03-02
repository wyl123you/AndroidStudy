package com.example.study.demo.view_diy;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityDiyViewMoveBinding;

public class DivViewMoveActivity extends BaseActivity<ActivityDiyViewMoveBinding> {
    @Override
    protected void initViews() {
        mBinding.movaableView.smoothScrollTo(-400,0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_diy_view_move;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "DivViewMoveActivity";
    }
}
