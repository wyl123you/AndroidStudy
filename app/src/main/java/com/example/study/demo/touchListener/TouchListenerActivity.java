package com.example.study.demo.touchListener;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityTouchListenerBinding;

public class TouchListenerActivity extends BaseActivity<ActivityTouchListenerBinding> {

    private int a = 0;
    private int b = 100;

    @Override
    protected void initViews() {

//        ThreadPool1.SimpleTask<Object> task = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "1");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground: 1  " + (a++));
//                }
//                return null;
//            }
//        };
//
//        ThreadPool1.SimpleTask<?> task1 = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "2");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground: 2  " + (b--));
//                }
//                return null;
//            }
//        };
//
//        ThreadPool1.SimpleTask<?> task3 = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "3");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground:3  " + (b--));
//                }
//                return null;
//            }
//        };
//
//        ThreadPool1.SimpleTask<?> task4 = new ThreadPool1.SimpleTask<Object>() {
//            @Nullable
//            @Override
//            public Object doInBackground() throws Exception {
//                Log.d("TouchListenerActivity", System.currentTimeMillis() + "4");
//                for (int i = 0; i < 3000; i++) {
//                    Log.d("TouchListenerActivity", System.currentTimeMillis() + "doInBackground:43  " + (b--));
//                }
//                return null;
//            }
//        };
//
////        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ThreadPool1.executeBySingle(task);
//                ThreadPool1.executeBySingle(task1);
//                ThreadPool1.executeBySingle(task3);
//                ThreadPool1.executeBySingle(task4);
//            }
//        });

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
