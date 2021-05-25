package com.example.study.demo.okhttp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.study.R;
import com.example.study.demo.retrofit.LuckyMoney;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText urlEditText;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        urlEditText = (EditText) findViewById(R.id.urlEditText);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "123.txt");

        try {
            FileOutputStream stream = new FileOutputStream("132", true);
            stream.write("1111111111111111111111111111".getBytes("gbk"));
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = file.getAbsolutePath();
        String word = "试试";
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path, true)));
            out.write(word + "\r\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NotNull View v) {
        URL = urlEditText.getText().toString();

        switch (v.getId()) {
            case R.id.button:
                asyncGet();//异步Get请求
                break;

            case R.id.button2:
                syncGet();//同步Get请求
                break;

            case R.id.button3:
                postString();
                break;

            case R.id.button4:
                postFile();
                break;
        }
    }

    private void asyncGet() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log("异步Get请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Log("异步Get请求成功");
                Log(json);
                Gson gson = new Gson();
                LuckyMoney[] luckyMonies = gson.fromJson(json, LuckyMoney[].class);


                for (LuckyMoney luckyMoney : luckyMonies) {
                    Log(luckyMoney.toString());
                }

                runOnUiThread(() -> Toast.makeText(OKHttpActivity.this, json, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void syncGet() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();

        final Call call = client.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                String json = response.body().string();
                Log("同步Get请求成功");
                Log(json);
                Gson gson = new Gson();
                LuckyMoney[] luckyMonies = gson.fromJson(json, LuckyMoney[].class);


                for (LuckyMoney luckyMoney : luckyMonies) {
                    Log(luckyMoney.toString());
                }

                runOnUiThread(() -> Toast.makeText(OKHttpActivity.this, json, Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                Log("同步Get请求失败");
                e.printStackTrace();
            }
        }).start();
    }

    private void postString() {
        URL = "http://115.198.215.80:8081/luckymoney/postString";
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am WYL.";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(requestBody, mediaType))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log("POST 一个  String 失败");
                runOnUiThread(() -> Toast.makeText(OKHttpActivity.this, "POST 一个  String 失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> Toast.makeText(OKHttpActivity.this, "POST 一个  String 成功", Toast.LENGTH_SHORT).show());

                Log("POST 一个  String 成功");
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log(headers.name(i) + ":" + headers.value(i));
                }
                Log("onResponse: " + response.body().string());
                Log("-------------------------------------------------------------------");
            }
        });
    }

    //提交表单
    private void Log(String s) {
        Log.d("WYL", s);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder()
//                .add("search", "Jurassic Park")
//                .build();
//        Request request = new Request.Builder()
//                .url("https://en.wikipedia.org/w/index.php")
//                .post(requestBody)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log("onFailure: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log(response.protocol() + " " +response.code() + " " + response.message());
//                Headers headers = response.headers();
//                for (int i = 0; i < headers.size(); i++) {
//                    Log(headers.name(i) + ":" + headers.value(i));
//                }
//                Log("onResponse: " + response.body().string());
//            }
//        });
    }

    private void postFile() {
        String a = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(a, "wyl.txt");

        URL = "http://115.198.215.80:8081/luckymoney/postFile";
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(file, mediaType))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log("POST 一个  File 失败");
                runOnUiThread(() -> Toast.makeText(OKHttpActivity.this, "POST 一个 File 失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log("POST 一个  File 成功");
                runOnUiThread(() -> Toast.makeText(OKHttpActivity.this, "POST 一个 File 成功", Toast.LENGTH_SHORT).show());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log(headers.name(i) + ":" + headers.value(i));
                }
                Log("onResponse: " + response.body().string());
                Log("-------------------------------------------------------------------");
            }
        });
        Log(file.getAbsolutePath());
    }
}