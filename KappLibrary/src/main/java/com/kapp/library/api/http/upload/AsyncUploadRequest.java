package com.kapp.library.api.http.upload;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.kapp.library.api.http.AsyncHttpRequest;
import com.kapp.library.api.http.manager.HttpManager;
import com.kapp.library.api.http.manager.SsX509TrustManager;

import org.apache.http.NameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/12 0012.
 * 上传基类
 */
public abstract class AsyncUploadRequest extends AsyncHttpRequest{

    public abstract void setRequestFileUploads(Map<String, File> fileUploads);

    @Override
    public void executePost() {
        executePost(false);
    }

    @Override
    public void executePost(boolean showProgress) {
        if (this.isRunning)
            return;
        this.isRunning = true;
        this.showProgress = showProgress;
        String url = super.addUrlSessionId();
        logger.w(url);

        List<NameValuePair> nvps = new ArrayList<>();
        AsyncUploadRequest.this.setRequestPostValue(nvps);
        logger.w(" NVPS params : "+nvps.size());

        Map<String, String> params = new HashMap<String, String>();
        for (NameValuePair param : nvps) {
            logger.w(" getName : "+param.getName()+" ** getValue : "+param.getValue());
            params.put(param.getName(), param.getValue());
        }

        Map<String, File> fileUploads = new HashMap<>();
        AsyncUploadRequest.this.setRequestFileUploads(fileUploads);

        this.sendStartMessage();
        SsX509TrustManager.allowAllSSL();
        MultipartRequest multipartRequest = new MultipartRequest(Request.Method.POST, url, params, fileUploads, this, this);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, maxNumRetries, 1.0f));
        multipartRequest.setShouldCache(false);
        HttpManager.getInstance().execute(multipartRequest);
    }

}
