package com.kapp.library.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/9/19 0019.
 *
 * 数据
 */

public class NumUtils {

    public static int getValueInt(String value){
        return getValueInt(value, 0);
    }

    /** String 转 Int */
    public static int getValueInt(String value, int defaultNum){
        if (TextUtils.isEmpty(value))
            return defaultNum;

        try{
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            return defaultNum;
        }
    }

    /** String 转 long */
    public static long getValueLong(String value){
        if (TextUtils.isEmpty(value))
            return 0L;

        try{
            return Long.parseLong(value);
        }catch (NumberFormatException e){
            return 0L;
        }
    }

    /** String 转 float */
    public static float getValueFloat(String value){
        if (TextUtils.isEmpty(value))
            return 0f;

        try{
            return Float.parseFloat(value);
        }catch (NumberFormatException e){
            return 0f;
        }
    }

    /** String 转 double */
    public static double getValueDouble(String value){
        if (TextUtils.isEmpty(value))
            return 0d;

        try{
            return Double.parseDouble(value);
        }catch (NumberFormatException e){
            return 0d;
        }
    }

}
