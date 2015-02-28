package com.example.almin;


public class AndroidNetworkConnectivityManager {
	public boolean isNetworkAvailable() {
		return MyApplication.getInstance().isNetworkAvailable();
	}
}
