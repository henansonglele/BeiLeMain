package com.dangdang.gx.ui.html.basehtml;

import android.webkit.JavascriptInterface;
import com.dangdang.gx.ui.utils.DRUiUtility;

public class OneDigestJSInterface {

    private OnJSListener mListener;

    public OneDigestJSInterface(OnJSListener listener) {
        mListener = listener;
    }

    public void setJSListener(OnJSListener l){
        mListener = l;
    }

    public interface OnJSListener {
        public void openImageWithPosition(String[] urlList, String url, int left, int top,
                int right, int bottom);
    }

    @JavascriptInterface
    public void openImageWithPosition(String[] urlList, String url, int left, int top, int right, int bottom) {
        if (mListener != null) {
            float density = DRUiUtility.getDensity();
            mListener.openImageWithPosition(urlList, url, (int) (left * density), (int) (top * density),
                    (int) (right * density), (int) (bottom * density));
        }
    }
}