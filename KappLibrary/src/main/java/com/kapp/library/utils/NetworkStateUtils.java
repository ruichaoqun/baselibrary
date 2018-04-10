package com.kapp.library.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/8 0008.
 *
 * 网络状态监听
 */

public class NetworkStateUtils {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    private static NetworkStateUtils networkStateUtils = null;
    private Map<String, OnNetworkStateListener> mapListener = new HashMap<>();
    private NetworkConnectChangedReceiver connectChangedReceiver = new NetworkConnectChangedReceiver();
    private boolean isState = false;//

    public static NetworkStateUtils getInstances(){
        if (networkStateUtils == null)
            networkStateUtils = new NetworkStateUtils();
        return networkStateUtils;
    }

    /** 监听网络变化 */
    public void registerNetworkState(Context context, String tag, OnNetworkStateListener listener){
        mapListener.put(tag, listener);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(connectChangedReceiver,filter);
    }

    /** 移除监听网络变化 */
    public void unregisterNetworkState(Context context, String tag){
        mapListener.remove(tag);
        context.unregisterReceiver(connectChangedReceiver);
    }

    public interface OnNetworkStateListener{

        /**
         * isConnection：是否连接网络
         * isWifi：是否连接WiFi
         * */
        void onNetworkState(boolean isConnection, boolean isWifi);
    }

    /**
     * 网络改变监控广播
     * <p>
     * 监听网络的改变状态,只有在用户操作网络连接开关(wifi,mobile)的时候接受广播,
     * 然后对相应的界面进行相应的操作，并将 状态 保存在我们的APP里面
     * <p>
     * <p>
     * Created by xujun
     */
    public class NetworkConnectChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
            // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
            // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                logger.i("CONNECTIVITY_ACTION");

                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (!isState && activeNetwork.isConnected()) {
                        isState = true;
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {//WiFi网络
//                            logger.e("当前WiFi连接可用 ");
                            invokMapListeners(true, true);
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {//移动网络
//                            logger.e("当前移动网络连接可用 ");
                            invokMapListeners(true, false);
                        }
                    } else {
//                        logger.e("当前没有网络连接，请确保你已经打开网络 ");
                        invokMapListeners(false, false);
                    }
                } else if (isState){   // not connected to the internet
//                    logger.e("当前没有网络连接，请确保你已经打开网络 ");
                    isState = false;
                    invokMapListeners(false, false);
                }
            }
        }
    }

    private void invokMapListeners(boolean isConnection, boolean isWifi){
        for (Map.Entry<String, OnNetworkStateListener> entry : mapListener.entrySet()){
            if (entry.getValue() != null){
                entry.getValue().onNetworkState(isConnection, isWifi);
            }
        }
    }

}
