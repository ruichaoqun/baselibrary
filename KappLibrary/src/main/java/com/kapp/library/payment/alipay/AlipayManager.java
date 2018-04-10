package com.kapp.library.payment.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.kapp.library.broadnotify.BroadNotifyUtils;
import com.kapp.library.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/21 0021.
 * 支付宝支付-管理类
 *
 * 注：次支付使用APP支付接口，蚂蚁金服上需要公匙上传至开放平台（合作伙伴上传公匙是移动支付接口与APP支付不同）
 */
public class AlipayManager {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "xxxxx";

    public static final int SDK_PAY_FLAG = 1;
    private PayTask payTask;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    Bundle bundle = new Bundle();
                    bundle.putString("result", resultInfo);
                    bundle.putBoolean("resultFlag", TextUtils.equals(resultStatus, "9000"));
                    BroadNotifyUtils.sendReceiver(BroadNotifyUtils.NOTIFY_ALIPAY, bundle);
                    break;
                }
                default:
                    break;
            }
        };
    };

    /** 调起支付 */
    public void payment(Activity context, AlipayInfo info){

        payTask = new PayTask(context);
        Map<String, String> params = buildOrderParamMap(info);
        if (params == null) {
            Toast.makeText(context, "业务参数生成失败！！", Toast.LENGTH_SHORT).show();
            return;
        }
        logger.i(params.toString());
        String orderParam = buildOrderParam(params);
        logger.i(orderParam);
        String sign = getSign(params, info.getKey());
        logger.i(sign);
        final String orderInfo = orderParam + "&" + sign;
        logger.i(orderInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                Map<String, String> result = payTask.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /** 构造支付订单参数列表 */
    public Map<String, String> buildOrderParamMap(AlipayInfo info) {

        String biz_content = formatBizContent(info);
        if (TextUtils.isEmpty(biz_content)){
            return null;
        }

        Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put("app_id", APPID);
        keyValues.put("biz_content", biz_content);
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("notify_url", info.getNotifyUrl());
        keyValues.put("sign_type", "RSA");
        keyValues.put("timestamp", getPayTime());
        keyValues.put("format", "json");
//        keyValues.put("partner", info.getPartner());
//        keyValues.put("service", "mobile.securitypay.pay");
//        keyValues.put("body", info.getDescription());//交易描述
//        keyValues.put("subject", info.getGoodsName());//商品的标题/交易标题/订单标题/订单关键字等。
//        keyValues.put("out_trade_no", info.getOrderNo());//订单
//        keyValues.put("timeout_express", "30m");//交易逾期时间
//        keyValues.put("total_amount", String.valueOf(info.getPrice()));//交易金额，单位元
//        keyValues.put("seller_id", info.getSeller());//支付宝帐号，未指定默认签约商户支付宝帐号
//        keyValues.put("product_code", "QUICK_MSECURITY_PAY");//销售产品码，固定值
        keyValues.put("version", "1.0");
        return keyValues;
    }

    //业务参数
    private String formatBizContent(AlipayInfo info){
        JSONObject json = new JSONObject();
        try {
            json.put("body", info.getDescription());//交易描述
            json.put("subject", info.getGoodsName());//商品的标题/交易标题/订单标题/订单关键字等。
            json.put("out_trade_no", info.getOrderNo());//订单
            json.put("timeout_express", "30m");//交易逾期时间
            json.put("total_amount", String.valueOf(info.getPrice()));//交易金额，单位元
            json.put("seller_id", info.getSeller());//支付宝帐号，未指定默认签约商户支付宝帐号
            json.put("product_code", "QUICK_MSECURITY_PAY");//销售产品码，固定值
            return json.toString();
        }catch (JSONException e){
            return null;
        }
    }

    //支付时间
    private String getPayTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString().replace("+", "%20");
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *            待签名授权信息
     *
     * @return
     */
    private String getSign(Map<String, String> map, String rsaKey) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    private String getSDKVersion() {
        String version = payTask.getVersion();
        return version;
    }

}
