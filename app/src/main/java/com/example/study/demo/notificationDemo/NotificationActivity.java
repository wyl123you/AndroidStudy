package com.example.study.demo.notificationDemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.study.BaseActivity;
import com.example.study.MainActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityNotificationBinding;

public class NotificationActivity extends BaseActivity<ActivityNotificationBinding> {

    @Override
    protected void initViews() {
        mBinding.setClick(new ClickHandler());
        Toast.makeText(this, NotifyUtil.isNotificationEnabled(this) ? "通知已打开" : "通知未打开", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Build.MANUFACTURER, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "initViews: " + Build.VERSION.SDK_INT);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(NotificationActivity.this, NotifyUtil.CHANNEL_MESSAGE)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("标题11111")
                .setContentText("主要内容11111")
                .setSubText("详细内容1111")
                .setTicker("状态栏通知信息11111")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.welcome))
                .setSmallIcon(R.drawable.face)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NotifyUtil.getID(), notification);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "NotificationActivity";
    }

    public class ClickHandler {
        //普通通知
        public void commonNotification() {
            Log.d(getTag(), "发送通知");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, NotifyUtil.CHANNEL_MESSAGE)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("标题")
                    .setContentText("主要内容")
                    .setSubText("详细内容")
                    .setTicker("状态栏通知信息")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.welcome))
                    .setSmallIcon(R.drawable.android)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NotifyUtil.getID(), notification);
        }

        //自定义试图通知
        public void foldNotification() {
            //用RemoteViews来创建自定义Notification视图
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.item);
            //其他的和普通Notification一样
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, NotifyUtil.CHANNEL_PUSH)
                    .setContentTitle("标题")
                    .setContentText("主要内容")
                    .setSubText("详细内容")
                    .setTicker("状态栏通知信息")
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.welcome))
                    .setSmallIcon(R.drawable.android)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notification.bigContentView = views;
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NotifyUtil.getID(), notification);
        }

        //悬挂式通知
        public void hangNotification() {
            //设置悬挂，通过setFullScreenIntent()

            Intent hangIntent = new Intent();
            hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            hangIntent.setClass(NotificationActivity.this, MainActivity.class);
            PendingIntent intentsss = PendingIntent.getActivity(NotificationActivity.this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this, NotifyUtil.CHANNEL_NOTICE)
                    .setContentTitle("标题")
                    .setContentText("主要内容")
                    .setSubText("详细内容")
                    .setTicker("状态栏通知信息")
                    .setWhen(System.currentTimeMillis())
                    .setFullScreenIntent(intentsss, true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.welcome))
                    .setSmallIcon(R.drawable.android)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NotifyUtil.getID(), notification);

        }

        public void openNotification() {
            NotifyUtil.openNotification(NotificationActivity.this);
        }
    }
}