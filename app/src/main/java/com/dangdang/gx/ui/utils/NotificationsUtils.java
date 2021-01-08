package com.dangdang.gx.ui.utils;

/**
 * Created by songxile on 18/5/30.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.app.NotificationManagerCompat;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.dialog.CommonDialog;

import static android.view.Gravity.CENTER;

/**
 * 获取通知栏权限是否开启
 */

public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";


    public static boolean isNotificationEnable(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
    //在Android 4.4（API19）以下是没有AppOpsManager类，无法获取通知栏开启状态
    public static boolean canGetNotificationStatus() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }



    private static void showNotificationSetDialog(final Context context) {
        final CommonDialog dialogBuilder = new CommonDialog(context,
                R.style.dialog_commonbg);
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.setCancelable(true);

        Window window = dialogBuilder.getWindow();
        window.setGravity(CENTER);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = CENTER;
        window.setAttributes(lp);
        dialogBuilder.onWindowAttributesChanged(lp);

        dialogBuilder.hideTitle();
        dialogBuilder.setInfo("亲，系统默认关闭了消息推送，请在系统“设置”-“通知中心”-“当当云阅读”中设置，以免错过相关提醒或福利通知哦！");

        dialogBuilder.setRightButtonText("去设置");
        dialogBuilder.setLeftButtonText("知道了");

        dialogBuilder.setOnRightClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickUtil.checkFastClick())
                    return;
                dialogBuilder.dismiss();
                goToSet(context);
            }
        });

        dialogBuilder.setOnLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.show();
    }

    public static void goToSet(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }
}
