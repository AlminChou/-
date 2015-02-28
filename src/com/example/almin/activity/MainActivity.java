package com.example.almin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.almin.MyApplication;
import com.example.almin.MyFragmentManager;
import com.example.almin.R;
import com.example.almin.fragment.AbstractFragment;
import com.example.almin.fragment.UpdateMyInfoFragment;
import com.example.almin.fragment.main.MyRequestFragment;
import com.example.almin.fragment.main.PersonalAssetsFragment;
import com.example.almin.fragment.main.PersonalSettingFragment;
import com.example.almin.fragment.main.ProcessManagementFragment;
import com.example.almin.fragment.main.TotalAssetsFragment;
import com.example.almin.fragment.main.UserCommentFragment;
import com.example.almin.fragment.main.UserManagementFragment;
import com.example.almin.listener.UpdateLocalUserListener;
import com.example.almin.widget.MyFragmentTabHost;

public class MainActivity extends FragmentActivity{
	private long mExitTime=0;
	private MyFragmentTabHost myFragmentTabHost;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mInflater = LayoutInflater.from(getApplicationContext());
		setDisplaySize();
//		MyFragmentManager.init(getSupportFragmentManager(), android.R.id.tabcontent);
		MyFragmentManager.init(getSupportFragmentManager(), R.id.realtabcontent);
		initMyFragmentTabhost(MyApplication.getInstance().getUser().getUsertype().equalsIgnoreCase("管理员"));
	}

	private void initMyFragmentTabhost(boolean isAdmin) {
		myFragmentTabHost = (MyFragmentTabHost)findViewById(android.R.id.tabhost);
//		myFragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		myFragmentTabHost.setup(this, getSupportFragmentManager(),  R.id.realtabcontent);
		myFragmentTabHost.getTabWidget().setDividerDrawable(R.color.tab_view_bg);
		if(isAdmin){
			myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("Total").setIndicator(getTabItemView(R.drawable.tab_all_assets_btn)),
					TotalAssetsFragment.class, null);
			myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("users").setIndicator(getTabItemView(R.drawable.tab_users_btn)),
					UserManagementFragment.class, null);
			myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("process").setIndicator(getTabItemView(R.drawable.tab_process_btn)),
					ProcessManagementFragment.class, null);
			myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("comment").setIndicator(getTabItemView(R.drawable.tab_comment_btn)),
					UserCommentFragment.class, null);
		}else{
			myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("personal_assets").setIndicator(getTabItemView(R.drawable.tab_my_asset_btn)),
					PersonalAssetsFragment.class, null);
			myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("my_request").setIndicator(getTabItemView(R.drawable.tab_my_process_btn)),
					MyRequestFragment.class, null);
		}
		myFragmentTabHost.addTab(myFragmentTabHost.newTabSpec("my_setting").setIndicator(getTabItemView(R.drawable.tab_my_setting_btn)),
				PersonalSettingFragment.class, null);
	}

	private View getTabItemView(int tabId) {
		View view = mInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(tabId);
		return view;
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(getApplicationContext(), "点击了Actionbar的Home键！", 3000).show();
			onBackPressed();
			break;
//		case R.id.action_settings:
//			Toast.makeText(getApplicationContext(), "点击了Actionbar的Settings键！", 3000).show();
//			break;
		default:
			break;
		}
		 
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		int fragmentCount=getSupportFragmentManager().getBackStackEntryCount();
		System.out.println("fragment数量  ："+fragmentCount);
		if(fragmentCount==0){
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(getApplicationContext(),"再点一次退出！  ",Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
		}else{
			AbstractFragment fragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(fragmentCount-1).getName());
			if(fragment!=null&&fragment.onBackPressed()){
				getSupportFragmentManager().popBackStack();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	public static void navigateToUpdateMyInfoFragment(UpdateLocalUserListener updateLocalUserListener) {
		MyFragmentManager.getInstance().addFragmentAndAdd2BackStack(
				new UpdateMyInfoFragment(updateLocalUserListener));
	}
	
	public static void navigateToPersonalAssetsFragment() {
		PersonalAssetsFragment fragment =new PersonalAssetsFragment();
		fragment.setTabViewVisible(false);
		MyFragmentManager.getInstance().addFragmentAndAdd2BackStack(
				fragment);
	}
	
	public void showTabViewByFlag(boolean flag) {
		myFragmentTabHost.getTabWidget().setVisibility(flag?View.VISIBLE:View.GONE);
	}
	
	private void setDisplaySize() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		MyApplication.getInstance().setDisplayHeight(displayMetrics.heightPixels);
		MyApplication.getInstance().setDisplayWidth(displayMetrics.widthPixels);
	}

//	public void goBackToHomePage() {
//		Fragment tabHostFragment = TheBumpFragmentManager.getInstance()
//				.findFragmentByTag("tabhost_fragment");
//		if (tabHostFragment != null
//				&& tabHostFragment instanceof TabHostFragment) {
//			((TabHostFragment) tabHostFragment).goBackToHomePage();
//		}
//	}

	private void checkWhetherSetActionBarArrow() {
//		getSupportFragmentManager().addOnBackStackChangedListener(
//				new FragmentManager.OnBackStackChangedListener() {
//					@Override
//					public void onBackStackChanged() {
//						getActionBar().setDisplayHomeAsUpEnabled(
//								TheBumpFragmentManager.getInstance()
//										.getBackStackCount() > 0);
//					}
//				});
	}
}
