package com.example.study.demo.shareAnimation;

import android.content.Intent;
import android.view.View;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity<ActivityDetailBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "DetailActivity";
    }

    @Override
    protected void initViews() {

        Intent intent = getIntent();
        //Glide.with(this).load(intent.getStringExtra("url")).into(mBinding.image);
        mBinding.image.setImageResource(R.drawable.faces);
        mBinding.name.setText(intent.getStringExtra("name"));
    }

    public void close(View view) {
        //finish();
        //如果直接finish回没有动画
        finishAfterTransition();
    }
}