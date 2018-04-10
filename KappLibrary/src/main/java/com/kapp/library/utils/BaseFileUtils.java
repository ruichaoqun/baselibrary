package com.kapp.library.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class BaseFileUtils {

    /** 程序基础目录 */
    public static String getRootPath(String rootPath){
        return getCachePath()+rootPath;
    }

    /** 创建目录 */
    public static void makirPath(){
    }

    public static String getCachePath(){
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(sdCardExist) {
            return Environment.getExternalStorageDirectory().getPath();//获取跟目录
        }
        return Environment.getRootDirectory().getPath();
    }
}
