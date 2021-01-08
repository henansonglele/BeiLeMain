package com.dangdang.gx.ui.utils.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

/**
 * 用于App内的权限管理，包括权限的申请，提示弹框等
 * Created by liuzhongtao on 2018/2/28.
 */

public class DDPermissionUtil {
    private static SparseArray<DDPermissionUtil> utilMap = new SparseArray<>();
    private final static String[] mustPermissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final static String[] mustPermissionsQ = { Manifest.permission.WRITE_EXTERNAL_STORAGE};//android 10以上 不获取电话状态权限

    private Context context;
    private String deniedTip = null;
    private String[] perms = null;
    private boolean appSettingDialogCancelable = true;
    private DDPermissionRequestCallback callback = null;

    static DDPermissionUtil getDDPermissionUtil(int index) {
        return utilMap.get(index);
    }

    public DDPermissionUtil(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Can't check permissions for null context");
        }

        this.context = context;
    }

    public interface DDPermissionRequestCallback {
        /**
         * 允许的权限
         * @param list perms
         */
        void onGranted(List<String> list);

        /**
         * 拒绝的权限
         * @param deniedList  单次拒绝
         */
        void onDenied(List<String> deniedList);
    }

    /**
     * 申请指定的权限
     * @param callback 返回结果
     */
    public void requestPermissions(DDPermissionRequestCallback callback) {
        if (callback == null) {
            return;
        }

        // 如果已有所有权限，直接回调
        if (hasPermissions(perms)) {
            callback.onGranted(Arrays.asList(perms));
            return;
        }

        this.callback = callback;
        utilMap.append(this.hashCode(), this);

        Intent intent = new Intent(context, DDPermissionRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DDPermissionRequestActivity.REQUEST_UTIL_INDEX, this.hashCode());
        context.startActivity(intent);
    }

    /**
     * 申请必须的权限
     */
    public void requestMustPermissions(DDPermissionRequestCallback callback) {
        if(Build.VERSION.SDK_INT >= 29){//android 10以上 不获取电话状态权限
            setPerms(mustPermissionsQ);
        }
        else{
            setPerms(mustPermissions);
        }
//        setDeniedTip(context.getString(R.string.start_permission_tip));
        setAppSettingDialogCancelable(false);
        requestPermissions(callback);

    }

    void clear() {
        utilMap.delete(this.hashCode());
    }

    public void setDeniedTip(String deniedTip) {
        this.deniedTip = deniedTip;
    }

    String getDeniedTip() {
        return deniedTip;
    }

    public void setPerms(String... perms) {
        this.perms = perms;
    }

    public String[] getPerms() {
        return perms;
    }

    boolean isAppSettingDialogCancelable() {
        return appSettingDialogCancelable;
    }

    /**
     * 提示跳转设置对话框是否可取消，默认可以，只有app启动时设置为不可以
     * @param appSettingDialogCancelable t/f
     */
    public void setAppSettingDialogCancelable(boolean appSettingDialogCancelable) {
        this.appSettingDialogCancelable = appSettingDialogCancelable;
    }

    DDPermissionRequestCallback getCallback() {
        return callback;
    }

    /**
     * 检查是否所有权限都存在
     * @param perms 权限列表
     * @return bool
     */
    public boolean hasPermissions(@Size(min = 1) @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查必须的权限是否存在
     * @return bool
     */
    public boolean hasMustPermissions() {
        if(Build.VERSION.SDK_INT >= 29){//android 10以上 不获取电话状态权限
            return hasPermissions(mustPermissionsQ);
        }
        else{
            return hasPermissions(mustPermissions);
        }
    }

//    /**
//     * 被拒绝的权限是否选择不再提示
//     * @param ac activity
//     * @param perm 权限
//     * @return bool
//     */
//    public boolean permissionPermanentlyDenied(Activity ac, @NonNull String perm) {
//        return !ActivityCompat.shouldShowRequestPermissionRationale(ac, perm);
//    }
}