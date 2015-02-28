package com.example.almin.webservice;

import com.example.almin.MyConfiguration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UsersRestClient {
	private static final String USERS_SERVICE_BASE_API = MyConfiguration.API_BASE+"User.";
	public static final String ACTION_CHECK_USER = "checkUser.do";
	public static final String ACTION_UPDATE_USER_INFO = "updateMySettings.do";
	public static final String ACTION_UPDATE_PASSWORD = "updatePassword.do";

	private static AsyncHttpClient client = new AsyncHttpClient();
																
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return USERS_SERVICE_BASE_API + relativeUrl;
	}
}
