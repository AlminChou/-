package com.example.almin.adapter;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.dialog.MyAlertDialog;
import com.example.almin.library.model.Asset;
import com.example.almin.listener.SpinnerStateListener;
import com.example.almin.webservice.AssetsRestClient;
import com.example.almin.widget.PinnedHeaderExpandableListView;
import com.example.almin.widget.PinnedHeaderExpandableListView.HeaderAdapter;
import com.example.almin.widget.SwipeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PersonalSearchAdapter extends BaseExpandableListAdapter implements HeaderAdapter{
	private String mCategory ;
	private HashMap<Integer, List<Asset>> mGroupDatas;
	private Context mContext;  
	private PinnedHeaderExpandableListView mListView;  
	private LayoutInflater mInflater;  
	private SpinnerStateListener mSpinnerStateListener;


	public PersonalSearchAdapter(Context context,PinnedHeaderExpandableListView listView,HashMap<Integer, List<Asset>> groupDatas,SpinnerStateListener spinnerStateListener,String category){  
		mGroupDatas = groupDatas;
		mContext = context;  
		mListView = listView;  
		mSpinnerStateListener = spinnerStateListener;
		mCategory = category;
		mInflater = LayoutInflater.from(mContext);  
	}  

	@Override  
	public Object getChild(int groupPosition, int childPosition) {  
		return mGroupDatas.get(groupPosition).get(childPosition);  
	}  

	@Override  
	public long getChildId(int groupPosition, int childPosition) {  
		return childPosition;  
	}  

	@Override  
	public View getChildView(int groupPosition, int childPosition,  
			boolean isLastChild, View convertView, ViewGroup parent) {  
		final Asset asset = mGroupDatas.get(groupPosition).get(childPosition);
		ChildHolder childHolder = null;
		if (convertView == null||((ChildHolder) convertView.getTag()).swipeView.isBackShow()) {   
			childHolder=new ChildHolder();
			convertView = mInflater.inflate(R.layout.my_assets_child_item, null);
			childHolder.swipeView=(com.example.almin.widget.SwipeView) convertView.findViewById(R.id.item_content);
			childHolder.tvName = (TextView) childHolder.swipeView.findViewById(R.id.tv_asset_name);
			childHolder.tvState = (TextView) childHolder.swipeView.findViewById(R.id.tv_asset_state);
			childHolder.tvDescription = (TextView) childHolder.swipeView.findViewById(R.id.tv_asset_description);
			convertView.setTag(childHolder);
		} else {   
			childHolder = (ChildHolder) convertView.getTag(); 
		} 

		childHolder.tvName.setText(asset.getName());
		childHolder.tvState.setText(asset.getState());
		childHolder.tvDescription.setText(asset.getDescription());
		childHolder.swipeView.setLeftButtonVisible(false);
		childHolder.swipeView.setRightButtonText("申请");
		childHolder.swipeView.setRightButtonBackground(R.color.other_btn_color);
//		childHolder.swipeView.setOnFrontViewClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				notifyDataSetChanged();
//			}
//		});
		childHolder.swipeView.setonBackViewDeleteClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MyAlertDialog(mContext, "是否申请？", "确定", "取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(MyApplication.getInstance().isNetworkAvailable()){
							RequestParams params = new RequestParams();
							
							AssetsRestClient.post("", params , mApplyHandler);
						}else{
							Toast.makeText(mContext, "当前网络不可用！！", Toast.LENGTH_LONG).show();
						}
					}
				}, null);
				notifyDataSetChanged();
			}
		});

		return convertView;   
	}  

	@Override  
	public int getChildrenCount(int groupPosition) {  
		return mGroupDatas.get(groupPosition).size();  
	}  

	@Override  
	public Object getGroup(int groupPosition) {  
		return mGroupDatas.get(groupPosition);  
	}  

	@Override  
	public int getGroupCount() {  
		return mGroupDatas.size();  
	}  

	@Override  
	public long getGroupId(int groupPosition) {  
		return groupPosition;  
	}  

	@Override  
	public View getGroupView(final int groupPosition, final boolean isExpanded,  
			View convertView, ViewGroup parent) {  

		GroupHolder groupHolder=null;
		if (convertView == null) {   
			groupHolder=new GroupHolder();
			convertView = mInflater.inflate(R.layout.my_assets_floatview, null);
			groupHolder.tvHead = (TextView) convertView.findViewById(R.id.tv_head_title);
			convertView.setTag(groupHolder);
		} else {   
			groupHolder = (GroupHolder) convertView.getTag(); 
		}   

		groupHolder.tvHead.setText(mCategory);  
		mListView.expandGroup(groupPosition);
		return convertView;   
	}  

	@Override  
	public boolean hasStableIds() {  
		return true;  
	}  

	@Override  
	public boolean isChildSelectable(int groupPosition, int childPosition) {  
		return true;  
	}  

	@Override  
	public int getHeaderState(int groupPosition, int childPosition) {  
		final int childCount = getChildrenCount(groupPosition);  
		if (childPosition == childCount - 1) {  
			return PINNED_HEADER_PUSHED_UP;  
		} else if (childPosition == -1  
				&& !mListView.isGroupExpanded(groupPosition)) {  
			return PINNED_HEADER_GONE;  
		} else {  
			return PINNED_HEADER_VISIBLE;  
		}  
	}  

	@Override  
	public void configureHeader(View header, int groupPosition,  
			int childPosition, int alpha) {  
		String groupData = mCategory;  
		((TextView) header.findViewById(R.id.tv_head_title)).setText(groupData);  
	}  


	@Override  
	public void setGroupClickStatus(int groupPosition, int status) {  
	}  

	@Override  
	public int getGroupClickStatus(int groupPosition) {  
		return 0;   
	}  

	class ChildHolder{
		SwipeView swipeView;
		TextView tvName;
		TextView tvState;
		TextView tvDescription;
	}
	class GroupHolder{
		TextView tvHead;
	}
	
	private AsyncHttpResponseHandler mApplyHandler = new AsyncHttpResponseHandler(){

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			
		}
		
	};
	
}
