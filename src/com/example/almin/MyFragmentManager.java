package com.example.almin;


import com.example.almin.fragment.AbstractFragment;

import android.support.v4.app.FragmentManager;


public class MyFragmentManager {
	static MyFragmentManager mMyFragmentManager;
	private FragmentManager mFragmentManager;
	private int mContainerId;
	
	public MyFragmentManager(FragmentManager fragmentManager,int containerId) {
		mContainerId = containerId;
		mFragmentManager = fragmentManager;
	}
	
	public static void init(FragmentManager fm, int containerId) {
//		if (fragmentManager == null)
		mMyFragmentManager = new MyFragmentManager(fm, containerId);
	}
	
	public void replaceFragmentAndAdd2BackStack(AbstractFragment fragment) {
		mFragmentManager.beginTransaction().replace(mContainerId, fragment,fragment.getFragmentTag()).addToBackStack(fragment.getFragmentTag()).commit();
	}
	public void addFragmentAndAdd2BackStack(AbstractFragment fragment) {
		mFragmentManager.beginTransaction().add(mContainerId, fragment,fragment.getFragmentTag()).addToBackStack(fragment.getFragmentTag()).commit();
	}

	public static MyFragmentManager getInstance() {
		return mMyFragmentManager;
	}
	
//	public AbstractFragment getCurrentFragment() {
//		try{
//			if(mFragmentTagStack.size() > 0){
//				return (AbstractFragment) getFragmentManager()
//						.findFragmentByTag(mFragmentTagStack.lastElement());
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
}
