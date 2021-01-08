package com.dangdang.gx.ui.login.onelogin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dangdang.gx.R;
import com.dangdang.gx.ui.utils.LaunchUtils;
import com.dangdang.gx.ui.utils.UiUtil;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jiguang.verifysdk.api.VerifyListener;
import io.reactivex.Observable;

// 一键登录
public class OneLoginManager {

    public static final int ONE_LOGIN_JG = 1;
    public static final int ONE_LOGIN_MD = 2;
    public static final int DEFAULT_LOGIN = 200;

    public static final int VERIFY_CONSISTENT = 9000;//手机号验证一致
    public static final int FETCH_TOKEN_SUCCESS = 2000;//获取token成功
    public static final int CODE_LOGIN_SUCCESS = 6000;
    public static final int CODE_LOGIN_FAILED = 6001;
    public static final int CODE_LOGIN_CANCELD = 6002;

    public static final String TAG = "oneLogin";
    private int mLoginRoute = DEFAULT_LOGIN;

    private static class SingletonInstance {
        private static final OneLoginManager INSTANCE = new OneLoginManager();
    }

    public static OneLoginManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    // 极光初始化
    public void initJG(Context context) {
//        JVerificationInterface.setDebugMode(true);
        JVerificationInterface.init(context, new RequestCallback<String>() {
            @Override
            public void onResult(int code, String msg) {
                Log.d(TAG, "code = " + code + " msg = " + msg);
            }
        });

    }

    // 极光预取号，验证当前运营商网络是否可以进行一键登录操作
    public void jgPreLogin(Context context, PreLoginListener listener) {
        JVerificationInterface.preLogin(context, 5000, listener);
    }

    // 极光登录
    public void jgLogin(Context context, VerifyListener listener) {
        JVerificationInterface.loginAuth(context, listener);
    }

    public void jgLogin(Context context) {
        JVerificationInterface.setCustomUIWithConfig(getFullScreenPortraitConfig(context));
        JVerificationInterface.loginAuth(context, new VerifyListener() {
            @Override
            public void onResult(final int code, final String token, String operator) {
                Log.e(TAG, "onResult: code=" + code + ",token=" + token + ",operator=" + operator);
                final String errorMsg = "operator=" + operator + ",code=" + code + "\ncontent=" + token;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        SimpleProgressDialogUtil.dismiss();
//                        if (code == CODE_LOGIN_SUCCESS) {
//                            LoginUtil util = new LoginUtil((Activity) context);
//                            util.setLoginListener(new LoginUtil.LoginListener() {
//                                @Override
//                                public void onLoginSuccess(DangUserInfo info) {
//                                    JVerificationInterface.dismissLoginAuthActivity();
//                                    UiUtil.showToast(context, "登录成功");
//                                    new AccountManager(context).updateUserInfo(info);
//                                }
//
//                                @Override
//                                public void onLoginFail(String result, String code) {
//                                    UiUtil.showToast(context, "一键登录失败，请使用其他方式登录");
//                                    JVerificationInterface.dismissLoginAuthActivity();
//                                    LaunchUtils.launchSmsLoginActivity(context);
//                                }
//                            });
//                            util.oneClickLogin(token, 1, null, null);
//                        } else if(code != CODE_LOGIN_CANCELD){
//                            JVerificationInterface.dismissLoginAuthActivity();
//                            LaunchUtils.launchSmsLoginActivity(context);
//                        }
                    }
                });
            }
        });
    }

    private JVerifyUIConfig getFullScreenPortraitConfig(Context context) {
        return new JVerifyUIConfig.Builder()
                .setNavColor(0xffffffff)
//                .setNavText("一键登录")
                .setNavTextColor(0xff444444)
                .setNavReturnImgPath("close_gray")
                .setLogoWidth(80)
                .setLogoHeight(80)
                .setLogoHidden(false)
                .setLogoOffsetY(60)
                .setLogoImgPath("logo")
                .setNumberColor(0xff393939)
                .setNumberSize(24)
                .setNumFieldOffsetY(225)
                .setLogBtnHeight(50)
                .setLogBtnText("一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnTextSize(18)
                .setLogBtnImgPath("login_btn_green")
                .setAppPrivacyOne("《用户许可协议》", "")
                .setAppPrivacyTwo("《隐私政策》", "")
                .setAppPrivacyColor(0xffc5c5c5, 0x8000c29a)
                .setPrivacyWithBookTitleMark(true)
                .enableHintToast(true, Toast.makeText(context, "请勾选协议", Toast.LENGTH_SHORT))
                .setPrivacyTextSize(12)
                .setUncheckedImgPath("privacy_unchecked_one_login")
                .setCheckedImgPath("privacy_checked_one_login")
                .setPrivacyCheckboxSize(0)
                .setPrivacyTextCenterGravity(true)
                .setPrivacyState(true)
                .setPrivacyOffsetY(23)
                .setPrivacyNavColor(0xffffffff)
                .setPrivacyNavReturnBtn(privacyNavImageView(context))
                .setPrivacyNavTitleTextColor(0xff444444)
                .setPrivacyNavTitleTextSize(16)
                .setAppPrivacyNavTitle1("用户许可协议")
                .setAppPrivacyNavTitle2("隐私政策")
                .setSloganTextColor(0xffa5a5a5)
                .setSloganTextSize(12)
                .setSloganOffsetY(178)
                .setLogBtnOffsetY(230)
                .setNavTransparent(false)
                .addCustomView(otherLoginView(context), false, new JVerifyUIClickCallback() {
                    @Override
                    public void onClicked(Context context, View view) {
                        LaunchUtils.launchSmsLoginActivity(context);
                        JVerificationInterface.dismissLoginAuthActivity();
                    }
                })
                .addCustomView(tipView(context), false, null)
                .addCustomView(localNumberView(context), false, null)
                .build();

    }

    private TextView otherLoginView(Context context) {
        RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamPhoneLogin.setMargins(0, UiUtil.dip2px(context, 350.0f), 0, 0);
        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParamPhoneLogin.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        TextView tvPhoneLogin = new TextView(context);
        tvPhoneLogin.setText("更换手机号");
        tvPhoneLogin.setTextSize(15);
        tvPhoneLogin.setTextColor(0xff999999);
        tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
        return tvPhoneLogin;
    }

    private TextView tipView(Context context) {
        RelativeLayout.LayoutParams tipParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tipParams.setMargins(0, 0, 0, UiUtil.dip2px(context, 77.0f));
        tipParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        tipParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        TextView tvPhoneLogin = new TextView(context);
        tvPhoneLogin.setText("未注册的手机号验证后自动创建账号");
        tvPhoneLogin.setTextSize(14);
        tvPhoneLogin.setTextColor(0xff999999);
        tvPhoneLogin.setLayoutParams(tipParams);
        return tvPhoneLogin;
    }

    private TextView localNumberView(Context context) {
        RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        numberParams.setMargins(0, UiUtil.dip2px(context, 185.0f), 0, 0);
        numberParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        numberParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        TextView tvPhoneLogin = new TextView(context);
        tvPhoneLogin.setText("本机号码");
        tvPhoneLogin.setTextSize(15);
        tvPhoneLogin.setTextColor(0xff999999);
        tvPhoneLogin.setLayoutParams(numberParams);
        return tvPhoneLogin;
    }

    private ImageView privacyNavImageView(Context context) {
        RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamPhoneLogin.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamPhoneLogin.leftMargin = UiUtil.dip2px(context, 10.0f);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.btn_arrow_back);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParamPhoneLogin);
        return imageView;
    }

}
