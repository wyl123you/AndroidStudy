package com.example.study.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     *
     * @param context * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "0";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();

        return mac;
    }

    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    private static String getMacAddress() {
        String macSerial = null;
        String str = "0";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim(); //去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     *
     * @return
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

    @NotNull
    public static String getMac(Context context) {
        String mac = "0";
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
        if (mac == null || mac.equals("")) {
            mac = "0";
        }
        return mac;
    }
}