package com.umengs.library.analytics;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/9/19.
 * 友盟统计
 */
public class UmStateUtils {

    private static UmStateUtils umStateUtils = null;

    public static UmStateUtils getInstances(){
        if (umStateUtils == null) {
            umStateUtils = new UmStateUtils();
            MobclickAgent.enableEncrypt(true);//日志加密
            umStateUtils.setErrorReport(true);//错误统计
        }
        return umStateUtils;
    }

    /** 统计Activity开始时间 */
    public void onStartTimes(Context context){
        MobclickAgent.onResume(context);
    }

    /** 统计Activity结束时间 */
    public void onEndTimes(Context context){
        MobclickAgent.onPause(context);
    }

    /** 统计UI开始时间 */
    public void onStartUIType(String className){
        MobclickAgent.onPageStart(className);
    }

    /** 统计UI结束时间 */
    public void onEndUIType(String className){
        MobclickAgent.onPageEnd(className);
    }

    /** 帐号登入统计 */
    public void accountIn(String accountId){
        MobclickAgent.onProfileSignIn(accountId);
    }

    /** 帐号登出统计 */
    public void accountOff(){
        MobclickAgent.onProfileSignOff();
    }

    /** 错误统计 */
    public void setErrorReport(boolean errorReport){
        MobclickAgent.setCatchUncaughtExceptions(errorReport);
    }

    /** Kill之前保存数据 */
    public void onKillProcess(Context context){
        MobclickAgent.onKillProcess(context);
    }
}
