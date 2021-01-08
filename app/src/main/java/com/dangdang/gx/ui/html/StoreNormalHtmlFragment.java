package com.dangdang.gx.ui.html;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.html.basehtml.BaseReaderHtmlFragment;
import com.dangdang.gx.ui.utils.NetUtil;
import com.dangdang.gx.ui.utils.NetUtils;
import com.dangdang.gx.ui.utils.UiUtil;
import com.dangdang.gx.ui.view.PullToRefreshBase;
import com.dangdang.gx.ui.view.PullToRefreshWebView;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 普通html fragment
 */
public class StoreNormalHtmlFragment extends BaseReaderHtmlFragment {

    /**
     * view
     */
    private PullToRefreshWebView mPullToRefreshWebView;
    private RelativeLayout mStoreRl;

    /**
     * data
     */
    private String mHtmlPath;
    private boolean mIsInitView;        // 是否初始化View，防止没初始化View就加载数据
    private boolean mIsLoaded;          // 是否已加载数据，防止多次加载
    private boolean mRefreshState;      // 当前页面浏览器是否把js下载下来

    private Timer mRefreshTimer;
    private TimerTask mRefreshTimerTask;
    private final static int MSG_WHAT_RESET_REFRESH_STATE = 123;
    private View  mRootView;

    public static StoreNormalHtmlFragment getInstance(String htmlPath) {
        StoreNormalHtmlFragment f = new StoreNormalHtmlFragment();
        Bundle args = new Bundle();
        args.putString(StoreNormalHtmlActivity.EXTRA_HTML_PATH, htmlPath);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null) {
            mHtmlPath = args.getString(StoreNormalHtmlActivity.EXTRA_HTML_PATH);
        } else {
            mHtmlPath = getActivity().getIntent().getStringExtra(StoreNormalHtmlActivity.EXTRA_HTML_PATH);
        }
    }

    public View onCreateViewImpl(LayoutInflater inflate, ViewGroup container,
                                 Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflate.inflate(
                    R.layout.store_nornal_html_fragment, null);
            findView();
            initUi();
            if (NetUtils.checkNetwork(getActivity())) {
                requestData();
            } else {
                UiUtil.showToast(getActivity(), R.string.error_no_net);
                showError();
            }

        } else {
            if (mRootView.getParent() != null)
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
//        EventBus.getDefault().register(this);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void findView() {
        mStoreRl = (RelativeLayout) mRootView.findViewById(R.id.storeRl);
        mPullToRefreshWebView = (PullToRefreshWebView) mRootView
                .findViewById(R.id.webView);
    }

    private void initUi() {
        mIsInitView = true;

        mPullToRefreshWebView.init(mOnRefreshListener);
        mWebView = mPullToRefreshWebView.getRefreshableView();
        initWebView();
        initWebViewClient();
        mWebView.setWebViewClient(mWebViewClient);
    }

    @Override
    public void onRetryClick() {

        getHtmlData();

        super.onRetryClick();
    }

    private void showError() {
        mIsLoaded = false;
        if (NetUtil.isNetworkConnected()) {
            showErrorView(mStoreRl, R.drawable.icon_error_server,
                    R.string.can_not_find_page_tip, R.string.refresh,null,0);
        } else {
            showErrorView(mStoreRl, R.drawable.icon_error_no_net,
                    R.string.no_net_tip, R.string.refresh,null,0);
        }
    }

    private void requestData() {
        if (!mIsInitView) {
            return;
        }
        if (mIsLoaded) {
            return;
        }

        mPullToRefreshWebView.setVisibility(View.VISIBLE);

        getHtmlData();
    }

    @Override
    protected String getHtmlUrl() {
        return mHtmlPath;
    }

    private PullToRefreshBase.OnRefreshListener mOnRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onPullDownRefresh() {
            if (NetUtils.checkNetwork(getActivity())) {
                if (mRefreshState) {
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.loadUrl("javascript:refresh()");
                        }
                    });
                } else {
                    getHtmlData();
                }

                mRefreshTimer = new Timer();
                mRefreshTimerTask = new TimerTask() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_WHAT_RESET_REFRESH_STATE);
                    }
                };
                mRefreshTimer.schedule(mRefreshTimerTask, 5 * 1000);

            } else {
                showToast(getResources().getString(R.string.error_no_net));
                mPullToRefreshWebView.onRefreshComplete();
            }
        }

        @Override
        public void onPullUpRefresh() {
        }
    };

    public void stopPullRefresh() {
        mPullToRefreshWebView.setPullToRefreshEnabled(false);
        mPullToRefreshWebView.setPullUpToRefreshEnabled(false);
    }

    //允许下拉刷新 （h5控制）
    public void enablePullRefresh() {
        mPullToRefreshWebView.setPullToRefreshEnabled(true);
    }


    private WebViewClient mWebViewClient;

    private void initWebViewClient() {
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (NetUtils.checkNetwork(getActivity())) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    showError();
                }
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    hideGifLoadingByUi(mRootView);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    hideGifLoadingByUi(mRootView);
                }
                showError();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        };
    }


    public void onDestroyImpl() {

        try {
            if (mWebView != null) {
                mWebView.setOnLongClickListener(null);
                mWebView.setWebChromeClient(null);
                mWebView.setWebViewClient(null);
                if (mWebView.getParent() != null)
                    ((ViewGroup) mWebView.getParent()).removeView(mWebView);
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mJsHandle != null) {
            mJsHandle.setJsHandle(null);
            mJsHandle = null;
        }
        if (mHandler != null) {
            mHandler.removeMessages(MSG_WHAT_RESET_REFRESH_STATE);
            mHandler = null;
        }
        resetRefreshState();
//        EventBus.getDefault().unregister(this);

    }

    private void resetRefreshState() {
        if (mRefreshTimerTask != null) {
            mRefreshTimerTask.cancel();
            mRefreshTimerTask = null;
        }
        if (mRefreshTimer != null) {
            mRefreshTimer.cancel();
            mRefreshTimer = null;
        }
    }

    @Override
    public String localStorageImg(String srcImgUrl) {
        return null;
    }

    @Override
    public void refreshFinished(boolean isFinish) {
        mPullToRefreshWebView.onRefreshComplete();
        if (isFinish) {
            resetRefreshState();
        } else {
            showToast("刷新失败");
        }
    }

    @Override
    public int checkSupport(String callbackMehtodName) {
        return 0;
    }

    @Override
    public void refreshState(boolean state) {
        mRefreshState = state;
    }

    protected void dealMsg(Message msg) {
        if (msg.what == MSG_WHAT_RESET_REFRESH_STATE) {
            resetRefreshState();
            mPullToRefreshWebView.onRefreshComplete();
        } else
            super.dealMsg(msg);
    }




}
