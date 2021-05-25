package com.example.study.demo.broadcast;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.study.R;

public class BroadcastActivity extends AppCompatActivity {

    private NetworkBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, 100);
        }



        /*
         * 标准广播：完全一步执行的广播，发出广播后，所有的广播接受者几乎会在同一时间收到这条广播
         * 有序广播：同步执行的一种广播，发出广播后，同一时间只有一个广播接受者能收到，当这个广播
         *         逻辑执行完才会传递到下一个接受者，当前的接受者还可以阶段广播的继续传递，那么
         *         后续的广播就无法接受到广播信息了
         */

        /*
         * 动态注册：就是在java代码中制定IntentFilter，然后添加不同的Action即可，想监听什么广
         *         播就写什么Action，另外动态注册的广播一定要调用unregisterReceiver取消注册
         *
         * 静态注册：动态注册需要程序启动后才能接受广播，静态广播就弥补了这个短板，AndroidManifest
         *         中制定<IntentFilter>就可以让程序在为启动的情况下就能接受广播了
         */


        //动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new NetworkBroadcastReceiver();
        registerReceiver(receiver, filter);


        //静态注册广播在AndroidManifest中
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void sendBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction("qqqqqqqqqqqqqq");
        intent.setAction("dddddddd");
        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setComponent(new ComponentName(getPackageName(), BootBroadcastReceiver.class.getName()));
        sendBroadcast(intent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(new BatteryBroadcastReceiver(),filter);
    }
}