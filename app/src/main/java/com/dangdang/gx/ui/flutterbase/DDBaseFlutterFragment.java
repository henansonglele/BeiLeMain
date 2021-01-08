package com.dangdang.gx.ui.flutterbase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransaction;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.base.BaseFragment;
import java.util.Map;

/**
 *  使用flutter fragment
 *
 *  只能new  DDBaseFlutterFragment，不能放在xml中使用
 */

@SuppressLint("ValidFragment")
public class DDBaseFlutterFragment extends BaseFragment {
    private DDFlutterFragment flutterFragment;
    private String pageUrl;
    private Map<String, String> params;


   @SuppressLint("ValidFragment")
   public DDBaseFlutterFragment(String pageUrl,Map<String, String> params){
       super();
       this.pageUrl = pageUrl;
       this.params = params;
   }

    @Override
    public View onCreateViewImpl(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        mRootView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_flutter_empyt_container_layout, null);
        initView();
        return mRootView;
    }

    @Override
    public void onDestroyImpl() {

    }

    private void initView(){
        flutterFragment = new DDFlutterFragment();
        flutterFragment.setPageUrl(pageUrl);
        flutterFragment.setParams(params);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flutter_out_layout, flutterFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (flutterFragment != null)
            flutterFragment.setUserVisibleHint(isVisibleToUser);
    }
}
