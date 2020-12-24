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
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


public class LoggerInterceptor implements Interceptor {
    private static final String TAG = "OKHttpInterceptor";

    @Override
    @NonNull
    public Response intercept(@NotNull Chain chain) throws IOException {

        Log.d("1234", "LoggerInterceptor: ");


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
            try {
                reqBody = toPrettyFormat(buffer.clone().readString(charset));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, String.format("\n发送请求\nmethod：%s\nurl：%s\nheaders: %s\nbody：%s",
                request.method(), request.url(), request.headers(), reqBody));

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
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            assert charset != null;
            respBody = toPrettyFormat(buffer.clone().readString(charset));
        }
        Log.d(TAG, String.format("收到响应\n响应code：%s\n响应message%s\n响应url：%s\n响应headers：%s\n响应body：%s",
                response.code(), response.message(), response.request().url(), response.headers(), respBody));
        return response;
    }

    private String toPrettyFormat(String json) {
        JsonElement jsonObject = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
}
