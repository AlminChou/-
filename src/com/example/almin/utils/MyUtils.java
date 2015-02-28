package com.example.almin.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyUtils {
	protected static final String TAG = "Utils";
	public static final boolean DEBUG = false;

	/**
	 * @Title: isEmail
	 * @param @param email
	 * @param @return
	 * @return boolean
	 * @throws
	 */

	public static Bitmap decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		float realWidth = opts.outWidth;
		float realHeight = opts.outHeight;

		int scale = (int) ((realWidth > realHeight) ? realWidth : realHeight) / 100;
		if (scale <= 0) {
			scale = 1;
		}
		opts.inSampleSize = scale;
		opts.inJustDecodeBounds = false;
		try {
			bitmap = BitmapFactory.decodeFile(path, opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null || listView == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem != null) {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static int getListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null || listView == null) {
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem != null) {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

		}
		return totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	}

	public static String[] expandStrings(String[] arr, String str) {
		if (arr == null) {
			arr = new String[1];
		}
		int size = arr.length;
		String[] temp = new String[size + 1];
		System.arraycopy(arr, 0, temp, 0, size);
		temp[size] = str;
		return temp;
	}

	public static String[] removeString(String[] arr, String str) {
		ArrayList<String> list = new ArrayList<String>();
		for (String string : arr) {
			if (str.equals(str)) {
				continue;
			}
			list.add(string);
		}
		String[] temp = list.toArray(new String[list.size()]);
		return temp;
	}

	public static void setInputMethodHide(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && activity != null
				&& activity.getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	public static void setInputMethodHide(Dialog dialog) {
		InputMethodManager imm = (InputMethodManager) dialog.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			if (imm.isActive()) {
				try {

					imm.hideSoftInputFromWindow(dialog.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File saveBitmapAsFile(Bitmap bitmap, String path, String name) {
		if (bitmap == null) {
			return null;
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(path, name + ".jpg");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static boolean isEmailValidate(String email) {
		String strPattern = "^[a-zA-Z0-9+][+\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(email);

		return m.matches();
	}
}
