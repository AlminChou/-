package com.example.almin;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.almin.library.model.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
	public static final String MAIN_DIR_PATH = "/AssetsService/";
	public static final String AVATAR_DIR_PATH = "/AssetsService/AvatarTemp/";
	public static final String AVATAR_PATH = "/AssetsService/AvatarTemp/myavatar.jpg";
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
		initImageLoader(getApplicationContext());
		loadUserInfo();
		//image-loader初始化
		// 其他初始化
		// 字体资源初始化
	}

	private void loadUserInfo() {//获取网络的用户的信息
		
	}

	@SuppressWarnings("deprecation")
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs()
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		ImageLoader.getInstance().init(config);
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
		cleanCache();
	}

	private void cleanCache() {
		
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
	
	@SuppressLint("SdCardPath")
	public static void initAppTempDir() {
		String sDir;
		//判断SD卡是否插入
		String status = Environment.getExternalStorageState();
		boolean isHaveSDcard=status.equals(Environment.MEDIA_MOUNTED);
		if (isHaveSDcard) {
			//然后根据是否插入状态指定目录
			File parent = new File("/sdcard/AssetsService/"); //创建总文件夹
			if (!parent.exists()) {
				parent.mkdirs();
				System.out.println("parent 文件夹创建完成"+isHaveSDcard);
			}
			sDir = "/sdcard/AssetsService/AvatarTemp/";
		} else {
			//然后根据是否插入状态指定目录
			File parent = new File("/AssetsService/"); //创建总文件夹
			if (!parent.exists()) {
				parent.mkdirs();
				System.out.println("parent 文件夹创建完成"+isHaveSDcard);
			}
			sDir = "/AssetsService/AvatarTemp/";
		}
		//然后是创建Avatar文件夹

		File avatarDir = new File(sDir); 
		if (!avatarDir.exists()) {
			avatarDir.mkdirs();
			System.out.println("avatar 文件夹创建完成");
		}
	}
	
	public static String saveImage(String directory, String filename,
			Bitmap source, byte[] jpegData, int quality,boolean isUpdate) {
		OutputStream outputStream = null;
		String filePath = directory + filename;
		try {
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(directory, filename);
			if(isUpdate){
				file.delete();
			}
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);
				if (source != null) {
					source.compress(CompressFormat.JPEG, quality, outputStream);
				} else {
					outputStream.write(jpegData);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			}
		}
		return filePath;
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
	
	public static boolean isTextNoResultForJson(String text) {
		return "[]".equalsIgnoreCase(text);
	}
	
	public static boolean isDoneCallBack(String text) {
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
	
	public static boolean isHaveSDcard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}
}
