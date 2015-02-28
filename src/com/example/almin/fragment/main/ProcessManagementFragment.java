package com.example.almin.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almin.R;
import com.example.almin.fragment.AbstractFragment;

public class ProcessManagementFragment extends AbstractFragment {
	private View mRootView;

	public ProcessManagementFragment getProcessManagementFragment(){
		return new ProcessManagementFragment();
	}

	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_process_management, container, false);

		return mRootView;
	}

	@Override
	public String getFragmentTag() {
		// TODO Auto-generated method stub
		return null;
	}
}
