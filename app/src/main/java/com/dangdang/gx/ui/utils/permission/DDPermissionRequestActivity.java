package com.dangdang.gx.ui.utils.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.dialog.CommonDialog;
import com.dangdang.gx.ui.utils.ClickUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于接收申请权限结果的透明界面
 * Created by liuzhongtao on 2018/2/28.
 */

public class DDPermissionRequestActivity extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 1513;
    private static final int APP_SETTING_REQUEST_CODE = 1527;
    private static final int MANAGE_UNKNOWN_APP_SOURCES = 10081;
    public static final String REQUEST_UTIL_INDEX = "REQUEST_UTIL_INDEX";

    private DDPermissionUtil util;
    private Dialog showDialog;
    private int requestUtilIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        //NBSAppAgent.leaveBreadcrumb("DDPermissionRequestActivity onCreate");

        if (savedInstanceState != null) {
            requestUtilIndex = savedInstanceState.getInt(REQUEST_UTIL_INDEX);
            //NBSAppAgent.leaveBreadcrumb("get REQUEST_UTIL_INDEX from savedInstanceState");
        } else {
            requestUtilIndex = getIntent().getIntExtra(REQUEST_UTIL_INDEX, 0);
            //NBSAppAgent.leaveBreadcrumb("get REQUEST_UTIL_INDEX from intent");
        }
        util =  DDPermissionUtil.getDDPermissionUtil(requestUtilIndex);

        if (util != null) {
            //NBSAppAgent.leaveBreadcrumb("request permission");
            ActivityCompat.requestPermissions(this, util.getPerms(), PERMISSION_REQUEST_CODE);
        } else {
            //NBSAppAgent.leaveBreadcrumb("util is null, finish");
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(REQUEST_UTIL_INDEX, requestUtilIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void finish() {
        if (util != null) {
            //NBSAppAgent.leaveBreadcrumb("finish clear util");
            util.clear();
            util = null;
        }

        if (showDialog != null) {
            showDialog.dismiss();
        }

        super.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }

        if (util != null) {
            List<String> granted = new ArrayList<>();
            List<String> denied = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted.add(perm);
                } else {
                    denied.add(perm);
                }
            }

            if (denied.size() > 0 && !TextUtils.isEmpty(util.getDeniedTip())) {
                showAppSettingDialog();
            } else {
                if (granted.size() > 0) {
                    util.getCallback().onGranted(granted);
                }

                if (denied.size() > 0) {
                    util.getCallback().onDenied(denied);
                }

                finish();
            }
        }
    }

    private void showAppSettingDialog() {
        final CommonDialog dialog = new CommonDialog(this, R.style.dialog_commonbg);
        if (util.isAppSettingDialogCancelable()) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setLeftButtonText(getString(R.string.cancel));
            dialog.setLeftButtonCancelStyle();
            dialog.setLeftBtnClickAble(true);
            dialog.setOnLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        } else {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.hideLeftButton();
        }

        dialog.setInfo(util.getDeniedTip());

        dialog.setTitleInfo(getString(R.string.request_permission));

        dialog.setRightButtonText(getString(R.string.grant));
        dialog.setOnRightClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickUtil.checkFastClick())
                    return;
                List<String> permList = Arrays.asList(util.getPerms());
                if(permList.contains(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)){//安装 未知来源程序权限
                    //NBSAppAgent.leaveBreadcrumb("goto unknown app source setting");
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES),MANAGE_UNKNOWN_APP_SOURCES);
                }else{
                    //NBSAppAgent.leaveBreadcrumb("goto app setting");
                    startActivityForResult(
                            new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", getPackageName(), null)),
                            APP_SETTING_REQUEST_CODE);
                }
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });

        dialog.show();
        showDialog = dialog;

        //NBSAppAgent.leaveBreadcrumb("show goto setting dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MANAGE_UNKNOWN_APP_SOURCES){//安装未知来源程序权限 重新走安装流程
            util.getCallback().onGranted(null);
        }else{
            //NBSAppAgent.leaveBreadcrumb("onActivityResult");

            // 避免util为空的保护
            if (util == null) {
                util = DDPermissionUtil.getDDPermissionUtil(requestUtilIndex);
            }
            if (util == null) {
                return;
            }

            // 如果用户在设置界面打开所有权限，则回调后关闭页面，否则什么也不做
            if (util.hasPermissions(util.getPerms())) {
                util.getCallback().onGranted(Arrays.asList(util.getPerms()));
                finish();
            }
        }
    }
}
