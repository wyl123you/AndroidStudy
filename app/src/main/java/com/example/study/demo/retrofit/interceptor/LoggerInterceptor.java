package com.example.study.demo.retrofit.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


public class LoggerInterceptor implements Interceptor {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    @NonNull
    public Response intercept(@NotNull Chain chain) throws IOException {
        Charset UTF8 = StandardCharsets.UTF_8;

        //打印请求报文
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String reqBody = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null) {
                charset = mediaType.charset(UTF8);
            }
            assert charset != null;
            reqBody = toPrettyFormat(buffer.clone().readString(charset));
        }
        String requestStr = "发送请求" + "\n" +
                "请求method: " + request.method() + "\n" +
                "请求url: " + request.url() + "\n" +
                "请求headers: " + request.headers().toString() + "\n" +
                "请求body: " + reqBody + "\n";
        Log.d(TAG, requestStr);

        //打印返回报文
        //先执行请求，才能够获取报文
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String respBody = null;
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();

            Charset charset = UTF8;
            MediaType mediaType = responseBody.contentType();
            if (mediaType != null) {
                charset = mediaType.charset(UTF8);
            }
            assert charset != null;
            respBody = toPrettyFormat(buffer.clone().readString(charset));
        }

        String responseStr = "收到响应" + "\n" +
                "响应code：" + response.code() + "\n" +
                "响应message：" + response.message() + "\n" +
                "响应url：" + response.request().url() + "\n" +
                "响应headers：" + response.headers() + "\n" +
                "响应body：" + respBody + "\n";
        Log.d(TAG, responseStr);
        return response;
    }

    private String toPrettyFormat(String json) {
        JsonElement jsonObject = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
}