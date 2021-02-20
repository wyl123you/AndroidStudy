package com.example.study.demo.appicon;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityAppiconBinding;

import java.util.List;

public class AppIconActivity extends BaseActivity<ActivityAppiconBinding> {

    private PackageManager pm;

    private ComponentName default_name;
    private ComponentName test1;
    private ComponentName test2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appicon;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "AppIconActivity";
    }

    @Override
    protected void initViews() {

        pm = getPackageManager();

        default_name = getComponentName();
        test1 = new ComponentName(getBaseContext(), "com.example.study.TestIcon1");
        test2 = new ComponentName(getBaseContext(), "com.example.study.TestIcon2");

    }

    private void enableComponent(ComponentName componentName) {
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName) {
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public void onAppIcon1(View view) {
        enableComponent(test1);
        disableComponent(test2);
        disableComponent(default_name);
        restartSystemLauncher();
    }

    public void onDefault(View view) {
        disableComponent(test1);
        disableComponent(test2);
        enableComponent(default_name);
        restartSystemLauncher();
    }

    public void restartSystemLauncher() {
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(i, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }
}