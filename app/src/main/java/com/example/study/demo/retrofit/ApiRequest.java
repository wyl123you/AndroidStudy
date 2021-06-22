package com.example.study.demo.retrofit;

import com.example.study.MyApplication;
import com.example.study.demo.retrofit.interceptor.LoggerInterceptor;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequest {

    @NotNull
    public static ApiService create() {

        Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), 1024 * 10);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor())
                //.addInterceptor(new CacheInterceptor())
                //.addNetworkInterceptor(new CacheInterceptor())
//                .sslSocketFactory(
                .cache(cache)
                .build();

        CacheControl control = new CacheControl.Builder()
                .maxAge(0, TimeUnit.SECONDS)//设置缓存数据的有效期，超出有效期自动清空
                .maxStale(1, TimeUnit.DAYS)//设置缓存数据过时时间，超出时间自动清空
                .build();


        //构建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jarka.cn/demo/")
//                .baseUrl("http://10.0.1.164:8088/")
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }
}