package com.example.study.demo.touchListener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityTouchListenerBinding;

import java.util.concurrent.TimeUnit;

public class TouchListenerActivity extends BaseActivity<ActivityTouchListenerBinding> {
    @Override
    protected void initViews() {
        mBinding.button2.performClick();

        ThreadPool.SimpleTask<Object> task = new ThreadPool.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Exception {
                Log.d("TouchListenerActivity", System.currentTimeMillis() + "");
                return null;
            }
        };

        findViewById(R.id.button2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //Log.d("WYL", System.currentTimeMillis()+"");
                        ThreadPool.executeByIoAtFixRate(task, 500, 3000000, TimeUnit.MILLISECONDS);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        //Log.d("WYL", "停止");
                        task.cancel();
                        return true;
                }


                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_touch_listener;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "TouchListenerActivity";
    }
}
