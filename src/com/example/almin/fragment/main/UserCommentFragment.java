package com.example.almin.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almin.R;
import com.example.almin.fragment.AbstractFragment;

public class UserCommentFragment extends AbstractFragment {
	private View mRootView;

	public UserCommentFragment getUserCommentFragment(){
		return new UserCommentFragment();
	}

	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_user_comment, container, false);

		return mRootView;
	}

	@Override
	public String getFragmentTag() {
		return null;
	}
}
