package com.example.study.demo.notificationDemo;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifyUtil {

    public static int ID = 0;

    public static final String CHANNEL_MESSAGE = "聊天消息";
    public static final String CHANNEL_PUSH = "推送消息";
    public static final String CHANNEL_NOTICE = "重要通知";

    public static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * IMPORTANCE_NONE 关闭通知
     * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
     * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
     * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
     * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
     *
     * @param context    上下文
     * @param id         id
     * @param name       name
     * @param importance importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static void createChannel(Context context, String id, String name, int importance) {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        //配置通知渠道的属性
        channel.setDescription("收到通知");
        //设置通知出现时声音，默认通知是有声音的
        //channel.setSound(null, null);
        //通知是否显示角标
        channel.setShowBadge(true);
        //设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(true);
        //设置是否振动
        channel.enableVibration(true);
        //设置振动模式
        //channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setLightColor(Color.RED);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        //创建通道
        notificationManager.createNotificationChannel(channel);
    }

    public static int getID() {
        return ID++;
    }

    public static void openNotification(Context context) {
        //进入设置系统应用权限界面
        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
//        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
//        intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
//        context.startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
