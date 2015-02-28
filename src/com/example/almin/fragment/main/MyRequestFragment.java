package com.example.almin.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almin.R;
import com.example.almin.fragment.AbstractFragment;

public class MyRequestFragment extends AbstractFragment {
	private View mRootView;
	
	public MyRequestFragment getMyRequestFragment(){
		return new MyRequestFragment();
	}
	
	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_my_request, container, false);
		
		
		
		return mRootView;
	}

	@Override
	public String getFragmentTag() {
		return null;
	}
}
