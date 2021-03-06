package com.example.almin.webservice;

import com.example.almin.MyConfiguration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AssetsRestClient {
	private static final String ASSETS_SERVICE_BASE_API = MyConfiguration.API_BASE+"Asset.";
	public static final String ACTION_GET_USER_ASSETS = "getUserAssetsInfo.do";
	public static final String ACTION_GET_ASSETS_WITH_STATE_IN_CATEGORY = "getAssetsInfoWithStateInCategory.do";
	public static final String ACTION_GET_ASSETS_WITH_STATE_AND_NAME_IN_CATEGORY = "getAssetsInfoWithStateAndNameInCategory.do";

	private static AsyncHttpClient client = new AsyncHttpClient();
																
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return ASSETS_SERVICE_BASE_API + relativeUrl;
	}
}
