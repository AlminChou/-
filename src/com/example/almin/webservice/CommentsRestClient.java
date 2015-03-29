package com.example.almin.webservice;

import com.example.almin.MyConfiguration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CommentsRestClient {
	private static final String COMMENT_SERVICE_BASE_API = MyConfiguration.API_BASE+"NeedsComment.";
	public static final String ACTION_LEAVE_COMMENT = "leaveComment.do";

	private static AsyncHttpClient client = new AsyncHttpClient();
																
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return COMMENT_SERVICE_BASE_API + relativeUrl;
	}
}
