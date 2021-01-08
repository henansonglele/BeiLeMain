package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dangdang.gx.R;


public class MyProgressLoadingView extends ProgressLoadingView {

	private ProgressBar mBar;

	public MyProgressLoadingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init(Context context) {
		context=context.getApplicationContext();
		mLoadingView = new RelativeLayout(context);

        LinearLayout l = (LinearLayout) View.inflate(context, R.layout.my_progress_loading_view, null);

        mBar = (ProgressBar) l.findViewById(R.id.my_progress_loading_view_bar);
		mMessageTV = new TextView(context);
		mMessageTV.setVisibility(View.GONE);
		l.addView(mMessageTV);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
		((ViewGroup)mLoadingView).addView(l, params);
		
		ViewGroup.LayoutParams param = mLoadingView.getLayoutParams();
        if (param != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            param.width = dm.widthPixels;
            param.height = dm.heightPixels;
            mLoadingView.setLayoutParams(param);
        }
        mLoadingView.setBackgroundColor(Color.TRANSPARENT);
		mLoadingView.setClickable(true);
    }

	@Override
	public void reset() {
		mBar.clearAnimation();
	}
}
