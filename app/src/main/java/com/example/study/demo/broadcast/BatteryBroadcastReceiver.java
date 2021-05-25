package com.example.study.demo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

public class BatteryBroadcastReceiver extends BroadcastReceiver {

    private String TAG = "Battery";

    @Override
    public void onReceive(Context context, @NotNull Intent intent) {
        String action = intent.getAction();


        switch (action) {
            case Intent.ACTION_POWER_CONNECTED:
                Log.d(TAG, "插上电源");
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                Log.d(TAG, "拔出电源");
                break;
            case Intent.ACTION_BATTERY_CHANGED:
                Log.d(TAG, "电量变化");
                break;
            case Intent.ACTION_BATTERY_LOW:
                Log.d(TAG, "低电量");
                break;
            case Intent.ACTION_BATTERY_OKAY:
                Log.d(TAG, "电源充满");
                break;
        }

    }
}
