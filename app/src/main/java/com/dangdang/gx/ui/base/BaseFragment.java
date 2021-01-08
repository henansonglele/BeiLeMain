package com.dangdang.gx.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public View  mRootView;

    public abstract View onCreateViewImpl(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState);

    public abstract void onDestroyImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = onCreateViewImpl(inflater, container, savedInstanceState);
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyImpl();
    }



    public  void hideGifLoadingByUi(View view){};
    public  void showToast(String string){};
    public  void showErrorView(String string){};
    public  void onRetryClick(){};
    protected void showErrorView(RelativeLayout rootRl, int promptImageResId, int promptTextResId, int promptButtonTextResId, View.OnClickListener listener, int id){
        Activity ac = getActivity();
        //if (ac instanceof BaseReaderActivity) {
        //    ((BaseReaderActivity) ac).showErrorView(rootRl, promptImageResId, promptTextResId, promptButtonTextResId, listener, id);
        //}
    }
}
