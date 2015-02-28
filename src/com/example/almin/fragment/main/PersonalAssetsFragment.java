package com.example.almin.fragment.main;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.adapter.MyAssetsAdapter;
import com.example.almin.fragment.AbstractFragment;
import com.example.almin.library.model.Asset;
import com.example.almin.listener.SpinnerStateListener;
import com.example.almin.webservice.AssetsRestClient;
import com.example.almin.widget.PinnedHeaderExpandableListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PersonalAssetsFragment extends AbstractFragment implements SpinnerStateListener{
	private ViewGroup mRootView;
	private LayoutInflater mInflater;
	private PinnedHeaderExpandableListView mListView;
	private MyAssetsAdapter mMyAssetsAdapter;
	private HashMap<Integer, List<Asset>> mMyAssetsHashMap;
	private boolean mIsTabViewVisible=true;
	
	public PersonalAssetsFragment getPersonalAssetsFragment(){
		return new PersonalAssetsFragment();
	}

	public void setTabViewVisible(boolean isTabViewVisible){
		mIsTabViewVisible=isTabViewVisible;
	}
	
	@Override
	protected String getActionBarTitle() {
		return "我的资产";
	}
	
	@Override
	protected boolean isTabViewVisible() {
		return mIsTabViewVisible;
	}
	
	@Override
	protected boolean isBackButtonVisible() {
		return true;
	}
	
	@Override
	protected void onTheAppAttach(Activity activity) {
		super.onTheAppAttach(activity);
	}
	
	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mInflater = layoutInflater;
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_personal_assets, container, false);
		mListView = (PinnedHeaderExpandableListView) mRootView.findViewById(R.id.plv_my_assets);
		mListView.setHeaderView(layoutInflater.inflate(R.layout.my_assets_floatview,  
				mListView, false));  
		getMyAssetsInfo();
		return mRootView;
	}

	@Override
	public String getFragmentTag() {
		return "PersonalAssetsFragment";
	}
	
	private void getMyAssetsInfo() {
		if(MyApplication.getInstance().isNetworkAvailable()){
			RequestParams params = new RequestParams();
			params.put("username", MyApplication.getInstance().getUser().getUsername());
			AssetsRestClient.get(AssetsRestClient.ACTION_GET_USER_ASSETS, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					System.out.println(new String(arg2));
					mMyAssetsHashMap= Asset.decodeAssetsFromJson(new String(arg2));
					if(mMyAssetsHashMap!=null){
						mMyAssetsAdapter=new MyAssetsAdapter(getActivity(),mListView,mMyAssetsHashMap,PersonalAssetsFragment.this);
						mListView.setAdapter(mMyAssetsAdapter);
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					showToast("请求失败！");
				}
			});
		}else{
			showToast("当前网络不可用！");
		}
	}
	
	@Override
	public boolean onBackPressed() {
		return true;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mIsTabViewVisible = true;
		updateTabViewStatus();
	}

	@Override
	public void toShowSpinner() {
		showSpinner(mInflater, mRootView);
	}

	@Override
	public void toHideSpinner() {
		hideSpinner();
	}
}
