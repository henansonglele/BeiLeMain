/**
 *
 */
package com.dangdang.gx.ui.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dangdang.gx.R;
import com.dangdang.gx.ui.view.MyProgressLoadingView;


/**
 * @author wanghaiming
 */
public class SimpleProgressDialogUtil {
    private static Dialog sProgressDialog;


    public static void show(Context context, CharSequence message) {
        show(context, message, true);
    }

    public static void show(Context context, CharSequence message, boolean cancelable) {
        show(context, message, cancelable, null);
    }

    public static void show(Context context, CharSequence message, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        if (context == null)
            return;

        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            sProgressDialog = null;
            return;
        }

        if (sProgressDialog != null) {
            sProgressDialog.dismiss();
            sProgressDialog = null;
        }

        sProgressDialog = new Dialog(context, R.style.dialog_commonbg);
        sProgressDialog.setContentView(getContentView(context, 0, message));
        sProgressDialog.setCancelable(cancelable);
        sProgressDialog.setOnCancelListener(cancelListener);
        sProgressDialog.show();
    }

    public static void dismiss() {
        try {
            if (sProgressDialog != null && sProgressDialog.isShowing()) {
                Context context = sProgressDialog.getContext();
                if (context instanceof Activity && ((Activity) context).isFinishing()) {
                    return;
                }

                sProgressDialog.dismiss();
                sProgressDialog = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static View getContentView(Context context, int drawableId, CharSequence message) {
        MyProgressLoadingView contentView = new MyProgressLoadingView(context);

        return contentView.getLoadingView();
    }
}
