package com.kapp.library.broadnotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.kapp.library.KAPPApplication;
import com.kapp.library.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9 0009.
 * 广播全局类
 */
public class BroadNotifyUtils {

    private static Logger logger = new Logger("ReceiverUtils");
    private final static String ACTION_TYPE_NAME = "ReceiverTypeName";
    private final static String ACTION_BUNDLE = "bundle";

    public static final String ACTION_BROAD_NOTIFY = "com.sdkpzb.app.BroadNotifyReceiver";

    public static final int NOTIFY_ALIPAY = 1000;//支付宝支付
    public static final int NOTIFY_UNIONPAY = 1001;//银联支付
    public static final int NOTIFY_WXPAY = 1002;//微信支付
    public static final int NOTIFY_NETWORK_MONITOR = 1003;//视频播放网络监听

    public static final int ACTIVITY_RESUME = 33567;//activity resume用于控制悬浮窗显示
    public static final int ACTIVITY_STOP = 33566;//activity stop用于控制悬浮窗隐藏

    public static final int ON_NEW_ITEM_FOUND = 44501;//重新检索手机视频
    public static final int ON_FILE_SCAN_FINISH = 44502;//重新检索手机视频

    static class MessageObserverReceiver extends BroadcastReceiver{

        public MessageObserverReceiver(){
            KAPPApplication.getContext().registerReceiver(this, new IntentFilter(ACTION_BROAD_NOTIFY));
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int receiverType = intent.getIntExtra(ACTION_TYPE_NAME, 0);
            Bundle bundle = intent.getBundleExtra(ACTION_BUNDLE);
            for(MessageReceiver receiver : receiverList){
                receiver.onMessage(receiverType, bundle);
            }
        }
    }

    private static MessageObserverReceiver broadCast = new MessageObserverReceiver();

    /** 发送广播 */
    public static void sendReceiver(int receiverType,Bundle bundle){
        if(!receiverList.isEmpty()){
            logger.i("SendReceiver : "+String.valueOf(receiverType));
            Intent intent = new Intent(ACTION_BROAD_NOTIFY);
            intent.putExtra(ACTION_TYPE_NAME, receiverType);
            intent.putExtra(ACTION_BUNDLE, bundle);
            KAPPApplication.getContext().sendBroadcast(intent);
        }
    }

    /** 添加广播监听器 */
    public static void addReceiver(MessageReceiver receiver){
        receiverList.add(receiver);
        logger.i(" --- addReceiver --- ");
    }

    /** 移除广播监听器 */
    public static void removeReceiver(MessageReceiver receiver){
        receiverList.remove(receiver);
        logger.i(" --- removeReceiver --- ");
    }

    public interface MessageReceiver{

        void onMessage(int receiverType,Bundle bundle);
    }

    private static List<MessageReceiver> receiverList;

    static{
        receiverList = Collections.synchronizedList(new ArrayList<MessageReceiver>());
    }

}
