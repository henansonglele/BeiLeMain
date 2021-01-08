package com.dangdang.gx.ui.html.basehtml;

import android.util.Log;
import android.webkit.JavascriptInterface;
//import com.dangdang.gx.ui.utils.DangDangParams;
import com.dangdang.gx.ui.utils.DangdangConfig;

/**
 *
 */
public class JSHandle {
    public static final int UNSupportThisMethod = 0;
    public static final int NativeSupportThisMethod = 1;
    public static final int JSBridgeSupportThisMethod = 2;
    public static final int BothSupportThisMethod = 3;
	private OnHtmlClickListener mOnHtmlClickListener;


    public JSHandle(OnHtmlClickListener onHtmlClickListener) {
		mOnHtmlClickListener = onHtmlClickListener;
	}

	public void setJsHandle(OnHtmlClickListener handle) {
		mOnHtmlClickListener = handle;
	}

	@JavascriptInterface
	public void onShowToast(String msg) {
		if (mOnHtmlClickListener != null) {
			mOnHtmlClickListener.onShowToast(msg);
		}
	}

	@JavascriptInterface
	public String getServerFont() {
		return "";//DangdangFileManager.getPreSetTTF();
	}

	@JavascriptInterface
	public String getServerIp() {
		return DangdangConfig.getAppH5Host();
	}

	@JavascriptInterface
	public void callHandler(String methodName, String methodParam) {
		if (mOnHtmlClickListener != null) {
			mOnHtmlClickListener.callHandler(methodName, methodParam);
		}
	}

	@JavascriptInterface
	public String getParam() {
      return "temp praram";
        //return DangDangParams.getPublicParams();
	}

	// 1：可以往左滑动 2：可以往右滑 3：左右都不可以滑 4：左右都可滑动
	@JavascriptInterface
	public void getNativeScrollState(int num) {
		if (mOnHtmlClickListener != null) {
			mOnHtmlClickListener.getNativeScrollState(num);
		}
	}
	// native 不处理滑动区域
	@JavascriptInterface
	public void setNotScrollHeight(int start,int end) {
		if (mOnHtmlClickListener != null) {
			mOnHtmlClickListener.setNotScrollHeight(start, end);
		}
	}
 /**
  * native 不处理滑动区域
  * 可以多次调用，本地维护一个list存储多个不滑动区域
  * */
@JavascriptInterface
public void addNotScrollHeightArray(int start,int end){
    if(mOnHtmlClickListener!=null)
        mOnHtmlClickListener.addNotScrollHeightArray(start,end);
}
/**
 * 清除所有不滑动区域s
 * */
@JavascriptInterface
public void clearNotScrollHeightArray(){
    mOnHtmlClickListener.clearNotScrollHeightArray();
}
    // h5 调用 native设置 title
@JavascriptInterface
public void setTitle(String title) {
    if (mOnHtmlClickListener != null) {
        //mOnHtmlClickListener.setNotScrollHeight(start, end);
    }
}


/**
 *  原生是否支持h5调用的当前接口
 *
 *  callbackMethodName 接口名字
 *
 *  判断是否支持 callbackMethodName 接口，，不支持给h5返回0。 原生方式支持给h5返回 1 ，jsbirdge方式支持返回2，原生和jsbridge 都支持返回 3
 */
@JavascriptInterface
public int checkSupport(String  callbackMethodName) {
    if(callbackMethodName.equals("getParam")||callbackMethodName.equals("setTitle")||callbackMethodName.equals("clearNotScrollHeightArray")
            ||callbackMethodName.equals("addNotScrollHeightArray")||callbackMethodName.equals("getNativeScrollState")||callbackMethodName.equals("setNotScrollHeight")
            ||callbackMethodName.equals("onShowToast")||callbackMethodName.equals("getServerIp")||callbackMethodName.equals("getServerFont")||callbackMethodName.equals("callHandler")
            ){
        return NativeSupportThisMethod;
    }
   else if (mOnHtmlClickListener != null) {
        return  mOnHtmlClickListener.checkSupport(callbackMethodName);
    }
    return UNSupportThisMethod;
}

@JavascriptInterface
public void setNavBack(String  callbackMethodName) {
    Log.d("ddd", " setNavBac1k:" );
}
@JavascriptInterface
public void setNavBack() {
    Log.d("ddd", " setNavBac2k:" );
}
}
