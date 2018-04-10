package com.kapp.library.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/11 0011.
 *
 * 日志输出
 */
public class Logger {

    private String tag;
    private static boolean isWrite = true;//是否打印Log

    public Logger(String tag) {
        this.tag = tag;
    }

    public void i(String msg){
        if (isWrite)
            Log.i(tag, msg);
    }

    public void i(String msg, Throwable throwable){
        if (isWrite)
            Log.i(tag, msg, throwable);
    }

    public void e(String msg){
        if (isWrite)
            Log.e(tag, msg);
    }

    public void e(String msg, Throwable throwable){
        if (isWrite)
            Log.e(tag, msg, throwable);
    }

    public void w(String msg){
        if (isWrite)
            Log.w(tag, msg);
    }

    public void w(Throwable throwable){
        if (isWrite)
            Log.e(tag, tag, throwable);
    }

    public void w(String msg, Throwable throwable){
        if (isWrite)
            Log.w(tag, msg, throwable);
    }

    public void d(String msg){
        if (isWrite)
            Log.d(tag, msg);
    }

    public void d(String msg, Throwable throwable){
        if (isWrite)
            Log.d(tag, msg, throwable);
    }

    public void v(String msg){
        if (isWrite)
            Log.v(tag, msg);
    }

    public void v(String msg, Throwable throwable){
        if (isWrite)
            Log.v(tag, msg, throwable);
    }

    public void test_i(String tag, String value){
        i(" --------------------------------------------------------------------- ");
        i(tag+value);
        i(" --------------------------------------------------------------------- ");
    }

    public static void setWriteLog(boolean isWrite){
        Logger.isWrite = isWrite;
    }

    public static boolean getWriteLog(){
        return Logger.isWrite;
    }
}
