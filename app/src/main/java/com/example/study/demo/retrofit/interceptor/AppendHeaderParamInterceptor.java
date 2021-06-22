package com.example.study.demo.retrofit.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/22 上午10:43
 * @Company LotoGram
 */

//Header统一追加参数
//https://blog.csdn.net/REIGE/article/details/72591093
public class AppendHeaderParamInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        //核心也是通过newBuilder 拿到Builder
        Headers.Builder builder = request.headers().newBuilder();

        //统一追加header参数
        Headers newHeader = builder
                .add("headeraaaa", "fffffffff")
                .build();

        Request newRequest = request.newBuilder()
                .headers(newHeader)
                .build();

        return chain.proceed(newRequest);
    }
}