package com.dangdang.gx.ui.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.dangdang.gx.ui.log.LogM;
import java.util.Stack;

/**
 */
public class ForegroundV2 implements Application.ActivityLifecycleCallbacks {
    private static ForegroundV2 sInstance;
    private boolean mIsForeground;
    private Stack<Activity> mActivityStack;

    private ForegroundV2() {
        mActivityStack = new Stack<Activity>();
    }

    public static final void init(Application app) {
        if (sInstance == null) {
            sInstance = new ForegroundV2();
            app.registerActivityLifecycleCallbacks(sInstance);
        }
    }

    public static final ForegroundV2 getInstance() {
        return sInstance;
    }


    public boolean isOnForeground() {
        return mIsForeground;
    }

    private void notifyToForeground() {//app进入前台
        //EventBus.getDefault().postSticky(new OnAppBecomeForegroundEvent());

    }

    private void notifyToBackground() {//app进入后台
        //EventBus.getDefault().postSticky(new OnAppBecomeBackgroundEvent());
    }

    public Activity popTopActivity() {
        if (mActivityStack.size() <= 0)
            return null;
        LogM.d("whm", "popTopActivity------topActivity is: " + mActivityStack.peek().getClass().getSimpleName());
        return mActivityStack.pop();
    }

    public Activity getTopActivity() {
        if (mActivityStack.size() <= 0)
            return null;
        LogM.d("whm", "getTopActivity-------topActivity is: " + mActivityStack.peek().getClass().getSimpleName());
        return mActivityStack.peek();
    }

    public Activity getSecondActivity() {
        int length = mActivityStack.size();
        if ( length > 1)
            return mActivityStack.get(length -2);
        return null;
    }
    public Activity getThirdActivity() {
        int length = mActivityStack.size();
        if ( length > 2)
            return mActivityStack.get(length - 3);
        return null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }
    @Override
    public void onActivityStarted(Activity activity) {
        LogM.d("whm", activity.getClass().getSimpleName() + "started");
        if (!mIsForeground) {
            mIsForeground = true;
            notifyToForeground();
        }
        mActivityStack.push(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogM.d("whm", activity.getClass().getSimpleName() + "stopped");
        mActivityStack.remove(activity);
        if ((mActivityStack.isEmpty()) && (!activity.isChangingConfigurations())) {
            mIsForeground = false;
            notifyToBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}
