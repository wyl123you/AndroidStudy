package com.example.study.demo.addressPicker;

import com.example.study.demo.retrofit.OKHttpInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequest {

    private static final String BASE_URL = "http://apis.juhe.cn/";
    private Retrofit retrofit;

    public static ApiRequest getInstance() {
        return ApiRequestHolder.INSTANCE;
    }

    public <T> T create(Class<T> cla) {
        initRetrofit();
        return retrofit.create(cla);
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(new OKHttpInterceptor())
//                .sslSocketFactory(
                .build();
        //构建Retrofit实例
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private static class ApiRequestHolder {
        public static final ApiRequest INSTANCE = new ApiRequest();
    }
}
