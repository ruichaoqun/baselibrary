package com.kapp.library.payment.wxpay;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.kapp.library.KAPPApplication;
import com.kapp.library.utils.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2016/11/21 0021.
 * 微信支付-管理类
 */
public class WXPayManager{

    private Logger logger = new Logger(this.getClass().getSimpleName());
    // APP_ID 替换为你的应用从官方网站申请到的合法appId：wx5182c28c4165e637 -- 2142510d428ad320353a79239fdae15b
    public static final String APP_ID = "xxx";
    private IWXAPI iwxapi = null;

    public WXPayManager(){
        iwxapi = WXAPIFactory.createWXAPI(KAPPApplication.getContext(), WXPayManager.APP_ID, false);
        registerAPP();
    }

    /** 获取控制器 */
    public IWXAPI getIwxapi(){
        return iwxapi;
    }

    /** 注册进入微信 */
    public void registerAPP(){
        iwxapi.registerApp(WXPayManager.APP_ID);
    }

    /** 注销进入微信 */
    public void unregisterApp(){
        iwxapi.unregisterApp();
    }

    /** 调起支付 */
    public void payment(WXPayInfo info){
        String entity = getProductArgs(info);
        new WXAsyncTask(info.getPartnerKey()).execute(entity);
    }

    private class WXAsyncTask extends AsyncTask<String, Integer, byte[]>{

        private String key;

        public WXAsyncTask(String key){
            this.key = key;
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String url="https://api.mch.weixin.qq.com/pay/unifiedorder"; //这个地址就是微信支付文档中请求的地址
            byte[] buf = Util.httpPost(url, params[0]);
            return buf;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);

            if (bytes != null) {
                Map<String,String> xml=new HashMap<>();
                String content = new String(bytes);
                logger.test_i("payment date : ", content);
                try {
                    Document doc = DocumentHelper.parseText(content);
                    Element root = doc.getRootElement();
                    List<Element> list = root.elements();
                    for (Element e : list) {
                        xml.put(e.getName(), e.getText());
                    }
                } catch (Exception e) {

                }
                if (xml != null) {

                    PayReq req = new PayReq();
                    req.appId = xml.get("appid");
                    req.partnerId = xml.get("mch_id");
                    req.prepayId = xml.get("prepay_id");
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = xml.get("nonce_str");
                    req.timeStamp = String.valueOf(genTimeStamp());

                    List<NameValuePair> signParams = new LinkedList<>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = genAppSign(signParams, key);
                    iwxapi.sendReq(req);
                } else {
                    logger.test_i("payment date : ", "The wxpay content is Null");
                }
            }
        }
    }

//        PayReq req = new PayReq();
//        req.appId			=  APP_ID;
//        req.partnerId		= info.getPartnerid();
//        req.prepayId		= info.getPrepayid();
//        req.nonceStr		= info.getNoncestr();
//        req.timeStamp		= info.getTimestamp();
//        req.packageValue	= "Sign=WXPay";
//        req.sign			= info.getSign();
////        req.extData			= "app data"; // optional
//        Log.i("Payment WX", " ------------------------------------------------------------- ");
//        Log.i("Payment WX", " appId : "+req.appId);
//        Log.i("Payment WX", " partnerId : "+req.partnerId);
//        Log.i("Payment WX", " prepayId : "+req.prepayId);
//        Log.i("Payment WX", " nonceStr : "+req.nonceStr);
//        Log.i("Payment WX", " timeStamp : "+req.timeStamp);
//        Log.i("Payment WX", " packageValue : "+req.packageValue);
//        Log.i("Payment WX", " sign : "+req.sign);
//        Log.i("Payment WX", " ------------------------------------------------------------- ");
//        iwxapi.sendReq(req);
//    }

    /**
     *
     * @Title: genProductArgs
     * @Description: TODO(拼接参数)
     * @return 返回类型  String
     * @throws
     */
    private String getProductArgs(WXPayInfo info) {
        StringBuffer xml = new StringBuffer();
        try {
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<>();
            packageParams.add(new BasicNameValuePair("appid", APP_ID));
            packageParams.add(new BasicNameValuePair("body", info.getBody()));//商品描述，商品或支付单简要描述，必填
            packageParams.add(new BasicNameValuePair("mch_id", info.getPartnerid()));   //商户ID
            packageParams.add(new BasicNameValuePair("nonce_str", genNonceStr()));//随机字符串，不长于32位。必填
            packageParams.add(new BasicNameValuePair("notify_url", info.getNotifyUrl()));//接收微信支付异步通知回调地址.必填
            packageParams.add(new BasicNameValuePair("out_trade_no", info.getOrderId()));//商户系统内部的订单号,32个字符内、可包含字母,必填
            packageParams.add(new BasicNameValuePair("spbill_create_ip", getLocalIpAddress(KAPPApplication.getContext())));//APP和网页支付提交用户端ip.必填
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(info.getPrice())));//订单总金额，只能为整数.必填
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));//取值如下：JSAPI，NATIVE，APP，WAP,必填
            String sign = genPackageSign(packageParams, info.getPartnerKey());
            packageParams.add(new BasicNameValuePair("sign",sign));  //签名
            String xmlstring =toXml(packageParams);
            xmlstring = new String(xmlstring.getBytes("UTF-8"), "ISO-8859-1");
            return xmlstring;
        } catch (Exception e) {
            Log.e("e", "genProductArgs fail, 异常: " + e.getMessage());
            return null;
        }
    }
    /**
     *
     * @Title: toXml
     * @Description: TODO(转换成String格式的xml)
     * @return 返回类型  String
     * @throws
     */
    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");
            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     *
     * @Title: int2ip
     * @Description: TODO(将ip的整数形式转换成ip形式 )
     * @return 返回类型  String
     * @throws
     */
    private String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     *
     * @Title: getLocalIpAddress
     * @Description: TODO(获取当前ip地址 )
     * @return 返回类型  String
     * @throws
     */
    private String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
        // return null;
    }

    /**
     *
     * @Title: genTimeStamp
     * @Description: TODO(生成时间戳)
     * @return 返回类型  long
     * @throws
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     *
     * @Title: genAppSign
     * @Description: TODO(生成签名)
     * @return 返回类型  String
     * @throws
     */
    private String genAppSign(List<NameValuePair> params, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=").append(key);

        String appSign = WXMD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }


    /**
     *
     * @Title: genPackageSign
     * @Description: TODO(生成签名)
     * @return 返回类型  String
     * @throws
     */
    private String genPackageSign(List<NameValuePair> params, String key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=").append(key);
        logger.i(sb.toString());
        String packageSign = WXMD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        logger.i("sign : "+packageSign);
        return packageSign;
    }

    /**
     *
     * @Title: genNonceStr
     * @Description: TODO(随机字符串)
     * @return 返回类型  String
     */
    public String genNonceStr() {
        Random random = new Random();
        return WXMD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
}
