package com.example.study.demo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "";

    @Override
    public void onReceive(Context context, @NotNull Intent intent) {

        Toast.makeText(context, "网络变化", Toast.LENGTH_LONG).show();

    }
}
