package com.example.study.demo.retrofit.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/22 上午10:38
 * @Company LotoGram
 */

//Get请求统一追加参数
//https://blog.csdn.net/REIGE/article/details/72591093
public class AppendUrlParamInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        ////拿到有信息的builder
        HttpUrl.Builder builder = request.url().newBuilder();

        //追加好参数，得到新的Url
        HttpUrl newHttpUrl = builder
                .addQueryParameter("aaaaaa", "aaaaaaaaaaaa")
                .build();

        //利用新的Url 构建新的Request ，并发送给服务器
        Request newRequest = request.newBuilder()
                .url(newHttpUrl)
                .build();
        return chain.proceed(newRequest);
    }
}