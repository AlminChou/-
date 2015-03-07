package com.example.almin.fragment;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.adapter.PersonalSearchAdapter;
import com.example.almin.library.model.Asset;
import com.example.almin.webservice.AssetsRestClient;
import com.example.almin.widget.PinnedHeaderExpandableListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserSearchAssetsFragment extends AbstractFragment{
	private ViewGroup mRootView;
	private PinnedHeaderExpandableListView mListView;
	private LinearLayout mLlSearch;
	private RelativeLayout mRlComment;
	private EditText mEtSearchInfo,mEtComment;
	private Spinner mSpCategory;
	private ImageButton mBtnSearch; 
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
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_users_search_assets, container, false);
		initView();
		
		return mRootView;
	}

	private void initView() {
		mLlSearch = (LinearLayout) mRootView.findViewById(R.id.ll_search);
		mRlComment = (RelativeLayout) mRootView.findViewById(R.id.rl_comment);
		mListView = (PinnedHeaderExpandableListView) mRootView.findViewById(R.id.lv_search_result);
		mEtSearchInfo = (EditText) mRootView.findViewById(R.id.et_find_asset_name);
		mEtComment = (EditText) mRootView.findViewById(R.id.et_needs_comment);
		mSpCategory = (Spinner) mRootView.findViewById(R.id.sp_category);
		mBtnSearch = (ImageButton) mRootView.findViewById(R.id.btn_search);
		mSearchMode = new SearchMode.SearchActionMode();
		updateViewStatus();
		mBtnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MyApplication.getInstance().isNetworkAvailable()){
					String searchInfo = mEtSearchInfo.getText().toString().trim();
					RequestParams params = new RequestParams(); 
					params.put("state", "空闲");
					params.put("assetName", searchInfo);
					AssetsRestClient.post(MyApplication.isTextEmptyOrNull(searchInfo)?
							AssetsRestClient.ACTION_GET_ASSETS_WITH_STATE:AssetsRestClient.ACTION_GET_ASSETS_WITH_STATE_AND_NAME, params, mResultHandler);
				}else{
					showToast("当前网络不可用！请检查！");
				}
			}
		});
	}
	
	private void updateViewStatus(){
		mLlSearch.setVisibility(mSearchMode.isSearchLayoutVisible()?View.VISIBLE:View.GONE);
		mRlComment.setVisibility(mSearchMode.isCommentLayoutVisible()?View.VISIBLE:View.GONE);
		mListView.setVisibility(mSearchMode.isListViewVisible()?View.VISIBLE:View.GONE);
	}
	
	
	@Override
	public boolean onBackPressed() {
		return true;
	}
	
	private AsyncHttpResponseHandler mResultHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			if(!MyApplication.isTextNoResultForJson(new String(arg2))){
				mListResult = Asset.decodeAssetsFromJsonToHashMap(new String(arg2),mSpCategory.getSelectedItem().toString());
				mSearchMode = new SearchMode.SearchResultMode();
				System.out.println("!null"+new String(arg2));
			}else{
				System.out.println("null"+new String(arg2));
				mSearchMode = new SearchMode.NoResultMode();
			}
			updateViewStatus();
		}
		
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
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
}
