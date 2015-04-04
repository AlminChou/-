package com.example.almin.activity;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.fragment.SignUpFragment;
import com.example.almin.library.model.User;
import com.example.almin.webservice.UsersRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends FragmentActivity{
	private Button mBtnLogin,mBtnSignUp;
	private EditText mEtEmail,mEtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		MyApplication.initAppTempDir();
		initView();
	}

	private void initView(){
		mEtEmail = (EditText) findViewById(R.id.et_email);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MyApplication.isValidEmail(mEtEmail.getText().toString().trim()) && !MyApplication.isTextEmptyOrNull(mEtPassword.getText().toString().trim())){
					if(MyApplication.getInstance().isNetworkAvailable()){
						RequestParams params = new RequestParams();
						params.put("username", mEtEmail.getText().toString().trim());
						params.put("password", mEtPassword.getText().toString().trim());
						UsersRestClient.post(UsersRestClient.ACTION_CHECK_USER, params , mLoginHandler );
					}else{
						Toast.makeText(getApplicationContext(), "当前网络不可用，请检查！", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "邮箱格式不正确！", Toast.LENGTH_LONG).show();
				}
			}
		});
		mBtnSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				navigateToSignUpFragment();
			}
		});
	}
	
	private AsyncHttpResponseHandler mLoginHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] arg1, byte[] result) {
				User user = User.getUserFromJson(result);
				if(user!=null){
					Toast.makeText(getApplicationContext(), "登录成功，请继续登录。", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					MyApplication.getInstance().setUser(user);
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "登录失败，用户信息不正确或者用户不存在存在，请重试！", Toast.LENGTH_LONG).show();
				}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] result, Throwable arg3) {
			Toast.makeText(getApplicationContext(), "登录失败，请重试！", Toast.LENGTH_LONG).show();
		}
	};
	
	private void navigateToSignUpFragment() {
		addFragmentAndAdd2BackStack(new SignUpFragment());
	}
	
	private void addFragmentAndAdd2BackStack(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
	}
}
