package com.example.study.manager;

import com.tencent.mmkv.MMKV;

public class MMKVUtil {

    private static MMKV userMMKV;
    private static MMKV systemMMKV;

    public static void initSystemMMKV() {
        systemMMKV = MMKV.mmkvWithID("system", MMKV.SINGLE_PROCESS_MODE, "1997");
    }


    public static void initUserMMKV() {
        userMMKV = MMKV.mmkvWithID("user", MMKV.SINGLE_PROCESS_MODE, "1997");

    }

    private static MMKV getUserMMKV() {
        if (userMMKV == null) {
            initUserMMKV();
        }
        return userMMKV;
    }

    private static MMKV getSystemMMKV() {
        if (systemMMKV == null) {
            initSystemMMKV();
        }
        return systemMMKV;
    }

    public static void setLanguage(String language) {
        getSystemMMKV().encode(Key.LANGUAGE, language);
    }

    public static String getLanguage() {
        return getSystemMMKV().decodeString(Key.LANGUAGE, "en");
    }

}
