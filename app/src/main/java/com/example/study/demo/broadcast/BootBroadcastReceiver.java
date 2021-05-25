package com.example.study.demo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, @NotNull Intent intent) {
        String action = intent.getAction();

        Log.d("aaaaa", "收到了自己发送的静态广播" + action);

        Toast.makeText(context, "收到了自己发送的静态广播" + action, Toast.LENGTH_LONG).show();
    }
}
