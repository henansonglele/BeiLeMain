package com.dangdang.gx.ui.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgressLoadingView extends LoadingView {

	public ProgressLoadingView(Context context) {
		super(context);
	}

	@Override
	protected void init(Context context) {
		// TODO Auto-generated method stub
		mLoadingView = new RelativeLayout(context);
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		l.setGravity(Gravity.CENTER);
		ProgressBar bar = new ProgressBar(context);
		l.addView(bar);
		mMessageTV = new TextView(context);
		mMessageTV.setVisibility(View.GONE);
		l.addView(mMessageTV);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		((ViewGroup)mLoadingView).addView(l, params);
		
		ViewGroup.LayoutParams param = mLoadingView.getLayoutParams();
		if(param != null){
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			param.width = dm.widthPixels;
			param.height = dm.heightPixels;
			mLoadingView.setLayoutParams(param);
		}
		mLoadingView.setBackgroundColor(0xaa000000);
		mLoadingView.setClickable(true);
	}

	@Override
	public void reset() {
		
	}
}
