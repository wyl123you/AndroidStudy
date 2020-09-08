package com.example.study.demo.addressPicker;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("xzqh/query")
    Observable<LocationResp> getProvince(
            @Query("fid") String fid,
            @Query("key") String key);
}
