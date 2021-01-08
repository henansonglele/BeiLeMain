package com.dangdang.gx.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class LoadingLayout extends FrameLayout{

	protected View mLoadingView;
	protected String mReleaseLabel;
	protected TextView mPromptView;
	protected String mPullLabel;
	protected String mRefreshingLabel;
	
	public LoadingLayout(Context context, final int mode, String releaseLabel,
			String pullLabel, String refreshingLabel) {
		super(context);
		init(context, mode, releaseLabel, pullLabel, refreshingLabel);
		pullToRefresh();
	}
	
	protected abstract void init(Context context, final int mode, String releaseLabel,
			String pullLabel, String refreshingLabel);
	
	public abstract void setRefreshValid(int mode); 
	
	public void setReleaseLabel(String releaseLabel) {
		this.mReleaseLabel = releaseLabel;
	}
	
	public void scrollPullHeader(int current, int max){
		
	}
	
	public void releaseToRefresh() {
		mPromptView.setText(mReleaseLabel);
	}

	public void reset() {
		mPromptView.setText(mPullLabel);
	}

	public void refreshing() {
		mPromptView.setText(mRefreshingLabel);
	}
	

	public void setPullLabel(String pullLabel) {
		this.mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(String refreshingLabel) {
		this.mRefreshingLabel = refreshingLabel;
	}

	public void pullToRefresh() {
		mPromptView.setText(mPullLabel);
	}

	public void setTextColor(int color) {
		mPromptView.setTextColor(color);
	}
	
	public void setLoadingBackgroundColor(int color) {
		if (mLoadingView != null)
			mLoadingView.setBackgroundColor(color);
	}

}
