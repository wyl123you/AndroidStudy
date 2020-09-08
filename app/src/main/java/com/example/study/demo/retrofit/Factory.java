package com.example.study.demo.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Factory {

    public static APIService create() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(new OKHttpInterceptor())
//                .sslSocketFactory(
                .build();

        CacheControl control = new CacheControl.Builder()
                .maxAge(0, TimeUnit.SECONDS)//设置缓存数据的有效期，超出有效期自动清空
                .maxStale(1, TimeUnit.DAYS)//设置缓存数据过时时间，超出时间自动清空
                .build();


        //构建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.137.211/demo/")
//                .baseUrl("http://192.168.124.18:8081/")
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(APIService.class);
    }
}