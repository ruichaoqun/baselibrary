package com.kapp.library.manager;

import android.app.Activity;
import android.app.ActivityManager;

import com.kapp.library.KAPPApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11 0011.
 * Activity栈管理
 */
public class AppSkipManager {

    private static List<Activity> arrayList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        arrayList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        arrayList.remove(activity);
    }

    /**
     * 只关闭Activity操作
     */
    public static void finishAllActivity() {
        for (Activity activity : arrayList) {
            if (activity != null && !activity.isFinishing())
                activity.finish();
        }
    }

    /**
     * 推出APP
     */
    public static void exitApp() {
        for (Activity activity : arrayList) {
            if (activity != null && !activity.isFinishing())
                activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

        ActivityManager manager = (ActivityManager) KAPPApplication.getContext().
                getSystemService(KAPPApplication.getContext().ACTIVITY_SERVICE);
        manager.killBackgroundProcesses(KAPPApplication.getContext().getPackageName());
    }

    // 是否为空列表
    public static boolean isAllFinish() {
        return arrayList.isEmpty();
    }
}
