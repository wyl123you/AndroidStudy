package com.example.study.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/1/21 上午11:35
 * @Company LotoGram
 */

@SuppressLint("HardwareIds")
public class MacAddressUtil {

    private static final String TAG = "MacAddressUtil";

    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     *
     * @param context * @return
     */
    @Nullable
    private static String getMacDefault(Context context) {
        if (context == null) return null;
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info == null ? null : info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    private static String getMacAddress() {
//        String macSerial = null;
//        String str = "0";
//
//        try {
//            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
//            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//
//            while (null != str) {
//                str = input.readLine();
//                if (str != null) {
//                    macSerial = str.trim(); //去空格
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            // 赋予默认值
//            ex.printStackTrace();
//        }
//
//        return macSerial;
        try {
            String path = "/sys/class/net/wlan0/address";
            File file = new File(path);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"/>
     */
    @Nullable
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
            Log.d(TAG, "getMacDefault: ");
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
            Log.d(TAG, "getMacAddress: ");
        } else {
            mac = getMacFromHardware();
            Log.d(TAG, "getMacFromHardware: ");
        }
        return mac;
    }
}