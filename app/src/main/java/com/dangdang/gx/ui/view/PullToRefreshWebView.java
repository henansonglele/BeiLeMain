package com.dangdang.gx.ui.view;

import android.content.Context;
import android.util.AttributeSet;

public class PullToRefreshWebView extends PullToRefreshBase<DDWebView> {

	public PullToRefreshWebView(Context context) {
		super(context);

		// init(listener);
	}

	public PullToRefreshWebView(Context context, int mode) {
		super(context, mode);

		// init(listener);
	}

	public PullToRefreshWebView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// init(listener);
	}

	public void init(OnRefreshListener listener) {
		setOnRefreshListener(listener);
	}

	@Override
	protected DDWebView createRefreshableView(Context context, AttributeSet attrs) {
		DDWebView webView = new DDWebView(context, attrs);
		// webView.setBackgroundColor(0xFFECECEC);
		// webView.getSettings().setJavaScriptEnabled(true);
		// webView.setWebViewClient(new SampleWebViewClient());
		// webView.setId(R.id.webview);

		return webView;
	}
	
	protected LoadingLayout getDownLoadingLayout(Context context) {
		String pullLabel = "";//context.getString(R.string.pull_to_refresh_pull_label);
		String releaseLabel = "";//context.getString(R.string.pull_to_refresh_release_label);
		String refreshingLabel = "";//context.getString(R.string.pull_to_refresh_refreshing_label);
		return new RoundLoadingLayout(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel);
	}

	@Override
	protected boolean isReadyForPullDown() {
		int y;
		if(refreshableView instanceof DDWebView)
			y = ((DDWebView)refreshableView).getWebView().getScrollY();
		else
			y = refreshableView.getScrollY();
		return (y == 0) && isPullToRefreshEnabled();
	}

	@Override
	protected boolean isReadyForPullUp() {
		int y;
		if(refreshableView instanceof DDWebView)
			y = ((DDWebView)refreshableView).getWebView().getScrollY();
		else
			y = refreshableView.getScrollY();
		return (y >= (refreshableView
				.getContentHeight() - refreshableView.getHeight()))
				&& isPullUpToRefreshEnabled();
	}

	public void showLoading() {
		getHeaderLayout().setVisibility(VISIBLE);
		((RoundLoadingLayout)getHeaderLayout()).releaseToRefresh();
		((RoundLoadingLayout)getHeaderLayout()).refreshing();
	}
}
