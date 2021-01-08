package com.dangdang.gx.ui.html.basehtml;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.base.BaseFragment;
import com.dangdang.gx.ui.dialog.CommonDialog;
import com.dangdang.gx.ui.eventBus.OnLogoutSuccessEvent;
import com.dangdang.gx.ui.eventBus.ShowCloseBtnEvent;
import com.dangdang.gx.ui.html.basehtml.model.WebViewTouchInvalidRegion;
import com.dangdang.gx.ui.html.model.H5HandlebackEvent;
import com.dangdang.gx.ui.html.model.H5RightButtonBean;
import com.dangdang.gx.ui.log.LogM;
import com.dangdang.gx.ui.utils.ConfigManager;
import com.dangdang.gx.ui.utils.DangdangFileManager;
import com.dangdang.gx.ui.utils.HtmlRegexpUtil;
import com.dangdang.gx.ui.utils.LaunchUtils;
import com.dangdang.gx.ui.utils.NotificationsUtils;
import com.dangdang.gx.ui.utils.UiUtil;
import com.dangdang.gx.ui.view.DDWebView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 *
 */
public abstract class BaseReaderHtmlFragment extends BaseFragment
        implements OnHtmlClickListener {
    public DDWebView mWebView;
    protected JSHandle mJsHandle = new JSHandle(this);

    /**
     * 跳转 action
     */
    private static final String TO_H5PAGE = "toH5Page";                     // 跳转一般的h5页面
    private static final String TO_PRODUCT = "toProduct";                   // h5跳单品
    private static final String TO_WEIXIN = "toWeiXin";                   // h5跳微信
    private static final String TO_SEARCH = "toSearch";                     // H5跳转搜索页
    private static final String REFRESH_FINISHED = "refreshFinished";       // 完成下拉刷新的回调接口
    private static final String REFRESH_STATE = "refreshState";             // 刷新状态
    private static final String TO_BOOK_STORE_INDEX = "toBookstoreIndex";   // H5跳转书城首页
    private static final String TO_OPEN_LOGIN_INDEX = "toOpenLoginIndex";   // H5跳转登录页
    private static final String HIDE_SOFT_INPUT = "hideSoftInput";          // 隐藏软键盘
    private static final String TO_HANDLEBACK = "handleback";               //H5处理back事件回调
    private static final String UPDATETITLENAME = "updateTitleName";        //更改标题名称
    private static final String SHOW_CLOSEBTN = "closePage";                //显示关闭按钮
    private static final String H5_GET_TOKEN = "callToken";                 // H5获取用户token
    private static final String SHOW_DIALOG = "show_dialog";                  //弹出 普通双按钮 dialog
    private static final String SET_NAV_BACK = "setNavBack";//导航栏 back
    private static final String MDD_TOKEN = "MDD_token";
    private static final String SHOW_LOADING = "showLoading";    // loading
    private static final String CHECK_NOTIFI = "judgePermissionNotifi";// h5 调用原生 获取 通知开关状态

    protected int mGetScrollState = 0;
    protected int mNotScrollStart = 0;
    protected int mNotScrollEnd = 0;
    protected List<WebViewTouchInvalidRegion> webViewTouchInvalidRegions = new ArrayList<>();
    private static final int NATIVE_DEAL_SUCCESS = 0;
    private static final int NATIVE_DEAL_FAIL = -1;
    private String callbackFunName;
    protected Handler mHandler;
    private String h5LifeCycleName = "";//js 接受 webview 生命周期的函数name

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
        mHandler = new MyHandler(this);
    }

    protected void initWebView() {
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setScrollbarFadingEnabled(false);
        try {
            mWebView.getSettings().setJavaScriptEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initUserAgent();

        mWebView.getSettings().setDefaultTextEncodingName("gbk");
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getActivity().getApplicationContext()
                .getCacheDir().getAbsolutePath();
        // mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setTextZoom(100);//避免修改系统字体大小后 webview界面混乱。

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);//设置h5可以播放音频，不需要用户手势打开开关
        }

        //设置  WebView中支持Http和Https混合使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(mJsHandle, "JSHandle");
        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setOnLongClickListener(new DDWebView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    private void initUserAgent() {
        ConfigManager cfm = new ConfigManager(getActivity());
        String version = cfm.getVersionName();

        String oldUAStr = mWebView.getSettings().getUserAgentString();

        if (TextUtils.isEmpty(oldUAStr) || !oldUAStr.contains("ddReader-Android")) {
            if (TextUtils.isEmpty(oldUAStr)) {
                mWebView.getSettings().setUserAgentString(" ddReader-Android/" + version);
            } else {
                mWebView.getSettings().setUserAgentString(oldUAStr + " ddReader-Android/" + version);
            }
        }

    }

    protected final WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String message = consoleMessage.message();
            if ("Uncaught ReferenceError: refresh is not defined".equals(message)) {
                getHtmlData();
            }
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress > 70) {
                hideGifLoadingByUi();
                //LogM.d("webviewloading","progress:"+newProgress+":"+getHtmlUrl());
            }
        }
    };

    protected void getHtmlData() {
        showGifLoadingByUi();
        if (TextUtils.isEmpty(getHtmlUrl())) {//online bug fix
            UiUtil.showToast(getActivity(), "数据错误");
            return;
        }
        HtmlRegexpUtil.loadHtmlData(mWebView, getHtmlUrl(), null);
        if(hideLoading!=null) {
            Message message = Message.obtain();
            message.what = 0;
            hideLoading.sendMessageDelayed(message,15000);
        }
    }

    Handler hideLoading = new Handler(){
        public void  handleMessage(Message msg){
            hideGifLoadingByUi();
        }
    };

    protected void getHtmlDataRefresh() {
        //showGifLoadingByUi(mRootView, -1);
        if (TextUtils.isEmpty(getHtmlUrl())) {//online bug fix
            UiUtil.showToast(getActivity(), "数据错误");
            return;
        }
        HtmlRegexpUtil.loadHtmlData(mWebView, getHtmlUrl(), null);
    }

    protected abstract String getHtmlUrl();

    @Override
    public void callHandler(final String methodName, final String methodParam) {

        if (TextUtils.isEmpty(methodName)) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = null;
                try {
                    json = JSONObject.parseObject(methodParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    handleH5Method(methodName, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    protected void handleH5Method(String methodName, JSONObject json) {
        try {

            switch (methodName) {
                case TO_H5PAGE:
                    toH5Page(json);
                    break;
                case TO_PRODUCT:
                    toProduct(json);
                    break;
                case TO_WEIXIN:
                    //获取需要关注 公众号name
                    break;
                case TO_SEARCH:
                    toSearch(json);
                    break;
                case REFRESH_FINISHED:
                    boolean isFinished = json.getBoolean("tf");
                    refreshFinished(isFinished);
                    break;
                case REFRESH_STATE:
                    boolean state = json.getBoolean("tf");
                    refreshState(state);
                    break;
                case TO_BOOK_STORE_INDEX:
                    toStore();
                    break;
                case TO_OPEN_LOGIN_INDEX:
                    mLoginSuccessResponseMethodStr = json.getString("response");
                    toLogin();
                    break;
                case HIDE_SOFT_INPUT:
                    UiUtil.hideInput(getActivity());
                    break;
                case TO_HANDLEBACK:
                    handleH5back(json);
                    break;
                case UPDATETITLENAME:
                    handleUpdateTitle(json);
                    break;
                case SHOW_CLOSEBTN:
                    handleShowCloseBtn(json);
                    break;
                case H5_GET_TOKEN:
                    handleGetToken(json);
                    break;

                case SHOW_DIALOG:
                    handleShowDialog(json);
                    break;

                case SET_NAV_BACK:
                    handleSetNaviBack(json);
                    break;
               case CHECK_NOTIFI:
                   notifyH5NotificationStatus(json);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void handleSetNaviBack(JSONObject json) {
        getActivity().finish();
    }



    private void handleGetToken(JSONObject json) {

    }

    // h5 调用原生显示common 双按钮  dialog
    private CommonDialog handleShowDialog(JSONObject json) {
        if (json == null) {
            UiUtil.showToast(getActivity(), "JSON数据格式错误");
            return null;
        }
        String titleStr = json.getString("title");
        String infoStr = json.getString("info");

        final CommonDialog dialogBuilder = new CommonDialog(getActivity(), R.style.dialog_commonbg);
        if (TextUtils.isEmpty(titleStr)) {
            dialogBuilder.hideTitle();
        } else {
            dialogBuilder.showTitle();
            dialogBuilder.setTitle(titleStr);
        }
        dialogBuilder.setInfo(infoStr);

        if (json.containsKey("leftButton")) {
            JSONObject leftButtonJsonObject = json.getJSONObject("leftButton");
            int state = leftButtonJsonObject.getIntValue("state");
            if (state == 1) {
                String leftButtonNameStr = leftButtonJsonObject.getString("name");
                dialogBuilder.setLeftButtonText(leftButtonNameStr);
                final String leftButtonTargetStr = leftButtonJsonObject.getString("target");
                final JSONObject leftButtonTargetParametersJsonObject = json.getJSONObject("parameters");

                dialogBuilder.setOnLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        handleH5DialogMethod(leftButtonTargetStr, leftButtonTargetParametersJsonObject);
                    }
                });

            } else {
                dialogBuilder.hideLeftButton();
            }

        } else {
            dialogBuilder.hideLeftButton();
        }

        if (json.containsKey("rightButton")) {
            JSONObject rightButtonJsonObject = json.getJSONObject("rightButton");
            int state = rightButtonJsonObject.getIntValue("state");
            if (state == 1) {
                String rightButtonNameStr = rightButtonJsonObject.getString("name");
                dialogBuilder.setRightButtonText(rightButtonNameStr);
                final String rightButtonTargetStr = rightButtonJsonObject.getString("target");
                final JSONObject rightButtonTargetParametersJsonObject = json.getJSONObject("parameters");

                dialogBuilder.setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        handleH5DialogMethod(rightButtonTargetStr, rightButtonTargetParametersJsonObject);
                    }
                });

            } else {
                dialogBuilder.hideRightButton();
            }

        } else {
            dialogBuilder.hideRightButton();
        }

        dialogBuilder.show();

        return dialogBuilder;
    }



    private void handleUpdateTitle(JSONObject json) {
        fragmentToActivityCallBack.updateH5Title(json.getString("title"));
    }

    HtmlFragmentToActivityCallBack fragmentToActivityCallBack;

    public interface HtmlFragmentToActivityCallBack {

        void onSetH5RightBtn(List<H5RightButtonBean> rightButtonBeans);

        void updateH5Title(String title);
    }

    public void setHtmlFragmentToActivityCallBack(HtmlFragmentToActivityCallBack fragmentToActivityCallBack) {
        this.fragmentToActivityCallBack = fragmentToActivityCallBack;
    }

    private void handleH5back(JSONObject json) {
        H5HandlebackEvent handlebackEvent = new H5HandlebackEvent();
        handlebackEvent.h5BackState = json.getIntValue("backstate");
        if (!handlebackEvent.handleBackState()) {
            onBack();
        }
    }
    private void handleShowCloseBtn(JSONObject json) {
        ShowCloseBtnEvent event = new ShowCloseBtnEvent();
        EventBus.getDefault().post(event);
    }


    private void handleJavaScriptMethod(String fileUrl, String srcImgUrl) {
        String url;
        if (TextUtils.isEmpty(fileUrl)) {
            url = "javascript:" + callbackFunName + "(" + "'" + fileUrl + "'"
                    + "," + "'" + srcImgUrl + "'" + ")";
        } else {
            url = "javascript:" + callbackFunName + "(" + "'" + "file://"
                    + fileUrl + "'" + "," + "'" + srcImgUrl + "'" + ")";
        }
        mWebView.loadUrl(url);
    }

    public void handleJavaScriptMethod(String script) {
        if (TextUtils.isEmpty(script)) {
            LogM.d(getClass().getSimpleName(), " handleJavaScriptMethod error: script is empty...");
            return;
        }
        mWebView.loadUrl(script);
    }

    /**
     * native 回调h5方法
     *
     * @param responseMethodNameStr
     * @param bSuccess
     * @param msg
     * @param responseMethodParam
     */
    private void handleJavaScriptMethod(String responseMethodNameStr, boolean bSuccess, String msg, JSONObject responseMethodParam) {
        mWebView.loadUrl("javascript:" + responseMethodNameStr + "(" + formResponseData(bSuccess, msg, responseMethodParam) + ")");
    }

    /**
     * 返回回调h5的参数格式
     *
     * @param bSuccess 是否本地处理成功
     * @param msg
     * @param params   回调h5方法的参数
     * @return 返回回调h5的参数格式
     */
    private String formResponseData(boolean bSuccess, String msg, JSONObject params) {
        JSONObject jsonObject = new JSONObject();
        if (bSuccess) {
            jsonObject.put("code", NATIVE_DEAL_SUCCESS);
        } else {
            jsonObject.put("code", NATIVE_DEAL_FAIL);
        }

        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        jsonObject.put("msg", msg);

        if (params != null) {
            jsonObject.put("data", params);
        }
        return jsonObject.toJSONString();
    }



    private void toH5Page(JSONObject json) throws Exception {
        if (isFastDoubleClick()) {
            return;
        }

        LaunchUtils.launchStoreNormalHtmlActivity(getActivity(), json.getString("title"), json.getString("url"));
    }




    private void toProduct(JSONObject json) throws Exception {
        if (isFastDoubleClick()) {
            return;
        }
        String id = json.getString("id");
        String mediaId = json.getString("mediaId");
        LaunchUtils.launchBookDetail(getActivity(), mediaId);
    }



    private void toSearch(JSONObject json) throws Exception {
        if (isFastDoubleClick()) {
            return;
        }
        LaunchUtils.launchSearchActivity(getActivity(), json.getString("key"));
    }

    private void toStore() {
        LaunchUtils.launchStore(getActivity());
    }



    private void toLogin() {
        LaunchUtils.gotoLogin(getActivity());
    }

    @Override
    public void onShowToast(String msg) {
        UiUtil.showToast(getActivity(), msg);
    }


    @Override
    public String getServerFont() {
        return DangdangFileManager.getPreSetTTF();
    }



    @Override
    public String getParam() {
        return "temp param";
        //return DangDangParams.getPublicParams()  /* + "&pubIdNum=" + mAccountManager.getPubIdNum() + "&custId=" + mAccountManager.getCustId()*/;//cust_id ,专栏作家认证使用;添加publicNum ,阅读打卡使用
    }

    @Override
    public void getNativeScrollState(int num) {
        mGetScrollState = num;
    }

    @Override
    public void setNotScrollHeight(int start, int end) {
        mNotScrollStart = UiUtil.dip2px(getContext(), start);
        mNotScrollEnd = UiUtil.dip2px(getContext(), end);
        LogM.d("sxl", "1 end:" + end);
    }

    @Override
    public void addNotScrollHeightArray(int start, int end) {
        WebViewTouchInvalidRegion obj = new WebViewTouchInvalidRegion(start, end);
        obj.start = UiUtil.dip2px(getContext(), obj.start);
        obj.end = UiUtil.dip2px(getContext(), obj.end);

        this.webViewTouchInvalidRegions.add(obj);
    }

    @Override
    public void clearNotScrollHeightArray() {
        this.webViewTouchInvalidRegions.clear();
    }

    public abstract void refreshState(boolean state);


    /**
     * 处理webview的back事件
     */
    public void onBack() {
        // TODO 处理goBack事件的问题，如空白页
        try {
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        callH5LifeCircle("onDestroy");
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        cancelLastHideLoading();
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        callH5LifeCircle("onResume");
        super.onResume();

    }

    @Override
    public void onPause() {
        mWebView.onPause();
        callH5LifeCircle("onpause");
        super.onPause();
    }


    // dialog 点击按钮后  跳转目的地
    private void handleH5DialogMethod(String targetMethodStr, JSONObject targetParametersJsonObject) {
        if (TextUtils.isEmpty(targetMethodStr) || targetParametersJsonObject == null)
            return;
        handleH5Method(targetMethodStr, targetParametersJsonObject);
    }
    public String mLoginSuccessResponseMethodStr;
    // 左上角title栏back键 可以类似操作。
    public String backButtonJsMehtod = null;
    /**
     * 分享结果 回调h5 函数name
     */
    public String callBackShareMehtodName = null;


    /**
     * 客户端判断当天是否 第一次进入 阅读打卡 界面。
     *
     * @param callbackFunName
     */
    public void checkIsVistedToday(String callbackFunName) {
    }


    /**
     * 客户端判断  账号是否当日首次进入 阅读打卡 界面。
     *
     * @param callbackFunName
     */
    public void checkUserIsVistToday(String callbackFunName) {
    }


    /**
     * 客户端判断 通知开关 状态 并把结果 通知h5
     **/
    public void notifyH5NotificationStatus(JSONObject jsonStr) {
        //回调js  返回的code： 0:notification 未打开，1：notification 已经打开
        String callbackFunName = jsonStr.getString("callbackFunName");
        String json = "{code:" + 0 + "}";
        if (!NotificationsUtils.canGetNotificationStatus() || NotificationsUtils.isNotificationEnable(getActivity())) {
            json = "{code:" + 1 + "}";
        }
        mWebView.loadUrl("javascript:window." + callbackFunName + "(" + json + ")");
    }


    /**
     * H5通知原生隐藏loadingview
     *
     * @param data
     */
    public void h5CallHideLoading(String data) {
        Log.d("webview", "h5CallHideLoading:" + getHtmlUrl());
        hideGifLoadingByUi();
        cancelLastHideLoading();

    }

    /**
     * 取消兜底  15s 隐藏loading'
     */
    public void cancelLastHideLoading(){

        if(hideLoading!= null ){
            hideLoading.removeMessages(0);
            hideLoading = null;
        }
    }

    /**
     * @param data 0禁止下拉刷新 1 可以下拉刷新
     */
    //h5 通知原生 停止下拉刷新
    public void isStopPullRefresh(String data) {

        if (TextUtils.isEmpty(data)) {
            return;
        }
        Log.d("sxl", data);
        if (TextUtils.isEmpty(data)) {
            return;
        }
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null) {
            return;
        }
        int response = json.getInteger("response");


        if (response == 0) {
            stopPullRefresh();
        } else if (response == 1) {
            enablePullRefresh();
        }
    }

    protected void dealMsg(Message msg) {

    }
    private static class MyHandler extends Handler {

        private final WeakReference<BaseReaderHtmlFragment> mFragmentView;

        MyHandler(BaseReaderHtmlFragment view) {
            this.mFragmentView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseReaderHtmlFragment service = mFragmentView.get();
            if (service == null) {
                return;
            }
            try {
                super.handleMessage(msg);
                service.dealMsg(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPullRefresh() {
    }

    public void enablePullRefresh() {
    }

    private void gerH5LifeCircleMethodName(String responseMethodNameStr) {
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(responseMethodNameStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json == null) {
            return;
        }
        h5LifeCycleName = json.getString("response");
    }

    /**
     * 通知webview的生命周期
     * <p>
     * js 接受函数name = webViewLifecycle
     * <p>
     * type  = onPause 页面进入后台
     * type  = onResume 页面进入前台
     * type  = onDestroy 页面销毁
     */
    private void callH5LifeCircle(String type) {
        if (!TextUtils.isEmpty(h5LifeCycleName)) {
            String url = "javascript:window." + h5LifeCycleName + "('" + type + "')";
            //LogM.d("callbackurl",url);
            mWebView.loadUrl(url);

        }

    }

    /**
     * 退出登录 通知h5
     * @param event
     */
    @Subscribe
    public void onLogoutSuccessEvent(OnLogoutSuccessEvent event) {
        handleJavaScriptMethod("callReaderLoginResultFun", false, "登出", null);
    }

    void showGifLoadingByUi(){

    }

    void hideGifLoadingByUi(){

    }

}
