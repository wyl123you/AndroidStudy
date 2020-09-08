package com.example.study.demo.addressPicker;

public class HttpEngine {

    private static ApiService api;

    public static ApiService getApi() {
        return ApiRequest.getInstance().create(ApiService.class);
    }


}
