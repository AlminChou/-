package com.example.almin;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.almin.library.model.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MyApplication extends Application {
	private static MyApplication instance;
	private User user;
	private Bitmap mBmpAvatar;
	private int mDisplayWidth;
	private int mDisplayHeight;
	private AndroidNetworkConnectivityManager mAndroidNetworkConnectivityManager;
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat sDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
//	private static final DateFormat sEnglishDateFormat = new SimpleDateFormat(
//			"MMMM yyyy", Locale.ENGLISH);
	
	
	@Override
	public void onCreate() {
		Log.e("oncreat", "MyApplication----onCreate");
		super.onCreate();
		if (instance == null) {
			instance = this;
		}
		MyConfiguration.init();
		loadUserInfo();
		//image-loader初始化
		// 其他初始化
		// 字体资源初始化
	}

	private void loadUserInfo() {//获取网络的用户的信息
		
	}

	public static MyApplication getInstance() {
		return instance;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Bitmap getmBmpAvatar() {
		return mBmpAvatar;
	}

	public void setmBmpAvatar(Bitmap mBmpAvatar) {
		this.mBmpAvatar = mBmpAvatar;
	}

	public AndroidNetworkConnectivityManager getAndroidNetworkConnectivityManager() {
		if (mAndroidNetworkConnectivityManager == null) {
			mAndroidNetworkConnectivityManager = new AndroidNetworkConnectivityManager();
		}
		return mAndroidNetworkConnectivityManager;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		//清理缓存工作  关闭其他东西
	}

	public static boolean isValidEmail(String email) {
		String strPattern = "^[a-zA-Z0-9+][+\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	
	public static void showInputMethod(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void hideInputMethod(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			View view = activity.getCurrentFocus();
			IBinder binder = view.getWindowToken();
			imm.hideSoftInputFromWindow(binder,
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}
	
	public boolean isNetworkAvailable() {
		try {
			ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cManager != null) {
				NetworkInfo[] infos = cManager.getAllNetworkInfo();
				if (infos != null) {
					for (int i = 0; i < infos.length; i++) {
						if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isTextEmptyOrNull(String text) {
		return TextUtils.isEmpty(text) || "null".equalsIgnoreCase(text);
	}
	
	public static boolean isTextDoneOrFaile(String text) {
		return "done".equalsIgnoreCase(text);
	}
	
	public int getDisplayWidth() {
		return mDisplayWidth;
	}

	public void setDisplayWidth(int displayWidth) {
		this.mDisplayWidth = displayWidth;
	}

	public int getDisplayHeight() {
		return mDisplayHeight;
	}

	public void setDisplayHeight(int displayHeight) {
		this.mDisplayHeight = displayHeight;
	}
	
	public static String formatDate(Date date) {
		return sDateFormat.format(date);
	}
	
	public static String getNowDateTime(){
		return sDateFormat.format(new Date());
	}
}
