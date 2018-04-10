package com.kapp.library.api.http.manager;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class StringPost extends StringRequest {

	private Map<String, String> params;
	
	public StringPost(int method, String url,Map<String, String> params, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		this.params = params;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return params;
	}
	
	
}
