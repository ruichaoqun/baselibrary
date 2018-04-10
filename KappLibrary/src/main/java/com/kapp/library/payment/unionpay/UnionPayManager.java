package com.kapp.library.payment.unionpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.text.TextUtils;
import android.widget.Toast;

import com.kapp.library.broadnotify.BroadNotifyUtils;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/1 0001.
 * 银联支付结果回调在当前支付界面，需要在OnActivityResult中调用OnPaymentResult方法
 * https://open.unionpay.com/ajweb/index
 */
public class UnionPayManager {

    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";

    /**
     * 调起支付
     */
    public void payment(final Activity context, UnionPayInfo info) {


        int ret = UPPayAssistEx.startPay(context, null, null, info.getTn(), mMode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            Toast.makeText(context, "银联支付调用失败！", Toast.LENGTH_SHORT).show();
        }
    }


    /** 支付结果回调：应以后台数据为准，此处为参考 */
    public void onPaymentResult(Intent data) {
        if (data == null)
            return;

        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result", "");
        if (TextUtils.isEmpty(str))
            return;

        Bundle bundle = new Bundle();
        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            bundle.putBoolean("resultFlag" ,true);
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    bundle.putString("sign", sign);
                    bundle.putString("dataOrg", dataOrg);
                } catch (JSONException e) {
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
            }
            BroadNotifyUtils.sendReceiver(BroadNotifyUtils.NOTIFY_UNIONPAY, bundle);
        } else if (str.equalsIgnoreCase("fail")) {
            bundle.putBoolean("resultFlag" ,false);
            BroadNotifyUtils.sendReceiver(BroadNotifyUtils.NOTIFY_UNIONPAY, bundle);
        } else if (str.equalsIgnoreCase("cancel")) {
            bundle.putBoolean("resultFlag" ,false);
            BroadNotifyUtils.sendReceiver(BroadNotifyUtils.NOTIFY_UNIONPAY, bundle);
        }
    }
}
