package com.example.study.demo.retrofit;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIService {

    @GET("luckyMoneys")
    //接口设置缓存时间段，max-age 的单位是秒, 表示缓存时长
    @Headers("Cache-Control:public,max-age=10")
    Call<ArrayList<LuckyMoney>> getAllLuckyMoneys();

    /**
     * 获取所有的LuckyMoney
     *
     * @return 所有的LuckyMoney
     */
    @GET("luckyMoneys")
    Observable<ArrayList<LuckyMoney>> getAllLuckyMoney();

    /**
     * 通过id获取对应的LuckyMoney
     * luckyMoney/{id}  相当于   luckyMoney/1  （直接带参数）
     *
     * @param id LuckyMoney的主键id
     * @return 对应id的LuckyMoney
     */
    @GET("luckyMoney/{id}")
    Observable<LuckyMoney> getLuckyMoneyById(@Path("id") int id);


    @GET("luckyMoney/{producer}/{consumer}")
    Observable<ArrayList<LuckyMoney>> getByProducerAndConsumer(
            @Path("producer") String producer,
            @Path("consumer") String consumer);


    @GET("luckyMoney.id")
    Observable<LuckyMoney> getLuckyMoneyById(@Query("id") String id);


    @GET("luckyMoney.pc")
    Observable<ArrayList<LuckyMoney>> getByProducerAndConsumer2(
            @Query("producer") String producer,
            @Query("consumer") String consumer
    );

    @GET("luckyMoney.pc")
    Observable<ArrayList<LuckyMoney>> getByProducerAndConsumer3(
            @QueryMap Map<String, String> map);

    @POST("luckyMoney/{producer}/{money}")
    Observable<LuckyMoney> create(
            @Path("producer") String producer,
            @Path("money") String money);

    @POST("luckyMoney")
    Observable<LuckyMoney> create0(
            @Query("producer") String producer,
            @Query("money") String money
    );

    @POST("luckyMoney")
    Observable<LuckyMoney> create0000(
            @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("luckyMoney")
    Observable<LuckyMoney> create999(
            @Field("producer") String producer,
            @Field("money") String money
    );

    @FormUrlEncoded
    @POST("luckyMoney")
    Observable<LuckyMoney> create9999(
            @FieldMap Map<String, String> map);


    @POST("luckyMoney/create")
    Observable<LuckyMoney> create11(
            @Body RequestBody body);

    //只有一个字符串
    @POST("luckyMoney/create1")
    Observable<LuckyMoney> create99(
            @Body RequestBody body);

    @POST("uploadFile")
    Observable<Boolean> uploadFile(
            @Body MultipartBody body);

    @Multipart
    @POST("uploadFile")
    Observable<Boolean> uploadFile2(
            @Part MultipartBody.Part body);
}