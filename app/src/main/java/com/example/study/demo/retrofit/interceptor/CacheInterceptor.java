package com.example.study.demo.retrofit.interceptor;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Log.d("1234", "CacheInterceptor: ");

        Request request = chain.request();

        CacheControl control = new CacheControl.Builder()
                .maxAge(0, TimeUnit.SECONDS)//设置缓存数据的有效期，超出有效期自动清空
                .maxStale(1, TimeUnit.DAYS)//设置缓存数据过时时间，超出时间自动清空
                .build();
        request = request.newBuilder()
                .cacheControl(control)
                .build();


        return chain.proceed(request);
    }
}
