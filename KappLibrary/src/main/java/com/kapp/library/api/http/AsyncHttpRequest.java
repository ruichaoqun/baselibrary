package com.kapp.library.api.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kapp.library.api.http.manager.HttpManager;
import com.kapp.library.api.http.manager.SsX509TrustManager;
import com.kapp.library.api.http.manager.StringPost;
import com.kapp.library.utils.Logger;
import com.kapp.library.utils.MD5;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AsyncHttpRequest implements Response.Listener<String>,
		Response.ErrorListener {

	protected Logger logger = new Logger(this.getClass().getSimpleName());

	private int requestType;
	protected boolean showProgress = false;
	private OnResponseListener listener;
	private StringRequest stringRequest = null;

	protected final int maxNumRetries = 3;// 最大重试次数
	protected final int timeoutMS = 5000;// 请求超时 单位：毫秒
	private boolean loadMore = false;//加载更多
	protected boolean isRunning = false;//运行状态
	private final String md5Str = "[/#$,l[d65apds3jf@{r56)*>!@#]";

	public abstract String getRequestUrl();
	public abstract boolean getUnifiedAnalyticalStandard();
	public abstract void parseResponse(BaseResponse response, JSONObject json)
			throws JSONException;
	public abstract void setRequestPostValue(List<NameValuePair> nvps);

	public void executeGet() {
		executeGet(false);
	}

	public void executeGet(boolean showProgress) {
		if (this.isRunning)
			return;
		this.isRunning = true;
		this.showProgress = showProgress;
		String url = addUrlSessionId();
		logger.w(url);

		this.sendStartMessage();
		SsX509TrustManager.allowAllSSL();
		stringRequest = new StringRequest(Method.GET, url, this, this);// 注：如果需要加入版本或者其他后缀，请在toVersion添加，替换url为toVersion(url)
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, maxNumRetries, 1.0f));
		stringRequest.setShouldCache(false);
		HttpManager.getInstance().execute(stringRequest);
	}

	public void executePost() {
		executePost(true);
	}

	public void executePost(boolean showProgress) {
		if (this.isRunning)
			return;
		this.isRunning = true;
		this.showProgress = showProgress;
		String url = addUrlSessionId();
		logger.w(url);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		AsyncHttpRequest.this.setRequestPostValue(nvps);
		logger.w(" NVPS params : "+nvps.size());

		Map<String, String> params = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		for (NameValuePair param : nvps) {
			sb.append("&").append(param.getName()).append("=").append(param.getValue());
			logger.w(" getName : " + param.getName() + " ** getValue : " + param.getValue());
			params.put(param.getName(), param.getValue());
		}
		logger.w(" NVPS params all : " + sb.toString());

		this.sendStartMessage();
		SsX509TrustManager.allowAllSSL();
		stringRequest = new StringPost(Method.POST, url, params, this, this);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, maxNumRetries, 1.0f));
		stringRequest.setShouldCache(false);
		HttpManager.getInstance().execute(stringRequest);
	}

	public void executeDelete() {
		executeDelete(true);
	}

	public void executeDelete(boolean showProgress) {
		if (this.isRunning)
			return;
		this.isRunning = true;
		this.showProgress = showProgress;
		String url = addUrlSessionId();
		logger.w(url);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		AsyncHttpRequest.this.setRequestPostValue(nvps);
		logger.w(" NVPS params : "+nvps.size());

		Map<String, String> params = new HashMap<String, String>();
		for (NameValuePair param : nvps) {
			logger.w(" getName : "+param.getName()+" ** getValue : "+param.getValue());
			params.put(param.getName(), param.getValue());
		}

		this.sendStartMessage();
		SsX509TrustManager.allowAllSSL();
		stringRequest = new StringPost(Method.DELETE, url, params, this, this);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, maxNumRetries, 1.0f));
		stringRequest.setShouldCache(false);
		HttpManager.getInstance().execute(stringRequest);
	}

	public void executePut() {
		executePut(true);
	}

	public void executePut(boolean showProgress) {
		if (this.isRunning)
			return;
		this.isRunning = true;
		this.showProgress = showProgress;
		String url = addUrlSessionId();
		logger.w(url);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		AsyncHttpRequest.this.setRequestPostValue(nvps);
		logger.w(" NVPS params : "+nvps.size());

		Map<String, String> params = new HashMap<String, String>();
		for (NameValuePair param : nvps) {
			logger.w(" getName : "+param.getName()+" ** getValue : "+param.getValue());
			params.put(param.getName(), param.getValue());
		}

		this.sendStartMessage();
		SsX509TrustManager.allowAllSSL();
		stringRequest = new StringPost(Method.PUT, url, params, this, this);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeoutMS, maxNumRetries, 1.0f));
		stringRequest.setShouldCache(false);
		HttpManager.getInstance().execute(stringRequest);
	}

	protected String addUrlSessionId(){
		String url = AsyncHttpRequest.this.getRequestUrl();
		String type;
		if (url.indexOf("?") > 0){
			type = "&";
		}else{
			type = "?";
		}

//		Date date = new Date();
		String time = String.valueOf(System.currentTimeMillis());
//		String time = String.valueOf(date.getTime());
		StringBuffer sb = new StringBuffer();
		sb.append(AsyncHttpRequest.this.getRequestUrl()).append(type);
		sb.append("token=").append(MD5.Str2MD5(time+md5Str)).append("&");
		sb.append("key=").append(time);
//		sb.append(StoreWession.getInstances().getKeyName()).append("=");
//		sb.append(StoreWession.getInstances().getKeyValue());
		return sb.toString();
	}

	public void cancel() {
		this.isRunning = false;
		if (stringRequest != null)
			stringRequest.cancel();
	}
	
	//移除Tag标记的请求
	public void cancelRequest(Object tag){
		this.isRunning = false;
		HttpManager.getInstance().removeTagRequest(tag);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		logger.w("AsyncHttpRequest Error : " + error.getMessage());
		this.isRunning = false;
		sendFailureMessage();
	}

	@Override
	public void onResponse(String response) {
		this.isRunning = false;
		sendSuccessMessage(response);
	}

	// 启动请求
	public void sendStartMessage() {
		BaseResponse response = new BaseResponse();
		response.setRequestType(this.requestType);
		response.setShowProgress(this.showProgress);
		response.setLoadMore(this.loadMore);
		if (listener != null)
			listener.onStart(response);
	}

	// 请求失败
	public void sendFailureMessage() {
		BaseResponse response = new BaseResponse();
		response.setRequestType(this.requestType);
		response.setShowProgress(this.showProgress);
		response.setLoadMore(this.loadMore);
		logger.e("DUBUG：RequestError");
		if (listener != null)
			listener.onFailure(response);
	}

	// 请求成功
	public void sendSuccessMessage(String json) {
		BaseResponse response = new BaseResponse();
		response.setRequestType(this.requestType);
		response.setShowProgress(this.showProgress);
		response.setLoadMore(this.loadMore);
		logger.i("Request Finished !!");
		try {
			logger.i("Response : " + json);
			JSONTokener jsonParser = new JSONTokener(json);
			JSONObject jsonObj = (JSONObject) jsonParser.nextValue();

			if (AsyncHttpRequest.this.getUnifiedAnalyticalStandard()) {
				
				int indexStatus = jsonObj.optInt("status", -1);
				String message = jsonObj.optString("msg", "");

				logger.i("indexStaus : "+indexStatus+" ** message : "+message);

				if(indexStatus == 10001){
					response.setStatus(1);
				}else{
					response.setStatus(0);
					response.setError_code(indexStatus);
					response.setError_msg(message);
					if (listener != null)
						listener.onFailure(response);
					return;
				}
			}

			response.setResponseJson(json);

			this.parseResponse(response, jsonObj.optJSONObject("dataInfo"));

			if (listener != null)
				listener.onSuccess(response);
		} catch (Exception e) {
			logger.w(e);
			if (listener != null)
				listener.onFailure(response);
		}
	}

	public OnResponseListener getOnResponseListener() {
		return listener;
	}

	public void setOnResponseListener(OnResponseListener listener) {
		this.listener = listener;
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	
	public void setLoadMore(boolean loadMore){
		this.loadMore = loadMore;
	}

	public boolean isShowProgress() {
		return showProgress;
	}

//	public abstract String getAPIVersion();
	// 加入版本信息
//	 public String toVersion(String url) {
//
//		 Uri.Builder builder = Uri.parse(url).buildUpon();
//		 builder.appendQueryParameter("api_version", getAPIVersion());
//		 builder.appendQueryParameter("market_id", STApplication.getMarket_id());
//		 builder.appendQueryParameter("Token", STWession.getInstance().getKey());
////		 builder.appendQueryParameter("", value)
//
//		 String result = builder.toString();
//		 return result;
//	 }
}
