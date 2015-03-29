package com.example.almin.webservice;

import com.example.almin.MyConfiguration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ProcessRestClient {
	private static final String PROCESS_SERVICE_BASE_API = MyConfiguration.API_BASE+"Process.";
	public static final String ACTION_ADD_PROCESS = "addProcess.do";

	private static AsyncHttpClient client = new AsyncHttpClient();
																
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return PROCESS_SERVICE_BASE_API + relativeUrl;
	}
}
