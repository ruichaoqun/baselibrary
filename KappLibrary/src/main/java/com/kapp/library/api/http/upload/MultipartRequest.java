package com.kapp.library.api.http.upload;

import com.android.volley.Request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kapp.library.utils.Logger;

/**
 * Created by Administrator on 2017/9/12 0012.
 * 文件上传
 */

public class MultipartRequest extends Request<String> {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    private HttpEntity httpEntity;

    private final Response.Listener<String> mListener;

    public MultipartRequest(int method,String url,Map<String, String> params,Map<String, File> uploadFiles, Response.ErrorListener errorListener,
                            Response.Listener<String> listener) {
        super(method, url, errorListener);
        mListener = listener;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            for (Map.Entry<String, File> entry : uploadFiles.entrySet()){
                builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }

            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addPart(entry.getKey(), new StringBody(entry.getValue(), contentType));
            }

            httpEntity = builder.build();
        } catch (Exception e) {
            logger.w(e);
        }
    }

    @Override
    public String getBodyContentType() {
        return httpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpEntity.writeTo(bos);
        } catch (IOException e) {
            logger.w(e);
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        logger.i("parseNetworkResponse");
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

        return headers;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
