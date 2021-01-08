package com.dangdang.gx.ui.view;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Iterator;
import java.util.List;


public class DDWebView extends WebView {
    Context mContext;

    public DDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public DDWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init(context);
    }

    public DDWebView(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

    };

    private boolean isAccessibilityInjectionEnabled() {
        AccessibilityManager manager = (AccessibilityManager) this.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return false;
        } else {
            List<AccessibilityServiceInfo> services = manager.getEnabledAccessibilityServiceList(1);
            return !services.isEmpty();
        }
    }

    protected boolean isTTsAvilible() {
        boolean bo = this.isAccessibilityInjectionEnabled();
        return bo ? true : this.isServiceWork(this.getContext(), "com.google.android.marvin.talkback.TalkBackService");
    }

    protected boolean isAppsInstall(List<String> list) {
        Iterator var2 = list.iterator();

        String str;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            str = (String) var2.next();
        } while (!this.isAppInstall(str));

        return true;
    }

    protected boolean isAppInstall(String name) {
        PackageManager packageManager = this.getContext().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);

        for (int i = 0; i < pinfo.size(); ++i) {
            if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(2147483647);
        if (myList.size() <= 0) {
            return false;
        } else {
            for (int i = 0; i < myList.size(); ++i) {
                String mName = ((ActivityManager.RunningServiceInfo) myList.get(i)).service.getClassName().toString();
                if (mName.equals(serviceName)) {
                    isWork = true;
                    break;
                }
            }

            return isWork;
        }
    }

    private void init(Context context) {
        //this.setWebViewClient(this.client);
        this.setWebChromeClient(this.chromeClient);
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        this.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                return true;
            }
        });

    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        if (!this.isTTsAvilible()) {
            super.setOnLongClickListener(listener);
        }
    }

    public void destroy() {
        try {
            this.removeAllViews();
            if (this != null) {
                this.setOnLongClickListener((View.OnLongClickListener) null);
                this.setWebChromeClient((WebChromeClient) null);
                this.setWebViewClient((WebViewClient) null);
                this.removeAllViews();
                //this.destroy();
                //this = null;
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void clear() {
    }

    public int getContentHeight() {
        return 5 + super.getContentHeight();
    }

    public WebView getWebView() {
        return this;
    }

    public void setShowProgressBar(boolean show) {
        //this.isShow = show;
        //if(!this.isShow) {
        //    this.progressbar.setVisibility(8);
        //}

    }

    //
    //public boolean isShowProgressBar() {
    //    return this.isShow;
    //}
    public ProgressBar getProgressBar() {
        return new ProgressBar(mContext);
    }
}
