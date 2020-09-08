package com.example.study.demo.addressPicker;

import android.content.Intent;

import java.util.ArrayList;

public class LocationResp {

    private String reason;
    private ArrayList<Province> result;
    private Integer error_code;

    public String getReason() {
        return reason;
    }

    public ArrayList<Province> getResult() {
        return result;
    }

    public Integer getError_code() {
        return error_code;
    }
}
