package com.dangdang.gx.ui.html.basehtml.model;

import java.io.Serializable;

/**
 * Created by songxile on 17/12/20.
 *
 * h5 轮播图 触摸区域，在此区域触摸事件，原生不拦截直接交给webview，
 * start 区域顶部
 * end 区域底部
 */

public class WebViewTouchInvalidRegion implements Serializable{
    public  WebViewTouchInvalidRegion(int start,int end){
        this.start = start;
        this.end = end;
    }
    public int start;
    public  int end;
}
