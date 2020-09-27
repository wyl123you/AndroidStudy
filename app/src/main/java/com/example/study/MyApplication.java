package com.example.study;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.study.demo.notificationDemo.NotifyUtil;
import com.example.study.manager.ActivityStackManager;
import com.example.study.manager.FragmentStackManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyApplication extends MultiDexApplication {

    private static final String TAG = "MyApplication";
    private static volatile MyApplication instance;

    //静态代码块可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            return new ClassicsHeader(context);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context);
//                        .setDrawableSize(20);
        });
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        initBugly();
        initNotificationChannel();
        initActivityCallbacks();
    }

    private void initActivityCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                Log.d(TAG, "onActivityCreated: ");
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.d(TAG, "onActivityStarted: ");
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.d(TAG, "onActivityResumed: ");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.d(TAG, "onActivityPaused: ");
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.d(TAG, "onActivityStopped: ");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
                Log.d(TAG, "onActivitySaveInstanceState: ");
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.d(TAG, "onActivityDestroyed: ");
            }
        });

    }

    private void initBugly() {
        String appId = null;
        String appKey = null;
        ApplicationInfo info;
        try {
            info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appId = info.metaData.getString("BUGLY_APPID");
            appKey = info.metaData.getString("BUGLY_APPKEY");
            Log.d(TAG, "appId:" + appId + "   appKey:" + appKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));

        if (appId != null && appKey != null) {
            // 初始化Bugly
            //第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            //输出详细的Bugly SDK的Log；
            //每一条Crash都会被立即上报；
            //自定义日志将会在Logcat中输出。
            //建议在测试阶段建议设置成true，发布时设置为false。
            CrashReport.initCrashReport(getApplicationContext(), appId, true, strategy);
            // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
            // CrashReport.initCrashReport(context, strategy);
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initNotificationChannel() {
        //创建聊天消息通道
        NotifyUtil.createChannel(
                getApplicationContext(),
                NotifyUtil.CHANNEL_MESSAGE,
                NotifyUtil.CHANNEL_MESSAGE,
                NotificationManager.IMPORTANCE_HIGH);

        //创建推送消息通道
        NotifyUtil.createChannel(
                getApplicationContext(),
                NotifyUtil.CHANNEL_PUSH,
                NotifyUtil.CHANNEL_PUSH,
                NotificationManager.IMPORTANCE_HIGH);

        //创建通知消息通道
        NotifyUtil.createChannel(
                getApplicationContext(),
                NotifyUtil.CHANNEL_NOTICE,
                NotifyUtil.CHANNEL_NOTICE,
                NotificationManager.IMPORTANCE_HIGH);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate:应用被系统终止");
        instance = this;
        ActivityStackManager.finishAllActivity();
        FragmentStackManager.destroyAllFragment();
        //service,broadcast等等
        System.exit(0);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory:应用处于低内存状态");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG, "onTrimMemory:应用正在清除内存");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}