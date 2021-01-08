package com.dangdang.gx.ui.base;

import android.content.Context;
import android.os.Bundle;

import com.dangdang.gx.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {
    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (showAnimation()) {
            overridePendingTransition(R.anim.anim_activity_in, R.anim.anim_activity_in_fake);
        }
        onCreateImpl(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //UmengStatistics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //UmengStatistics.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        if (showAnimation()) {
            overridePendingTransition(R.anim.anim_group_activity_in, R.anim.anim_group_activity_out);
        }
    }

    protected abstract void onCreateImpl(Bundle savedInstanceState);

    /**
     * 是否支持进入和退出的平移动画
     *
     * @return
     */
    protected boolean showAnimation() {
        return true;
    }
}

