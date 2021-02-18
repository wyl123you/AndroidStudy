package com.example.study.demo.jni;

import java.util.Map;

public class NDKTools {

    static {
        System.loadLibrary("a");
    }

    public static native int add(int a, int b);

    public static native String getStringFromNDK(String var1);

    public static native String getSign(String url, Map<String, Object> map, String key);

}
