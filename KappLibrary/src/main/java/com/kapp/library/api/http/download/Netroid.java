package com.kapp.library.api.http.download;

import android.os.Build;

import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.stack.HttpClientStack;
import com.duowan.mobile.netroid.stack.HttpStack;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;
import com.duowan.mobile.netroid.toolbox.FileDownloader;

import org.apache.http.protocol.HTTP;

/**
 * Created by Administrator on 2017/9/12 0012.
 * Netroid 基于 Volley扩展的http框架
 * https://github.com/vince-styling/Netroid
 * http://netroid.cn/
 */

public class Netroid {

    public static final String USER_AGENT = "netroid_sample";
    private static Netroid mInstance;
    private FileDownloader mFileDownloader;
    private Network mNetwork;
    private RequestQueue mRequestQueue;

    public Netroid(){

    }

    public static void init(){

        mInstance = new Netroid();

        HttpStack stack;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            stack = new HurlStack(USER_AGENT, null);
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            stack = new HttpClientStack(USER_AGENT);
        }

        mInstance.mNetwork = new BasicNetwork(stack, HTTP.UTF_8);
        int poolSize = RequestQueue.DEFAULT_NETWORK_THREAD_POOL_SIZE;
        mInstance.mRequestQueue = new RequestQueue(mInstance.mNetwork, poolSize, null);
        mInstance.mRequestQueue.start();
    }

    public static RequestQueue getRequestQueue() {
        if (mInstance.mRequestQueue != null) {
            return mInstance.mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void setFileDownloder(FileDownloader downloder) {
        mInstance.mFileDownloader = downloder;
    }

    public static FileDownloader getFileDownloader() {
        if (mInstance.mFileDownloader != null) {
            return mInstance.mFileDownloader;
        } else {
            throw new IllegalStateException("FileDownloader not initialized");
        }
    }

    public static void destroy() {
        if (mInstance.mRequestQueue != null) {
            mInstance.mRequestQueue.stop();
            mInstance.mRequestQueue = null;
        }

        if (mInstance.mFileDownloader != null) {
            mInstance.mFileDownloader.clearAll();
            mInstance.mFileDownloader = null;
        }

        mInstance.mNetwork = null;
    }
}
