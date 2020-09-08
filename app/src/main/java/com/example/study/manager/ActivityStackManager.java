package com.example.study.manager;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityStackManager {

    private static ArrayList<Activity> activityList = new ArrayList<>();

    public static ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public static void addActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
            activityList.add(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    public static void finishAllActivity() {
        for (Activity activity : getActivityList()) {
            activity.finish();
        }
    }
}
