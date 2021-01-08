package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.utils.UiUtil;

public class ProgressLoadingLayout extends LoadingLayout {

	private ImageView mImage;
	private ProgressBar mBar;
	private RotateAnimation animation, reAnimation;
	
	public ProgressLoadingLayout(Context context, int mode,
			String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context, mode, releaseLabel, pullLabel, refreshingLabel);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init(Context context, int mode, String releaseLabel,
			String pullLabel, String refreshingLabel) {
		
		mLoadingView = new RelativeLayout(context);

		mPromptView = new TextView(context);
		mPromptView.setTextColor(0xff969696);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		int tmp = UiUtil.dip2px(context, 10);
		params.setMargins(tmp, 0, tmp, 0);
		mPromptView.setId(R.id.textview);
		((ViewGroup)mLoadingView).addView(mPromptView, params);
		
		mImage = new ImageView(context);
		mImage.setScaleType(ScaleType.CENTER_INSIDE);
		tmp = UiUtil.dip2px(context, 30);
		params = new RelativeLayout.LayoutParams(tmp, tmp);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.LEFT_OF, R.id.textview);
		((ViewGroup)mLoadingView).addView(mImage, params);
		Bitmap bitmap = null;
		if(mode == PullToRefreshBase.MODE_PULL_UP_TO_REFRESH)
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pulltorefresh_up_arrow);	
		else
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pulltorefresh_down_arrow);	
		mImage.setImageBitmap(bitmap);
	
		mBar = new ProgressBar(context);
		params = new RelativeLayout.LayoutParams(tmp, tmp);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.RIGHT_OF, R.id.textview);
		((ViewGroup)mLoadingView).addView(mBar, params);
		
		mRefreshingLabel = refreshingLabel;
		mReleaseLabel = releaseLabel;
		mPullLabel = pullLabel;
		
		mBar.setVisibility(View.GONE);			
		
		ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.dip2px(context, 60));
		this.addView(mLoadingView, param);
	}

	private RotateAnimation getRotateAnimation(){
		if(animation == null){
			animation = new RotateAnimation(0f,180f,Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
			animation.setDuration(500);
			animation.setFillAfter(true);
		}
		return animation;
	}
	
	private RotateAnimation getResetAnimation(){
		if(reAnimation == null){
			reAnimation = new RotateAnimation(180f,0f,Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
			reAnimation.setDuration(500);
			reAnimation.setFillAfter(false);
		}
		return reAnimation;
	}
	
	@Override
	public void setRefreshValid(int mode) {
	}
	
	@Override
	public void releaseToRefresh(){
		super.releaseToRefresh();
		mImage.startAnimation(getRotateAnimation());
	}
	
	@Override
	public void pullToRefresh(){
		super.pullToRefresh();
		mImage.startAnimation(getResetAnimation());
	}
	
	@Override
	public void refreshing() {
		super.refreshing();
		mImage.setVisibility(View.GONE);
		mImage.clearAnimation();
		mBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void reset(){
		super.reset();
		mImage.setVisibility(View.VISIBLE);
		mImage.clearAnimation();
		mBar.setVisibility(View.GONE);
	}
}
