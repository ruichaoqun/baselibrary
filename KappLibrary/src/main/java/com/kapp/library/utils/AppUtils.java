package com.kapp.library.utils;

import android.provider.Settings;

import com.kapp.library.KAPPApplication;

/**
 * Created by Administrator on 2016/11/25 0025.
 */
public class AppUtils {

    /** 获取机器码 */
    public static String getMchId(){
        return Settings.Secure.getString(KAPPApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
