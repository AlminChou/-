package com.example.almin.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyAlertDialog {
	public MyAlertDialog(Context context,String dialogTitle,String positive,String negative,DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(dialogTitle);
		builder.setNegativeButton(negative, negativeListener);
		builder.setPositiveButton(positive, positiveListener);
		builder.create().show();
	}
}
