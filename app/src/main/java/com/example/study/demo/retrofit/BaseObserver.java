package com.example.study.demo.retrofit;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        Log.d("WYL", "onSubscribe");
    }

    @Override
    public void onNext(T t) {
        Log.d("WYL", "onNext");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.d("WYL", "onError：" + e.toString());
    }

    @Override
    public void onComplete() {
        Log.d("WYL", "onComplete");
    }
}
