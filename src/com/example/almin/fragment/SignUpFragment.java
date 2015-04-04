package com.example.almin.fragment;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.webservice.UsersRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SignUpFragment extends Fragment {
	private View mRootView;
	private EditText mEtEmail,mEtPassword,mEtName,mEtPosition;
	private ImageView mIvSignUp;
	private Spinner mSpDepartment,mSpSex;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.sign_up, null);
		mRootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		initView();
		return mRootView;
	}

	private void initView() {
		mEtEmail = (EditText) mRootView.findViewById(R.id.et_email);
		mEtPassword = (EditText) mRootView.findViewById(R.id.et_password);
		mEtName = (EditText) mRootView.findViewById(R.id.et_name);
		mEtPosition = (EditText) mRootView.findViewById(R.id.et_position);
		mSpDepartment = (Spinner) mRootView.findViewById(R.id.sp_department);
		mSpSex = (Spinner) mRootView.findViewById(R.id.sp_sex);
		mIvSignUp = (ImageView) mRootView.findViewById(R.id.iv_join_now);
		mIvSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MyApplication.isValidEmail(mEtEmail.getText().toString().trim()) && !MyApplication.isTextEmptyOrNull(mEtPassword.getText().toString().trim())){
					if(MyApplication.getInstance().isNetworkAvailable()){
						RequestParams params = new RequestParams();
						params.put("username", mEtEmail.getText().toString().trim());
						params.put("password", mEtPassword.getText().toString().trim());
						params.put("name", mEtName.getText().toString().trim());
						params.put("department", mSpDepartment.getSelectedItemPosition());
						params.put("sex", mSpSex.getSelectedItemPosition());
						params.put("position", mEtPosition.getText().toString().trim());
						UsersRestClient.post(UsersRestClient.ACTION_REGISTER, params , mSignHandler );
					}else{
						Toast.makeText(getActivity(), "当前网络不可用，请检查！", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getActivity(), "邮箱格式不正确！", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private AsyncHttpResponseHandler mSignHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] arg1, byte[] result) {
			if(MyApplication.isDoneCallBack(new String(result))){
				Toast.makeText(getActivity(), "注册成功，请继续登录。", Toast.LENGTH_LONG).show();
				getActivity().onBackPressed();
			}else{
				Toast.makeText(getActivity(), "注册失败，邮箱已存在，请重试！", Toast.LENGTH_LONG).show();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] result, Throwable arg3) {
			Toast.makeText(getActivity(), "注册失败，请重试！", Toast.LENGTH_LONG).show();
		}
	};
}
