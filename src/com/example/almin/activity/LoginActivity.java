package com.example.almin.activity;

import java.io.File;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.library.model.User;
import com.example.almin.webservice.UsersRestClient;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	private Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
//		initAppTempDir();
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RequestParams params = new RequestParams();
				params.put("username", "461604533");
				params.put("password", "123456");
				UsersRestClient.post(UsersRestClient.ACTION_CHECK_USER, params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						User user =new Gson().fromJson(new String(arg2),User.class);
						System.out.println(user!=null);
						if(user!=null){
							Intent intent = new Intent(LoginActivity.this,MainActivity.class);
							MyApplication.getInstance().setUser(user);
							startActivity(intent);
							finish();
						}
						System.out.println(arg0);
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						
					}
				});
			}
		});
		
	}

//	private void initAppTempDir() {
//		String sDir1;
//		int i=0;//�Ƿ��鵽����SD��
//		//�ж�SD���Ƿ����
//		String status = Environment.getExternalStorageState();
//		if (status.equals(Environment.MEDIA_MOUNTED)) {
//			i=1;
//		} else {
//			i=0;
//		}
//		//Ȼ������Ƿ����״ָ̬��Ŀ¼
//		if (i==1) {
//			File parent = new File("/sdcard/AssetsService/"); //�������ļ���
//			if (!parent.exists()) {
//				parent.mkdirs();
//			}
//			sDir1 = "/sdcard/AssetsService/AvatarTemp/";
//		} else {
//			File parent = new File("/AssetsService/"); //�������ļ���
//			if (!parent.exists()) {
//				parent.mkdirs();
//			}
//			sDir1 = "/AssetsService/AvatarTemp/";
//		}
//		//Ȼ���Ǵ����ļ���
//
//		File parent1 = new File(sDir1); 
//		if (!parent1.exists()) {
//			parent1.mkdirs();
//		}
//	}
}
