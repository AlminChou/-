package com.example.almin.widget;




import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.almin.R;


public class SwipeView 
extends LinearLayout 
implements View.OnTouchListener, OnGestureListener {
	public static final int TYPE_TEXT = 0;
	public static final int TYPE_CHECKBOX = 1;
	public static final int TYPE_NONE = 2;
	private static final int HORIZONTAL_TOUCH_OFFSET=20;
	private Context mContext;
	private int mLastX;
	private LinearLayout mBackView, mFrontView,mBackOperateView;
	private int mBackViewWidth;
	private boolean mIsBackViewShow;
	//private TextView mTvFrontContent;
	private boolean mIsSwipeable = true;
	private Button mBtnDelete, mBtnOthers;
	private TextView mTvLeftText;
	private OnClickListener mFrontViewClickListener;
	private GestureDetector mDetector = null;;
	private boolean mIsFrontLeftClickable=true;
	public enum BackViewType {
		TYPE_BACK_NONE, TYPE_BACK_DELETE, TYPE_BACK_EDIT, TYPE_BACK_EDIT_DELETE
	}

	public SwipeView(Context context) {
		super(context);
		init(context);
	}

	public SwipeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SwipeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public boolean isBackViewShow() {
		return mIsBackViewShow;
	}

	public void setBackViewShow(boolean isBackViewShow) {
		this.mIsBackViewShow = isBackViewShow;
	}

	public void setLeftButtonVisible(boolean isVisible){
		mBtnOthers.setVisibility(isVisible?View.VISIBLE:View.GONE);
	}
	
	public void setLeftButtonText(String text){
		mBtnOthers.setText(text);
	}
	
	public void setRightButtonText(String text){
		mBtnDelete.setText(text);
	}
	
	public void setRightButtonBackground(int color){
		mBtnDelete.setBackgroundColor(color);
	}

	public void setLeftButtonBackground(int color){
		mBtnOthers.setBackgroundColor(color);
	}
	
	private void init(Context context) {
		mContext = context;
		mDetector = new GestureDetector(mContext, this);
		View contentView = LayoutInflater.from(mContext).inflate(
				R.layout.my_assets_swipeitem, null);
		mFrontView = (LinearLayout)contentView.findViewById(R.id.front);
		mBackOperateView = (LinearLayout)contentView.findViewById(R.id.back_operationview);
		mBackView = (LinearLayout)contentView.findViewById(R.id.back);
		mBtnDelete = (Button)contentView.findViewById(R.id.back_delete);
		mBtnOthers = (Button)contentView.findViewById(R.id.back_other);
		LayoutParams contentParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.addView(contentView, contentParams);
		mFrontView.setOnTouchListener(this);
	}

	public void setOnFrontViewClickListener(OnClickListener onClickListener) {
		mFrontViewClickListener = onClickListener;
		mFrontView.setOnClickListener(onClickListener);
	}

	public void setFrontViewBackgroundColor(int color) {
		mFrontView.setBackgroundResource(color);
	}

	public void setOnBackViewOthersClickListener(OnClickListener othersClickListener) {
		mBtnOthers.setOnClickListener(othersClickListener);
	}

	public void setonBackViewDeleteClickListener(OnClickListener deleteClickListener) {
		mBtnDelete.setOnClickListener(deleteClickListener);
	}

	public void showFrontLeftViewType(int type) {
		if (type == TYPE_TEXT) {
			mTvLeftText.setVisibility(View.VISIBLE);
			mIsFrontLeftClickable=false;
		} else if (type == TYPE_CHECKBOX) {
			mTvLeftText.setVisibility(View.GONE);
			mIsFrontLeftClickable=true;
		} else if (type == TYPE_NONE) {
			mTvLeftText.setVisibility(View.GONE);
			mIsFrontLeftClickable=false;
		}
	}


	public void setBackViewType(BackViewType backViewType) {
		mIsSwipeable = true;
		switch (backViewType) {
		case TYPE_BACK_NONE:
			mIsSwipeable = false;
			break;
		case TYPE_BACK_DELETE:
			mBtnDelete.setVisibility(View.VISIBLE);
			mBtnOthers.setVisibility(View.INVISIBLE);
			break;
		case TYPE_BACK_EDIT:
			mBtnDelete.setVisibility(View.GONE);
			mBtnOthers.setVisibility(View.VISIBLE);
			break;
		case TYPE_BACK_EDIT_DELETE:
			mBtnOthers.setVisibility(View.VISIBLE);
			mBtnDelete.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

//	public void setFrontContent(String content, int enableCount) {
//		if (!TextUtils.isEmpty(content)) {
//			if (enableCount > 0 && content.length() > enableCount) {
//				content = content.substring(0, enableCount).concat("...");
//			}
//			mTvFrontContent.setText(content);
//		}
//	}

	private int mFrontRight, mFrontLeft;
	private OnTouchListener mFrontViewTouchListener = new OnTouchListener() {		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mIsSwipeable) {
				int action = event.getAction();
				if(action==MotionEvent.ACTION_MOVE){
					int frontDx = mLastX - (int) event.getRawX();
					int currentFrontLeft = mFrontLeft - frontDx;
					int currentFrontRight = mFrontRight - frontDx;
					if (currentFrontLeft < 0) {
						mFrontView.layout(currentFrontLeft,
								mFrontView.getTop(), currentFrontRight,
								mFrontView.getBottom());
						getParent().requestDisallowInterceptTouchEvent(true);
					} else {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			}
			return false;
		}
	};
	
	@Override
	public boolean onDown(MotionEvent event) {
		mLastX = (int) event.getRawX();
		mBackViewWidth = mBackOperateView.getWidth();
		mFrontRight = mFrontView.getRight();
		mFrontLeft = mFrontView.getLeft();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		boolean isEventConsumed = false;
		
		if(mFrontViewClickListener!=null) {
				mFrontViewClickListener.onClick(mFrontView);
				isEventConsumed = true;
			
		}
		return isEventConsumed;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(Math.abs(e1.getRawX()-e2.getRawX())>HORIZONTAL_TOUCH_OFFSET){
			mFrontViewTouchListener.onTouch(mFrontView, e2);
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		if(action==MotionEvent.ACTION_UP){
			if(mIsSwipeable){
				int frontDxUp = mLastX - (int) event.getRawX();
				if (frontDxUp > mBackViewWidth / 2
						|| (frontDxUp > 0 && mIsBackViewShow)) {
					mFrontView.layout(-mBackViewWidth, mFrontView.getTop(),
							mBackOperateView.getLeft()+(int)mBackView.getX(), mFrontView.getBottom());
					mIsBackViewShow = true;
				} else if (frontDxUp != 0||frontDxUp<mBackViewWidth) {
					System.out.println("frontDxUp----"+frontDxUp);
					System.out.println("mBackViewWidth----"+mBackViewWidth);
					mFrontView.layout((int)mBackView.getX(), mFrontView.getTop(),
							mBackView.getRight(), mFrontView.getBottom());
					mIsBackViewShow = false;
				}
			}
		}
		return mDetector.onTouchEvent(event);
	}
	
	public boolean isBackShow(){
		return mIsBackViewShow;
	}


}
