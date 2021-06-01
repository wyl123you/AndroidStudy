package com.example.study.demo.Observer;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午11:33
 * @Company LotoGram
 */

public class Dog implements Observer, LifecycleObserver, androidx.lifecycle.Observer<String> {

    private static final String TAG = "Dog";

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void update(Observable o, Object arg) {
        String aa = (String) arg;
        Log.d(TAG, "update: 收到通知" + aa);
    }

    @Override
    public void onChanged(String s) {
        Log.d(TAG, "onChanged: androidx.lifecycle.Observer<String>" + s);
    }
}
