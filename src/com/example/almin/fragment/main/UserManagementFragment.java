package com.example.almin.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almin.R;
import com.example.almin.fragment.AbstractFragment;

public class UserManagementFragment extends AbstractFragment {
	private View mRootView;

	public UserManagementFragment getUserManagementFragment(){
		return new UserManagementFragment();
	}

	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_user_management, container, false);

		return mRootView;
	}

	@Override
	public String getFragmentTag() {
		// TODO Auto-generated method stub
		return null;
	}
}
