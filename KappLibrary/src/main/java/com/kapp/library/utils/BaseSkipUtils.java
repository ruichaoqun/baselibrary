package com.kapp.library.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import com.kapp.library.widget.pictureimage.PicturePagerActivity;
import com.kapp.library.widget.pictureimage.PicturePagerInfo;

/**
 * Created by Administrator on 2016/12/8 0008.
 * 跳转记录类
 */
public class BaseSkipUtils {

    /**
     * 启动大图查看模式
     * */
    public static void startPicturePagerActivity(Activity context, PicturePagerInfo info){
        Intent intent = new Intent(context, PicturePagerActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }

    /**
     * 调起系统发短信功能
     * @param message
     */
    public static void startSendSMS(Context context, String message){
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }


    /** 复制文本到剪贴板 */
    public static void startCopeTxt(Context context, String value) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        ClipData clipData = ClipData.newPlainText("text", value);
        cm.setPrimaryClip(clipData);
        ToastUtils.showToast("复制成功！！");
    }
}
