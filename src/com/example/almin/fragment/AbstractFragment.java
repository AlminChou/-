package com.example.almin.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.almin.R;
import com.example.almin.activity.MainActivity;

public abstract class AbstractFragment extends Fragment {
	private ViewGroup mSpinnerOverlay;
	protected Handler mHandler = new Handler();
	protected boolean mIsPopBackStack;
	public abstract String getFragmentTag();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (isActionbarVisible()) {
			activity.getActionBar().show();
		} else {
			activity.getActionBar().hide();
		}

		onTheAppAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = onTheAppCreateView(layoutInflater, container,
				savedInstanceState);
		setHasOptionsMenu(true);
		updateActionbarStatus();
		updateTabViewStatus();
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		return view;
	}
	
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		return super
				.onCreateView(layoutInflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		onTheAppResume();
	}

	protected void onTheAppAttach(Activity activity) {
	}

	protected void onTheAppResume() {

	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		for (int i = 0; i < menu.size(); i++) {
//			menu.removeGroup(i);
//		}
//	}
	
	public boolean onBackPressed() {
		return mIsPopBackStack;
	}
	
	protected String getActionBarTitle() {
		return "";
	}
	
	protected void updateActionbarStatus() {
		if((MainActivity)getActivity()!=null){
			ActionBar actionBar = ((MainActivity)getActivity()).getActionBar();
			actionBar.setTitle(getActionBarTitle());
			actionBar.setLogo(isLogoVisible() ? null: null);
			actionBar.setDisplayHomeAsUpEnabled(isBackButtonVisible());
		}
	}
	
	protected boolean isActionbarVisible() {
		return true;
	}

	protected boolean isLogoVisible() {
		return false;
	}

	protected boolean isBackButtonVisible() {
		return false;
	}

	protected boolean isTabViewVisible() {
		return true;
	}
	
	public void updateTabViewStatus() {
		if (getActivity() != null) {
			if (getActivity() instanceof MainActivity) {
				((MainActivity) getActivity())
						.showTabViewByFlag(isTabViewVisible());
			}
		}
	}
	
	protected void showSpinner(LayoutInflater layoutInflater,
			ViewGroup container) {
		if (mSpinnerOverlay == null) {
			mSpinnerOverlay = (ViewGroup) layoutInflater.inflate(
					R.layout.overlay_spinner, container, false);
		} else {
			((ViewGroup) mSpinnerOverlay.getParent())
					.removeView(mSpinnerOverlay);
		}

		mSpinnerOverlay.setVisibility(View.VISIBLE);
		mSpinnerOverlay.findViewById(R.id.spinner).setVisibility(View.VISIBLE);

		//禁止点击界面
		mSpinnerOverlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		container.addView(mSpinnerOverlay);
	}
	
	protected void showSpinner(LayoutInflater layoutInflater,
			ViewGroup container,boolean isShowProgress) {
		if (mSpinnerOverlay == null) {
			mSpinnerOverlay = (ViewGroup) layoutInflater.inflate(
					R.layout.overlay_spinner, container, false);
		} else {
			((ViewGroup) mSpinnerOverlay.getParent()).removeView(mSpinnerOverlay);
		}

		mSpinnerOverlay.setVisibility(View.VISIBLE);
		mSpinnerOverlay.findViewById(R.id.spinner).setVisibility(isShowProgress?View.VISIBLE:View.INVISIBLE);

		//禁止点击界面
		mSpinnerOverlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		container.addView(mSpinnerOverlay);
	}

	protected void hideSpinner() {
		if (mSpinnerOverlay != null)
			mSpinnerOverlay.setVisibility(View.GONE);
	}
	
	protected void showToast(final String msg) {
		if (getActivity() != null) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT)
							.show();
				}

			});
		}
	}
}
