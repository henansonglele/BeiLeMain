package com.dangdang.gx.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public abstract class LoadingView {
	
	protected Context mContext;
	protected int mMsgId = -1;

	protected View mLoadingView;
	protected TextView mMessageTV;
	
	public LoadingView(Context context){
		mContext = context;
		init(context);
	}
	
	public void setMessage(int id) {
		if(mMessageTV == null)
			return;
		if (id > 0) {
			mMsgId = id;
			String message = mContext.getResources().getString(id);
			if (!TextUtils.isEmpty(message)) {
				mMessageTV.setText(message);
				mMessageTV.setVisibility(View.VISIBLE);
				return;
			}
		}
		mMessageTV.setVisibility(View.GONE);
	}
	
	public int getMessageId(){
		return mMsgId;
	}
	
	public View getLoadingView() {
		return mLoadingView;
	}

	protected abstract void init(Context context);
	
	public abstract void reset();	
	
}
