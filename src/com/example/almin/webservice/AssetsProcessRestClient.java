package com.example.almin.webservice;

import com.example.almin.MyConfiguration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AssetsProcessRestClient {
	private static final String ASSETPROCESS_SERVICE_BASE_API = MyConfiguration.API_BASE+"AssetAndProcess.";
	public static final String ACTION_ADD_PROCESS_AND_UPDATE_ASSET_STATE = "addProcessAndUpdateState.do";

	private static AsyncHttpClient client = new AsyncHttpClient();
																
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return ASSETPROCESS_SERVICE_BASE_API + relativeUrl;
	}
}
