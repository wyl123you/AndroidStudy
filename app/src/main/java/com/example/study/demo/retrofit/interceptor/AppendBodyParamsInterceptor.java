package com.example.study.demo.retrofit.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/22 上午10:46
 * @Company LotoGram
 */

//Post请求body统一追加参数
//https://blog.csdn.net/REIGE/article/details/72591093
public class AppendBodyParamsInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        RequestBody body = request.body();

        if (body == null) {
            return chain.proceed(request);
        } else {
            request.body();

            //构建新Body
            RequestBody newBody = RequestBody.create("token=iamtoken", MediaType.parse("application/json"));

            Request newRequest = request.newBuilder()
                    .post(newBody)
                    .build();
            return chain.proceed(request);
        }
    }
}