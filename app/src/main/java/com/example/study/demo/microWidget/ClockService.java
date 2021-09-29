package com.example.study.demo.microWidget;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.study.MainActivity;
import com.example.study.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 更新小组件事件的服务
 *
 * @author lgl
 */
public class ClockService extends Service {

    // 定时器
    private Timer timer;
    // 日期格式
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Log.d("ClockProvider", "onCreate: ");
        // TODO Auto-generated method stub
        super.onCreate();
        timer = new Timer();
        /**
         * 参数：1.事件2.延时事件3.执行间隔事件
         */
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                updateView();
                Log.d("ClockProvider", "发送更新");
            }
        }, 0, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        return START_REDELIVER_INTENT;
    }

    private void createNotificationChannel() {
        Intent intent = new Intent(); //点击后跳转的界面，可以设置跳转数据
        intent.setClass(this, MainActivity.class);

        //获取一个Notification构造器
        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.face)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("SMI InstantView") //设置下拉列表里的标题
                .setSmallIcon(R.drawable.face) // 设置状态栏内的小图标
                .setContentText("is running......") //设置上下文内容
                .setWhen(System.currentTimeMillis() + 2000); //设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(111, notification);
    }

    /**
     * 更新事件的方法
     */
    private void updateView() {
        // 时间
        String time = sdf.format(new Date());
        /**
         * 参数：1.包名2.小组件布局
         */
        RemoteViews rViews = new RemoteViews(getPackageName(),
                R.layout.clock_provider);
        // 显示当前事件
        rViews.setTextViewText(R.id.appwidget_text, time);

        // 刷新
        AppWidgetManager manager = AppWidgetManager
                .getInstance(getApplicationContext());
        ComponentName cName = new ComponentName(getApplicationContext(),
                ClockProvider.class);
        manager.updateAppWidget(cName, rViews);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        timer = null;
        Log.d("ClockProvider", "onDestroy: ");
    }
}