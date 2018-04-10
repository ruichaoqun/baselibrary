package com.kapp.library.api.http;

import android.text.TextUtils;

public class BaseResponse {
	
	public static final int SUCCEED=1;

	private int status;
	private int requestType;
	private boolean showProgress;
	private int error_code;
	private String error_msg;
	private String responseJson;
	private Object data;
	private Object exData;
	private boolean loadMore;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		if (TextUtils.isEmpty(error_msg))
			this.error_msg = "网络错误！!";
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public boolean isShowProgress() {
		return showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public boolean isLoadMore() {
		return loadMore;
	}

	public void setLoadMore(boolean loadMore) {
		this.loadMore = loadMore;
	}

	public Object getExData() {
		return exData;
	}

	public void setExData(Object exData) {
		this.exData = exData;
	}

}
