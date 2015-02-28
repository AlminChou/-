package com.example.almin.widget;



import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class PinnedHeaderExpandableListView extends ExpandableListView implements OnScrollListener,OnGroupClickListener {
	public PinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		registerListener();
	}

	public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		registerListener();
	}

	public PinnedHeaderExpandableListView(Context context) {
		super(context);
		registerListener();
	}


	public interface HeaderAdapter {
		public static final int PINNED_HEADER_GONE = 0;
		public static final int PINNED_HEADER_VISIBLE = 1;
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		int getHeaderState(int groupPosition, int childPosition);


		void configureHeader(View header, int groupPosition,int childPosition, int alpha);


		void setGroupClickStatus(int groupPosition, int status);


		int getGroupClickStatus(int groupPosition);

	}

	private static final int MAX_ALPHA = 255;

	private HeaderAdapter mAdapter;


	private View mHeaderView;

	private int mOnClickMinLength;

	private boolean mHeaderViewVisible;

	private int mHeaderViewWidth;

	private int mHeaderViewHeight;


	public void setHeaderView(View view) {
		if(view!=null){
			mHeaderView = view;
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(lp);
			setFadingEdgeLength(0);

			requestLayout();
		}
	}

	private void registerListener() {
		setOnScrollListener(this);
		setOnGroupClickListener(this);
	}
	
	 /** 
     * 点击 HeaderView 触发的事件 
     */  
	private void headerViewClick() {
		long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());  
        
        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);  
          
        if (mAdapter.getGroupClickStatus(groupPosition) == 1) {  
            this.collapseGroup(groupPosition);  
            mAdapter.setGroupClickStatus(groupPosition, 0);  
        }  
        else{  
            this.expandGroup(groupPosition);  
            mAdapter.setGroupClickStatus(groupPosition, 1);  
        }  
          
        this.setSelectedGroup(groupPosition); 
	}

	private float mDownX;
	private float mDownY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mHeaderViewVisible) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				float x = ev.getX();
				float y = ev.getY();
				float offsetX = Math.abs(x - mDownX);
				float offsetY = Math.abs(y - mDownY);
				if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
						&& offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight&&x>mOnClickMinLength&&offsetX<mOnClickMinLength) {
					if (mHeaderView != null) {
						headerViewClick();
					}

					return true;
				}
				break;
			default:
				break;
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mHeaderViewVisible) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
					return true;
				}
				break;

			default:
				break;
			}
		}

		return super.onTouchEvent(ev);

	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (HeaderAdapter) adapter;
	}


	@Override
	public boolean onGroupClick(ExpandableListView parent,View v,int groupPosition,long id) {
//		return true;
		  if (mAdapter.getGroupClickStatus(groupPosition) == 0) {  
	            mAdapter.setGroupClickStatus(groupPosition, 1);  
	            parent.expandGroup(groupPosition);  
	            //Header自动置顶  
	            //parent.setSelectedGroup(groupPosition);  
	              
	        } else if (mAdapter.getGroupClickStatus(groupPosition) == 1) {  
	            mAdapter.setGroupClickStatus(groupPosition, 0);  
	            parent.collapseGroup(groupPosition);  
	        }  
		  return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {  
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);  
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();  
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();  
		}  
	}

	private int mOldState = -1;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
		if(mAdapter!=null){
			int state = mAdapter.getHeaderState(groupPos, childPos);
			if (mHeaderView != null && mAdapter != null && state != mOldState) {
				mOldState = state;
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
		}

		configureHeaderView(groupPos, childPos);
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mAdapter == null
				|| ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
			return;
		}
		mHeaderView.setTag(groupPosition);
		int state = mAdapter.getHeaderState(groupPosition, childPosition);

		switch (state) {
		case HeaderAdapter.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case HeaderAdapter.PINNED_HEADER_VISIBLE: {
			mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, MAX_ALPHA);

			if (mHeaderView.getTop() != 0){
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			mHeaderViewVisible = true;

			break;
		}

		case HeaderAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();

			int headerHeight = mHeaderView.getHeight();

			int y;

			int alpha;

			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}

			mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, alpha);

			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
			}

			mHeaderViewVisible = true;
			break;
		}
		}
	}

	@Override

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

		configureHeaderView(groupPosition, childPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}