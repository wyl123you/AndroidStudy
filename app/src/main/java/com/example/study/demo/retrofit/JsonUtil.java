package com.example.study.demo.retrofit;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    public static String toJson(String[] keys, Object[] values) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            if (values[i] != null)
                map.put(keys[i], values[i]);
        }
        return new JSONObject((map)).toString();
    }
}
