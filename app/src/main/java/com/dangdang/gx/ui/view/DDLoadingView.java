package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DDLoadingView extends LoadingView {

	private ImageView mImage;
	private AnimationDrawable animationDrawable;
	
	public DDLoadingView(Context context) {
		super(context);
	}

	@Override
	protected void init(Context context) {
		mLoadingView = new RelativeLayout(context);
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		l.setGravity(Gravity.CENTER);
		mImage = new ImageView(context);
		l.addView(mImage);
		mMessageTV = new TextView(context);
		l.addView(mMessageTV);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		((ViewGroup)mLoadingView).addView(l, params);
		
		animationDrawable = (AnimationDrawable) mImage.getBackground();
		if(animationDrawable != null)
			animationDrawable.start();
		
		ViewGroup.LayoutParams param = mLoadingView.getLayoutParams();
		if(param != null){
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			param.width = dm.widthPixels;
			param.height = dm.heightPixels;
			mLoadingView.setLayoutParams(param);
		}
		mLoadingView.setClickable(true);
	}

	@Override
	public void reset() {
		if(animationDrawable != null)
			animationDrawable.stop();
		animationDrawable = null;
	}
}
