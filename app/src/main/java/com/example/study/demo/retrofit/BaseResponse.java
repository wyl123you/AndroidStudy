package com.example.study.demo.retrofit;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    private String status;
    private int code;
    private String message;
    private long nextId;

    public BaseResponse() {
    }

    public BaseResponse(String status, int code, String message, long nextId) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.nextId = nextId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }
}
