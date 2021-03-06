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
import com.example.almin.library.model.User;
import com.example.almin.listener.UpdateSearchModeListener;
import com.example.almin.webservice.AssetsProcessRestClient;
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
	private UpdateSearchModeListener mUpdateSearchModeListener;

	public PersonalSearchAdapter(Context context,PinnedHeaderExpandableListView listView,HashMap<Integer, List<Asset>> groupDatas,String category,UpdateSearchModeListener updateSearchModeListener){  
		mGroupDatas = groupDatas;
		mContext = context;  
		mListView = listView;  
		mCategory = category;
		mUpdateSearchModeListener = updateSearchModeListener;
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
		childHolder.swipeView.setRightButtonText("����");
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
				new MyAlertDialog(mContext, "�Ƿ����룿", "ȷ��", "ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(MyApplication.getInstance().isNetworkAvailable()){
							User user = MyApplication.getInstance().getUser();
							RequestParams params = new RequestParams();
							params.put("assetId", asset.getId());
							params.put("name", user.getName());
							params.put("state", "����");
							params.put("message", "");
							params.put("processDate", MyApplication.getNowDateTime());
							params.put("useraccount", user.getUsername());
							AssetsProcessRestClient.post(AssetsProcessRestClient.ACTION_ADD_PROCESS_AND_UPDATE_ASSET_STATE, params , mApplyHandler);
						}else{
							Toast.makeText(mContext, "��ǰ���粻���ã���", Toast.LENGTH_LONG).show();
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

	private static class ChildHolder{
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
		public void onFailure(int statusCode, Header[] arg1, byte[] result,
				Throwable arg3) {
			Toast.makeText(mContext, "����ʧ�ܣ���������", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onSuccess(int statusCode, Header[] arg1, byte[] result) {
			if(new String(result).equalsIgnoreCase("done")){
				mUpdateSearchModeListener.navigationToSearchMode();
				Toast.makeText(mContext, "����ɹ�,�����ĵȴ���ˣ�", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(mContext, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
		}
		
	};
	
}
