package com.dangdang.gx.ui.flutterbase;

import com.idlefish.flutterboost.containers.FlutterFragment;

import java.util.Map;

/**
 * Created by liuzhongtao on 2019/9/11.
 */
public class DDFlutterFragment extends FlutterFragment {
    private String pageUrl;
    private Map<String, String> params;
    public static boolean isVisible = true;

    @Override
    public String getContainerUrl() {
        return pageUrl;
    }

    @Override
    public Map getContainerUrlParams() {
        return  params;
    }

    public void setPageUrl(String pageUrl){
        this.pageUrl = pageUrl;
    }
    public void setParams(Map<String, String> params){
        this.params = params;
    }

    // 备注：[FlutterActivityAndFragmentDelegateAspect]中通过AspectJ修改FlutterActivityAndFragmentDelegate的onResume、onPause方法，
    //      根据原生的isVisible来设置FlutterFragment的appear和disappear
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser == isVisible)
            return;
        if (getUserVisibleHint()) {
            isVisible = true;
            onResume();
        } else {
            isVisible = false;
            onPause();
        }

    }

    @Override
    public void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
