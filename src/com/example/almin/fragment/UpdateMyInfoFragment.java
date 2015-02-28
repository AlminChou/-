package com.example.almin.fragment;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.library.model.User;
import com.example.almin.listener.UpdateInfoSaveListener;
import com.example.almin.listener.UpdateLocalUserListener;
import com.example.almin.utils.MyUtils;
import com.example.almin.webservice.UsersRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


@SuppressLint("ValidFragment")
public class UpdateMyInfoFragment extends AbstractFragment implements UpdateInfoSaveListener{
	private LayoutInflater mLayoutInflater;
	private ViewGroup mRootView;
	private UpdateMode mUpdateMode;
	private LinearLayout mLlInfo,mLlPassword;
	private RelativeLayout mRlUpdatePassowd;
	private EditText mEtName,mEtPosition,mEtOldPassword,mEtNewPassword;
	private Spinner mSpDepartment,mSpSex;
	private ImageView mIvCleanName,mIvCleanPosition,mIvCleanOldPassword,mIvCleanNewPassword;
	private boolean mIsTabViewVisible;
	private User mUser;
	private UpdateLocalUserListener mUpdateLocalUserListener;

	public UpdateMyInfoFragment(UpdateLocalUserListener updateLocalUserListener) {
		mUpdateLocalUserListener = updateLocalUserListener;
	}

	public UpdateMyInfoFragment getUpdateMyInfoFragment(UpdateLocalUserListener updateLocalUserListener){
		return new UpdateMyInfoFragment(updateLocalUserListener);
	}

	@Override
	public String getFragmentTag() {
		return "UpdateMyInfoFragment";
	}

	@Override
	protected boolean isBackButtonVisible() {
		return true;
	}

	@Override
	protected boolean isActionbarVisible() {
		return true;
	}

	@Override
	protected boolean isTabViewVisible() {
		return mIsTabViewVisible;
	}

	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mLayoutInflater = layoutInflater;
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_update_my_info, container, false);
		mLlInfo = (LinearLayout) mRootView.findViewById(R.id.ll_my_info);
		mLlPassword = (LinearLayout) mRootView.findViewById(R.id.ll_my_password);
		mRlUpdatePassowd = (RelativeLayout) mRootView.findViewById(R.id.rl_update_my_password);
		mEtName = (EditText) mRootView.findViewById(R.id.et_update_name);
		mEtPosition = (EditText) mRootView.findViewById(R.id.et_update_position);
		mEtOldPassword = (EditText) mRootView.findViewById(R.id.et_update_old_password);
		mEtNewPassword = (EditText) mRootView.findViewById(R.id.et_update_new_password);
		mSpDepartment = (Spinner) mRootView.findViewById(R.id.sp_update_department);
		mSpSex = (Spinner) mRootView.findViewById(R.id.sp_update_sex);
		mIvCleanName = (ImageView) mRootView.findViewById(R.id.iv_clean_name);
		mIvCleanPosition = (ImageView) mRootView.findViewById(R.id.iv_clean_position);
		mIvCleanOldPassword = (ImageView) mRootView.findViewById(R.id.iv_clean_old_password);
		mIvCleanNewPassword = (ImageView) mRootView.findViewById(R.id.iv_clean_new_password);
		initMyInfo();
		mUpdateMode = new UpdateMode.UpdateInfoMode(this);
		initViewShowMode();
		initViewClickListener();

		return mRootView;
	}

	private void initMyInfo() {
		mUser = MyApplication.getInstance().getUser();
		mEtName.setText(mUser.getName());
		mEtPosition.setText(mUser.getPosition());
		mSpDepartment.setSelection(Integer.parseInt(mUser.getDepartment()));
		mSpSex.setSelection(Integer.parseInt(mUser.getSex()));
	}

	private void initViewShowMode() {
		mLlInfo.setVisibility(mUpdateMode.isShowInfoLayout()?View.VISIBLE:View.GONE);
		mLlPassword.setVisibility(mUpdateMode.isShowPasswordLayout()?View.VISIBLE:View.GONE);
	}

	private void initViewClickListener() {
		mRlUpdatePassowd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mUpdateMode = new UpdateMode.UpdatePasswordMode(UpdateMyInfoFragment.this);
				initViewShowMode();
			}
		});
		mIvCleanName.setTag(mEtName);
		mIvCleanName.setOnClickListener(new ivOnClickListener());
		mIvCleanPosition.setTag(mEtPosition);
		mIvCleanPosition.setOnClickListener(new ivOnClickListener());
		mIvCleanOldPassword.setTag(mEtOldPassword);
		mIvCleanOldPassword.setOnClickListener(new ivOnClickListener());
		mIvCleanNewPassword.setTag(mEtNewPassword);
		mIvCleanNewPassword.setOnClickListener(new ivOnClickListener());

	}

	private class ivOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			EditText editText = (EditText) v.getTag();
			editText.setText("");
		}
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.update_my_info, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_save:
			MyUtils.setInputMethodHide(getActivity());
			if(MyApplication.getInstance().isNetworkAvailable()){
				mUpdateMode.clickSave();
			}else{
				showToast("当前网络环境不可用！");
			}
			break;
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onBackPressed() {
		hideSpinner();
		mIsPopBackStack = true;
		if(mUpdateMode.isShowPasswordLayout()){
			mUpdateMode = new UpdateMode.UpdateInfoMode(this);
			initViewShowMode();
			mIsPopBackStack = false;
		}
		return mIsPopBackStack;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mIsTabViewVisible=true;
		updateTabViewStatus();
	}

	public abstract static class UpdateMode{
		protected abstract void clickSave();
		protected abstract boolean isShowInfoLayout();
		protected abstract boolean isShowPasswordLayout();
		protected UpdateInfoSaveListener mUpdateInfoSaveListener;
		public static class UpdateInfoMode extends UpdateMode{

			public UpdateInfoMode(UpdateInfoSaveListener updateInfoSaveListener) {
				mUpdateInfoSaveListener = updateInfoSaveListener;
			}

			@Override
			protected void clickSave() {
				mUpdateInfoSaveListener.clickSaveInfoBtn();
			}

			@Override
			protected boolean isShowInfoLayout() {
				return true;
			}

			@Override
			protected boolean isShowPasswordLayout() {
				return false;
			}
		}

		public static class UpdatePasswordMode extends UpdateMode{

			public UpdatePasswordMode(UpdateInfoSaveListener updateInfoSaveListener) {
				mUpdateInfoSaveListener = updateInfoSaveListener;
			}

			@Override
			protected void clickSave() {
				mUpdateInfoSaveListener.clickSavePasswordBtn();
			}

			@Override
			protected boolean isShowInfoLayout() {
				return false;
			}

			@Override
			protected boolean isShowPasswordLayout() {
				return true;
			}
		}
	}

	@Override
	public void clickSaveInfoBtn() {
		if(!MyApplication.isTextEmptyOrNull(mEtName.getText().toString().trim())&&!MyApplication.isTextEmptyOrNull(mEtPosition.getText().toString().trim())){
			showSpinner(mLayoutInflater, mRootView);
			RequestParams params = new RequestParams();
			params.put("username", mUser.getUsername());
			params.put("name", mEtName.getText().toString().trim());
			params.put("position", mEtPosition.getText().toString().trim());
			params.put("department", mSpDepartment.getSelectedItemPosition());
			params.put("sex", mSpSex.getSelectedItemPosition());
			UsersRestClient.post(UsersRestClient.ACTION_UPDATE_USER_INFO, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] arg1, byte[] arg2) {
					if(statusCode==200&&new String(arg2).equalsIgnoreCase("done")){
						updateLocalUserInfo(false);
						MyApplication.getInstance().setUser(mUser);
						mUpdateLocalUserListener.updateLocalUser();
						hideSpinner();
					}else{
						showToast("操作有误！");
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] arg1, byte[] arg2, Throwable arg3) {
					showToast("请求失败！");
				}
			});
		}else{
			showToast("请正确填满信息！");
		}

	}

	@Override
	public void clickSavePasswordBtn() {
		if(!MyApplication.isTextEmptyOrNull(mEtOldPassword.getText().toString().trim())&&!MyApplication.isTextEmptyOrNull(mEtNewPassword.getText().toString().trim())){
			showSpinner(mLayoutInflater, mRootView);
			RequestParams params = new RequestParams();
			params.put("username", mUser.getUsername());
			params.put("oldPassword", mEtOldPassword.getText().toString().trim());
			params.put("newPassword", mEtNewPassword.getText().toString().trim());
			UsersRestClient.post(UsersRestClient.ACTION_UPDATE_PASSWORD, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] arg1, byte[] arg2) {
					if(statusCode==200&&new String(arg2).equalsIgnoreCase("done")){
						updateLocalUserInfo(true);
						MyApplication.getInstance().setUser(mUser);
						hideSpinner();
					}else{
						showToast("请输入正确的旧密码！");
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] arg1, byte[] arg2, Throwable arg3) {
					showToast("请求失败！");
				}
			});
		}else{
			showToast("请正确填满信息！");
		}
	}

	private void updateLocalUserInfo(boolean isUpdatePassword) {
		if(isUpdatePassword){
			mUser.setPassword(mEtNewPassword.getText().toString());
		}else{
			mUser.setName(mEtName.getText().toString().trim());
			mUser.setPosition(mEtPosition.getText().toString().trim());
			mUser.setDepartment(String.valueOf(mSpDepartment.getSelectedItemPosition()));
			mUser.setSex(String.valueOf(mSpSex.getSelectedItemPosition()));
		}
	}
}
