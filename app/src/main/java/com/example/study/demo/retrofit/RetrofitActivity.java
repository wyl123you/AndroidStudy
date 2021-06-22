package com.example.study.demo.retrofit;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityRetrofitBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.study.demo.retrofit.MediaTypes.MEDIA_TYPE_FILE;


public class RetrofitActivity extends BaseActivity<ActivityRetrofitBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String[] a = getAssets().list("");

            for (String s : a) {
                Log.d("WYL", s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Call<ArrayList<LuckyMoney>> call = ApiRequest.create().getAllLuckyMoneys();

        call.enqueue(new Callback<ArrayList<LuckyMoney>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<LuckyMoney>> call, @NonNull Response<ArrayList<LuckyMoney>> response) {
                Toast.makeText(RetrofitActivity.this, "successful", Toast.LENGTH_SHORT).show();
                Log.d("WYL", response.body().toString());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<LuckyMoney>> call, @NonNull Throwable t) {

            }
        });

        File file = new File(Environment.getExternalStorageDirectory(), "wyl.txt");
        String a = "wewrwetertery";
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(a.getBytes());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "RetrofitActivity";
    }

    public void onClick(View v) throws UnsupportedEncodingException {
        switch (v.getId()) {
            case R.id.getAll:
                ApiRequest.create()
                        .getAllLuckyMoney()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<ArrayList<LuckyMoney>>() {
                            @Override
                            public void onNext(ArrayList<LuckyMoney> luckyMonies) {
                                super.onNext(luckyMonies);
                                Toast.makeText(RetrofitActivity.this, luckyMonies.toString(), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < luckyMonies.size(); i++) {
                                    Log.d("WYL", luckyMonies.get(i).toString());
                                }
                            }
                        });
                break;
            case R.id.getById:
                ApiRequest.create()
                        .getLuckyMoneyById(Integer.parseInt(((EditText) findViewById(R.id.et_id)).getText().toString()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.byPAndC:
                ((TextInputLayout) findViewById(R.id.til_1)).setErrorEnabled(true);
                ((TextInputLayout) findViewById(R.id.til_1)).setError("这是一个测试");

                ApiRequest.create()
                        .getByProducerAndConsumer(
                                ((EditText) findViewById(R.id.producer)).getText().toString(),
                                ((EditText) findViewById(R.id.consumer)).getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<ArrayList<LuckyMoney>>() {
                            @Override
                            public void onNext(ArrayList<LuckyMoney> luckyMonies) {
                                super.onNext(luckyMonies);
                                Toast.makeText(RetrofitActivity.this, luckyMonies.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;

            case R.id.getById2:
                ApiRequest.create()
                        .getLuckyMoneyById(((EditText) findViewById(R.id.et_id)).getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.byPAndC2:
                ApiRequest.create()
                        .getByProducerAndConsumer2(
                                ((EditText) findViewById(R.id.producer)).getText().toString(),
                                ((EditText) findViewById(R.id.consumer)).getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<ArrayList<LuckyMoney>>() {
                            @Override
                            public void onNext(ArrayList<LuckyMoney> luckyMonies) {
                                super.onNext(luckyMonies);
                                Toast.makeText(RetrofitActivity.this, luckyMonies.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.byPAndC3:
                String producer = ((EditText) findViewById(R.id.producer)).getText().toString();
                String consumer = ((EditText) findViewById(R.id.consumer)).getText().toString();
                Map<String, String> map = new HashMap<>();
                map.put("producer", producer);
                map.put("consumer", consumer);

                ApiRequest.create()
                        .getByProducerAndConsumer3(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<ArrayList<LuckyMoney>>() {
                            @Override
                            public void onNext(ArrayList<LuckyMoney> luckyMonies) {
                                super.onNext(luckyMonies);
                                Toast.makeText(RetrofitActivity.this, luckyMonies.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.createByPath:
                String producer111 = ((EditText) findViewById(R.id.producer111)).getText().toString();
                String money111 = ((EditText) findViewById(R.id.money111)).getText().toString();
                ApiRequest.create()
                        .create(producer111, money111)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.createByValue:
                String producer11 = ((EditText) findViewById(R.id.producer111)).getText().toString();
                String money11 = ((EditText) findViewById(R.id.money111)).getText().toString();

                ApiRequest.create()
                        .create0(producer11, money11)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.createByValueM:
                String producer1 = ((EditText) findViewById(R.id.producer111)).getText().toString();
                String money1 = ((EditText) findViewById(R.id.money111)).getText().toString();

                Map<String, String> map1 = new HashMap<>();
                map1.put("producer", producer1);
                map1.put("money", money1);

                ApiRequest.create()
                        .create0000(map1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.createByField:
                String producerF = ((EditText) findViewById(R.id.producer111)).getText().toString();
                String moneyF = ((EditText) findViewById(R.id.money111)).getText().toString();

                ApiRequest.create()
                        .create999(producerF, moneyF)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.createByFieldMap:
                Map<String, String> map2 = new HashMap<>();
                map2.put("producer", "11111111111111111111111111");
                map2.put("money", "888888");

                ApiRequest.create()
                        .create9999(map2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;


            case R.id.createByValueB:
                String[] keys = {"id", "producer", "money", "consumer"};
                Object[] values = {"", "WYL", "5000", ""};
                String json = JsonUtil.toJson(keys, values);
                RequestBody body = RequestBody.create(MediaTypes.MEDIA_TYPE_JSON, json);

                ApiRequest.create()
                        .create11(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.createByValuepp:
                String producerQ = "WYL";
                RequestBody body1 = RequestBody.create(MediaTypes.MEDIA_TYPE_JSON, producerQ);
                ApiRequest.create()
                        .create99(body1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<LuckyMoney>() {
                            @Override
                            public void onNext(LuckyMoney luckyMoney) {
                                super.onNext(luckyMoney);
                                Toast.makeText(RetrofitActivity.this, luckyMoney.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.uploadFile:
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(path, "wyl.txt");
                RequestBody fileBody = RequestBody.create(file, MEDIA_TYPE_FILE);
//                MultipartBody.Part part = MultipartBody.Part.createFormData("file", "wyl.txt", fileBody);
                MultipartBody requestBody = new MultipartBody.Builder().setType(MediaType.parse("multipart/form-data"))
                        .addFormDataPart("file", "wyl.txt", fileBody)
                        .build();

                ApiRequest.create()
                        .uploadFile(requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                super.onNext(aBoolean);
                                Toast.makeText(RetrofitActivity.this, aBoolean.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSubscribe(Disposable d) {
                                addToCompositeDisposable(d);
                                super.onSubscribe(d);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Toast.makeText(RetrofitActivity.this, "error", Toast.LENGTH_LONG).show();
                            }
                        });
                break;
            case R.id.uploadFile2:
                String path1 = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file1 = new File(path1, "wyl.txt");


                RequestBody fileBody1 = RequestBody.create(file1, MEDIA_TYPE_FILE);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", "wyl.txt", fileBody1);
//                MultipartBody requestBody1 = new MultipartBody.Builder().setType(MediaType.parse("multipart/form-data"))
//                        .addFormDataPart("file", "wyl.txt", fileBody1)
//                        .build();

                ApiRequest.create()
                        .uploadFile2(part)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                super.onNext(aBoolean);
                                Toast.makeText(RetrofitActivity.this, aBoolean.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSubscribe(Disposable d) {
                                addToCompositeDisposable(d);
                                super.onSubscribe(d);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Toast.makeText(RetrofitActivity.this, "error", Toast.LENGTH_LONG).show();
                            }
                        });
                break;
        }

    }
}