package com.example.study.demo.shareAnimation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityShareAnimationBinding;

@SuppressWarnings("unchecked")
public class ShareAnimationActivity extends BaseActivity<ActivityShareAnimationBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_animation;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "ShareAnimationActivity";
    }

    @Override
    protected void initViews() {

        mBinding.card.setOnClickListener(view -> {

            String url = "http://img.wmtp.net/wp-content/uploads/2015/04/9860c41b0ef41bd59c5f7b7055da81cb38db3da0.png";
            String name = "标题";

            View names = mBinding.name;
            View image = mBinding.image;

            Pair<View, String> pair1 = Pair.create(names, "name");
            Pair<View, String> pair2 = Pair.create(image, "image");

            Intent intent = new Intent();
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ShareAnimationActivity.this, pair1, pair2).toBundle();

            intent.setClass(ShareAnimationActivity.this, DetailActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("name", name);
            startActivity(intent, bundle);
        });
    }
}