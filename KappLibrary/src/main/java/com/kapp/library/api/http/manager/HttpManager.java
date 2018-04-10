package com.kapp.library.api.http.manager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kapp.library.KAPPApplication;
import com.kapp.library.R;
import com.kapp.library.api.http.toolbox.ExtHttpClientStack;
import com.kapp.library.api.http.toolbox.SslHttpClient;

import java.io.InputStream;

public class HttpManager {

	private static HttpManager instance = new HttpManager();
	
	public static HttpManager getInstance(){
		return instance;
	}
	
	private RequestQueue mQueue = null;
	
	private HttpManager(){
		bindQueue();
	}
	
	private void bindQueue(){
		mQueue = Volley.newRequestQueue(KAPPApplication.getContext());

		//http://blog.csdn.net/arjinmc/article/details/47108061
//		InputStream keyStore = KAPPApplication.getContext().getResources().openRawResource(R.raw.ssl);
//		mQueue = Volley.newRequestQueue(KAPPApplication.getContext(),
//				new ExtHttpClientStack(new SslHttpClient(keyStore, "123456", 8076)));
	}
	
	public <T> void execute(Request<T> request){
		mQueue.getCache().clear();
		mQueue.add(request);
	}
	
	public void removeTagRequest(Object tag){
		mQueue.cancelAll(tag);
	}
	
}
