package com.example.study.demo.refreshRecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivitySmartRefreshBinding;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class SmartRefreshActivity extends BaseActivity<ActivitySmartRefreshBinding> {

    @Override
    protected void initViews() {

        mBinding.list.setAdapter(new ListAdapter(this, getData()));
        mBinding.list.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mBinding.refresh.setRefreshHeader(new FalsifyHeader(this));
        mBinding.refresh.setRefreshFooter(new BallPulseFooter(this));


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smart_refresh;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "SmartRefreshActivity";
    }

    @NotNull
    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            int a = random.nextInt(200);
            data.add("数据" + a);
            Log.d(TAG, "数据" + a);
        }
        return data;
    }
}