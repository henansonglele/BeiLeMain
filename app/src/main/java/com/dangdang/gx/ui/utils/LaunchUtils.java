package com.dangdang.gx.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.dangdang.gx.ui.html.StoreNormalHtmlActivity;

public class LaunchUtils {

    public static void launchPresentBookDetail(Context activity, String mPacketId) {

    }

    public static void launchBookDetail(Context activity, String mPacketId) { }
    public static void launchStore(Context activity) { }
    public static void gotoLogin(Context activity) { }
    public static void launchSearchActivity(Context activity,String key) { }
    /**
     * 进入普通h5页面
     */
    public static void launchStoreNormalHtmlActivity(Activity ac, String title, String url) {
        if (ac == null || TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(ac, StoreNormalHtmlActivity.class);
        intent.putExtra(StoreNormalHtmlActivity.EXTRA_TITLE, title);
        intent.putExtra(StoreNormalHtmlActivity.EXTRA_HTML_PATH, url);
        ac.startActivity(intent);
    }

    public static void launchSmsLoginActivity(Context context) {

    }
}
