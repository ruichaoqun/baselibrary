package com.kapp.library.payment;

import com.kapp.library.payment.alipay.AlipayManager;
import com.kapp.library.payment.unionpay.UnionPayManager;
import com.kapp.library.payment.wxpay.WXPayManager;

/**
 * Created by Administrator on 2016/11/21 0021.
 * 支付API
 */
public class KAPPPaymentAPI {

    public static final int PAYMENT_TYPE_WX = 1;//微信支付
    public static final int PAYMENT_TYPE_ALIPAY = 2;//支付宝支付
    public static final int PAYMENT_TYPE_UNIONPAY = 3;//银联支付

    private static KAPPPaymentAPI paymentAPI = null;
    private WXPayManager wxPayManager = null;
    private AlipayManager alipayManager = null;
    private UnionPayManager unionPayManager = null;

    public static KAPPPaymentAPI getInstances(){
        if (paymentAPI == null)
            paymentAPI = new KAPPPaymentAPI();
        return paymentAPI;
    }

    /** 微信支付 */
    public WXPayManager getWXPay(){
        if (wxPayManager == null)
            wxPayManager = new WXPayManager();
        return wxPayManager;
    }

    /** 支付宝支付 */
    public AlipayManager getAlipay(){
        if (alipayManager == null)
            alipayManager = new AlipayManager();
        return alipayManager;
    }

    /** 银联支付 */
    public UnionPayManager getUnionPay(){
        if (unionPayManager == null)
            unionPayManager = new UnionPayManager();
        return unionPayManager;
    }
}