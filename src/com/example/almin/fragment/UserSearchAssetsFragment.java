package com.example.almin.fragment;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.adapter.PersonalSearchAdapter;
import com.example.almin.library.model.Asset;
import com.example.almin.library.model.User;
import com.example.almin.listener.UpdateSearchModeListener;
import com.example.almin.utils.MyUtils;
import com.example.almin.webservice.AssetsRestClient;
import com.example.almin.webservice.CommentsRestClient;
import com.example.almin.widget.PinnedHeaderExpandableListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserSearchAssetsFragment extends AbstractFragment implements UpdateSearchModeListener{
	private ViewGroup mRootView;
	private PinnedHeaderExpandableListView mListView;
	private LinearLayout mLlSearch;
	private RelativeLayout mRlComment,mRlResult;
	private EditText mEtSearchInfo,mEtNeedAsset,mEtComment;
	private Spinner mSpCategory;
	private ImageButton mBtnSearch,mBtnComment; 
	private SearchMode mSearchMode;
	private HashMap<Integer, List<Asset>> mListResult;
	private PersonalSearchAdapter mAdapter;
	
	@Override
	public String getFragmentTag() {
		return "UserSearchAssetsFragment";
	}

	@Override
	protected boolean isTabViewVisible() {
		return false;
	}
	
	@Override
	protected boolean isBackButtonVisible() {
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		for (int i = 0; i < menu.size(); i++) {
			menu.removeGroup(i);
		}
	}
	
	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_users_search_assets, container, false);
		initView();
		
		return mRootView;
	}

	private void initView() {
		mLlSearch = (LinearLayout) mRootView.findViewById(R.id.ll_search);
		mRlComment = (RelativeLayout) mRootView.findViewById(R.id.rl_comment);
		mRlResult = (RelativeLayout) mRootView.findViewById(R.id.rl_lv_result);
		mListView = (PinnedHeaderExpandableListView) mRootView.findViewById(R.id.lv_search_result);
		mListView.setHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.my_assets_floatview,  
				mListView, false));
		mListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		mEtSearchInfo = (EditText) mRootView.findViewById(R.id.et_find_asset_name);
		mEtComment = (EditText) mRootView.findViewById(R.id.et_needs_comment);
		mEtNeedAsset = (EditText) mRootView.findViewById(R.id.et_needs_comment_asset);
		mSpCategory = (Spinner) mRootView.findViewById(R.id.sp_category);
		mBtnSearch = (ImageButton) mRootView.findViewById(R.id.btn_search);
		mBtnComment = (ImageButton) mRootView.findViewById(R.id.btn_comment);
		mSearchMode = new SearchMode.SearchActionMode();
		updateViewStatus();
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyUtils.setInputMethodHide(getActivity());
				if(MyApplication.getInstance().isNetworkAvailable()){
					String searchInfo = mEtSearchInfo.getText().toString().trim();
					RequestParams params = new RequestParams(); 
					params.put("state", "空闲");
					params.put("assetName", searchInfo);
					params.put("category", mSpCategory.getSelectedItem().toString());
					AssetsRestClient.post(MyApplication.isTextEmptyOrNull(searchInfo)?
							AssetsRestClient.ACTION_GET_ASSETS_WITH_STATE_IN_CATEGORY:
								AssetsRestClient.ACTION_GET_ASSETS_WITH_STATE_AND_NAME_IN_CATEGORY, params, mResultHandler);
				}else{
					showToast("当前网络不可用！请检查！");
				}
			}
		});
		mBtnComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//发布留言
				if(MyApplication.getInstance().isNetworkAvailable()){
					if(!MyApplication.isTextEmptyOrNull(mEtNeedAsset.getText().toString())
							&&!MyApplication.isTextEmptyOrNull(mEtComment.getText().toString())){
						User user = MyApplication.getInstance().getUser();
						RequestParams params = new RequestParams();
						params.put("useraccount", user.getUsername());
						params.put("username", user.getName() );
						params.put("asset", mEtNeedAsset.getText().toString().trim());
						params.put("date", MyApplication.getNowDateTime());
						params.put("comment", mEtComment.getText().toString().trim());
						CommentsRestClient.post(CommentsRestClient.ACTION_LEAVE_COMMENT, params, mCommentHandler);
					}else{
						showToast("请正确填写留言信息！！");
					}
				}else{
					showToast("当前网络不可用！请检查！");
				}
			}
		});
	}
	
	private void updateViewStatus(){
		mLlSearch.setVisibility(mSearchMode.isSearchLayoutVisible()?View.VISIBLE:View.GONE);
		mRlComment.setVisibility(mSearchMode.isCommentLayoutVisible()?View.VISIBLE:View.GONE);
		mRlResult.setVisibility(mSearchMode.isListViewVisible()?View.VISIBLE:View.GONE);
	}
	
	
	@Override
	public boolean onBackPressed() {
		boolean isBack = mRlResult.getVisibility()==View.VISIBLE;
		if(isBack){
			navigationToSearchMode();
		}
		
		return !isBack;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mListResult = null;
	}
	
private AsyncHttpResponseHandler mCommentHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] arg1, byte[] result) {
			if(new String(result).equalsIgnoreCase("done")){
				showToast("留言成功，请耐心等待！");
			}else{
				showToast("留言失败，请重试！");
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] result, Throwable arg3) {
			showToast("请求操作有误！");
		}
	};
	
	
	private AsyncHttpResponseHandler mResultHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] arg1, byte[] result) {
			if(!MyApplication.isTextNoResultForJson(new String(result))&&getActivity()!=null){
				mListResult =null;
				mListResult = Asset.decodeAssetsFromJsonToHashMap(new String(result),mSpCategory.getSelectedItem().toString());
				mAdapter = new PersonalSearchAdapter(getActivity(), mListView, mListResult, mSpCategory.getSelectedItem().toString(),UserSearchAssetsFragment.this);
				mListView.setAdapter(mAdapter);
				mSearchMode = new SearchMode.SearchResultMode();
				System.out.println("!null"+new String(result));
			}else{
				System.out.println("null"+new String(result));
				mSearchMode = new SearchMode.NoResultMode();
			}
			updateViewStatus();
		}
		
		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] result, Throwable arg3) {
			showToast("请求操作有误！");
		}
	};
	
	private static abstract class SearchMode{
		protected abstract boolean isListViewVisible();
		protected abstract boolean isCommentLayoutVisible();
		protected abstract boolean isSearchLayoutVisible();
		private static class SearchActionMode extends SearchMode{

			@Override
			protected boolean isListViewVisible() {
				return false;
			}

			@Override
			protected boolean isCommentLayoutVisible() {
				return false;
			}

			@Override
			protected boolean isSearchLayoutVisible() {
				return true;
			}
			
		}
		private static class NoResultMode extends SearchMode{
			@Override
			protected boolean isListViewVisible() {
				return false;
			}

			@Override
			protected boolean isCommentLayoutVisible() {
				return true;
			}

			@Override
			protected boolean isSearchLayoutVisible() {
				return true;
			}
		}
		private static class SearchResultMode extends SearchMode{
			@Override
			protected boolean isListViewVisible() {
				return true;
			}

			@Override
			protected boolean isCommentLayoutVisible() {
				return false;
			}

			@Override
			protected boolean isSearchLayoutVisible() {
				return false;
			}
		}
	}

	@Override
	public void navigationToSearchMode() {
		mSearchMode = new SearchMode.SearchActionMode();
		updateViewStatus();
	}
}
