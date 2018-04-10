package com.kapp.library.utils;

import android.widget.Toast;

import com.kapp.library.KAPPApplication;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class ToastUtils {

    private static Toast toast;

    public static void showToast(String value){
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(KAPPApplication.getContext(), value, Toast.LENGTH_SHORT);
        toast.show();
    }
}
