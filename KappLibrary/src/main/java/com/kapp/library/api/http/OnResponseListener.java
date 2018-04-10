package com.kapp.library.api.http;

public interface OnResponseListener {

	void onStart(BaseResponse response);
	
	void onFailure(BaseResponse response);
	
	void onSuccess(BaseResponse response);
}
