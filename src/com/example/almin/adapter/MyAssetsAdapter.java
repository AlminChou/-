package com.example.almin.adapter;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.dialog.MyAlertDialog;
import com.example.almin.library.model.Asset;
import com.example.almin.listener.SpinnerStateListener;
import com.example.almin.webservice.ProcessRestClient;
import com.example.almin.widget.PinnedHeaderExpandableListView;
import com.example.almin.widget.PinnedHeaderExpandableListView.HeaderAdapter;
import com.example.almin.widget.SwipeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyAssetsAdapter extends BaseExpandableListAdapter implements HeaderAdapter{
	private String[] mCategory = {"电器","家具","杂物","车辆","房屋","文具"};
	private HashMap<Integer, List<Asset>> mGroupDatas;
	private Context mContext;  
	private PinnedHeaderExpandableListView mListView;  
	private LayoutInflater mInflater;  
	private PopupWindow mPopupWindow;
	private String mOtherState="维修";
	private SpinnerStateListener mSpinnerStateListener;
	public MyAssetsAdapter(Context context,PinnedHeaderExpandableListView listView,HashMap<Integer, List<Asset>> groupDatas,SpinnerStateListener spinnerStateListener){  
		mGroupDatas = groupDatas;
		mContext = context;  
		mListView = listView;  
		mSpinnerStateListener = spinnerStateListener;
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
		childHolder.swipeView.setOnFrontViewClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow=makeAssetInfoPopupWindow(asset);
				notifyDataSetChanged();
			}
		});
		childHolder.swipeView.setOnBackViewOthersClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow=makeProcessPopupWindow(asset);
				notifyDataSetChanged();
			}
		});
		childHolder.swipeView.setonBackViewDeleteClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MyAlertDialog(mContext, "是否归还？", "确定", "取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(MyApplication.getInstance().isNetworkAvailable()){
							mSpinnerStateListener.toShowSpinner();
							RequestParams params = new RequestParams();
							params.put("assetId", asset.getId());
							params.put("username", asset.getName());
							params.put("state", "归还");
							params.put("message", "");
							params.put("isAccept", "未通过");
							params.put("processDate", MyApplication.getNowDateTime());
							ProcessRestClient.post(ProcessRestClient.ACTION_ADD_PROCESS, params , mProcessHandler);
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

		//		ImageView iv = (ImageView)view.findViewById(R.id.groupIcon);  
		//		if (isExpanded) {  
		//			iv.setImageResource(R.drawable.btn_browser2);  
		//		}  
		//		else{  
		//			iv.setImageResource(R.drawable.btn_browser);  
		//		}  
		//
		groupHolder.tvHead.setText(mCategory[groupPosition]);  
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isExpanded){
					mListView.collapseGroup(groupPosition);
				}else{
					mListView.expandGroup(groupPosition);
				}
			}
		});
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
		String groupData = mCategory[groupPosition];  
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


	private PopupWindow makeAssetInfoPopupWindow(Asset asset) {
		PopupWindow window;
		View contentView = mInflater.inflate(R.layout.my_assets_item_popup, null);
		((TextView) contentView.findViewById(R.id.tv_asset_id)).setText(asset.getId());;
		((TextView) contentView.findViewById(R.id.tv_asset_name)).setText(asset.getName());
		((TextView) contentView.findViewById(R.id.tv_asset_category)).setText(asset.getCategory());
		((TextView) contentView.findViewById(R.id.tv_asset_state)).setText(asset.getState());
		((TextView) contentView.findViewById(R.id.tv_asset_owner)).setText(asset.getOwner());
		((TextView) contentView.findViewById(R.id.tv_asset_date)).setText(asset.getPurchase_date());
		((TextView) contentView.findViewById(R.id.tv_asset_description)).setText(asset.getDescription());
		((ImageView) contentView.findViewById(R.id.iv_close_asset_info)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});

		window = new PopupWindow(contentView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		window.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);  
		window.setBackgroundDrawable(cd);  
		window.showAtLocation(mListView,Gravity.CENTER,0,0);
		return window;
	}

	private PopupWindow makeProcessPopupWindow(final Asset asset) {
		PopupWindow window;
		View contentView = mInflater.inflate(R.layout.my_assets_process_popup, null);
		((TextView) contentView.findViewById(R.id.tv_asset_id)).setText(asset.getId());;
		((TextView) contentView.findViewById(R.id.tv_asset_name)).setText(asset.getName());
		((TextView) contentView.findViewById(R.id.tv_asset_owner)).setText(MyApplication.getInstance().getUser().getName());
		final EditText etReason = (EditText) contentView.findViewById(R.id.et_asset_description);
		RadioGroup rgState  = (RadioGroup)contentView.findViewById(R.id.radioGroup_state_process);
		rgState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton)group.findViewById(checkedId);
				mOtherState=rb.getText().toString();
			}
		});
		((Button)contentView.findViewById(R.id.btn_process_send)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(MyApplication.getInstance().isNetworkAvailable()){
					mSpinnerStateListener.toShowSpinner();
					RequestParams params = new RequestParams();
					params.put("assetId", asset.getId());
					params.put("username", asset.getName());
					params.put("state", mOtherState);
					params.put("message", etReason.getText().toString().trim());
					params.put("isAccept", "未通过");
					params.put("processDate", MyApplication.getNowDateTime());
					ProcessRestClient.post(ProcessRestClient.ACTION_ADD_PROCESS, params , mProcessHandler);
				}else{
					Toast.makeText(mContext, "当前网络不可用！！", Toast.LENGTH_LONG).show();
				}
			}
		});
		((ImageView) contentView.findViewById(R.id.iv_close_asset_info)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});

		window = new PopupWindow(contentView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		window.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);  
		window.setBackgroundDrawable(cd);  
		window.showAtLocation(mListView,Gravity.CENTER,0,0);
		return window;
	}



	private AsyncHttpResponseHandler mProcessHandler = new AsyncHttpResponseHandler(){

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			Toast.makeText(mContext, "请求失败！！", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			if(MyApplication.isTextDoneOrFaile(new String(arg2))){
				System.out.println(new String(arg2));
				if(mPopupWindow!=null){
					mPopupWindow.dismiss();
				}
				mSpinnerStateListener.toHideSpinner();
			}else{
				Toast.makeText(mContext, "操作失败！！", Toast.LENGTH_LONG).show();
			}
		}
	};
}
