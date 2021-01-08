package com.dangdang.gx.ui.html;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.base.BaseActivity;
import com.dangdang.gx.ui.eventBus.ShowCloseBtnEvent;
import com.dangdang.gx.ui.html.basehtml.BaseReaderHtmlFragment;
import com.dangdang.gx.ui.html.model.H5RightButtonBean;
import com.dangdang.gx.ui.utils.UiUtil;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * 普通的Html页
 */
public class StoreNormalHtmlActivity extends BaseActivity implements BaseReaderHtmlFragment.HtmlFragmentToActivityCallBack {
    /**
     * static
     */
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_HTML_PATH = "EXTRA_HTML_PATH";
    private String mTitle;            // 页面标题
    private String mHtmlPath;        // html页面路径
    private boolean mHasCategory = false;
    protected StoreNormalHtmlFragment mStoreNormalHtmlFragment;

    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        setContentView(R.layout.store_nornal_html_activity);
        initIntentData();
        initUi();
        setOnClickListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra(EXTRA_TITLE);
            mHtmlPath = intent.getStringExtra(EXTRA_HTML_PATH);
        }
    }

    private void initUi() {
        initTitleLayout();
        initContentLayout();
    }

    private void initTitleLayout() {
        updateTitle(mTitle);
    }

    protected void updateTitle(String titleName) {
        TextView commonTitleView = (TextView) findViewById(R.id.common_title);
        commonTitleView.setText(titleName);

    }

    private void initContentLayout() {
        mStoreNormalHtmlFragment = StoreNormalHtmlFragment.getInstance(mHtmlPath);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_fl, mStoreNormalHtmlFragment);
        t.commitAllowingStateLoss();
        mStoreNormalHtmlFragment.setHtmlFragmentToActivityCallBack(this);
    }

    private void setOnClickListener() {
        findViewById(R.id.common_back).setOnClickListener(mOnClickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onBack() {
        if (mHasCategory) {
            mStoreNormalHtmlFragment.handleJavaScriptMethod("javascript:toFrontPage()");
        } else {
            onBackInner();
        }
    }

    private void onBackInner() {
        if (mStoreNormalHtmlFragment != null) {
            mStoreNormalHtmlFragment.onBack();
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.common_back:
                    onBack();
                    break;
            }
        }
    };





    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    @Subscribe
    public void onEventShowCloseBtn(ShowCloseBtnEvent event) {
        ImageView searchImage = (ImageView) findViewById(R.id.common_menu_btn);
        //searchImage.setImageResource(R.drawable.browser_close);
        searchImage.setOnClickListener(mOnClickListener);
    }


    private AnimationSet createAnimation() {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation animationBig = new ScaleAnimation(1.0f, 1.1f, 1.0f,
                1.1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animationBig.setDuration(100);

        ScaleAnimation animationSmall = new ScaleAnimation(1.1f, 1.0f,
                1.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animationSmall.setDuration(100);
        animationSmall.setStartOffset(100);

        animationSet.addAnimation(animationSmall);
        animationSet.addAnimation(animationBig);

        return animationSet;
    }




    @Override
    public void onSetH5RightBtn(List<H5RightButtonBean> rightButtonBeans) {
        if (rightButtonBeans == null || rightButtonBeans.size() == 0)
            return;
        if (rightButtonBeans.get(0) != null) {
            H5RightButtonBean btn = rightButtonBeans.get(0);
            if (!TextUtils.isEmpty(btn.getText())) {
                TextView textView = (TextView) findViewById(R.id.common_menu_tv);
                textView.setText(btn.getText());
                if (!TextUtils.isEmpty(btn.getColor())) {
                    textView.setTextColor(Color.parseColor(btn.getColor()));
                }
                if (btn.getFontSize() > 0) {
                    textView.setTextSize(btn.getFontSize());
                }
                textView.setOnClickListener(rightBtnClickListener(btn));
            } else if (!TextUtils.isEmpty(btn.getIconUrl())) {
                ImageView imageView = (ImageView) findViewById(R.id.common_menu_btn);
                //ImageManager.getInstance().dislayImage(btn.getIconUrl(), imageView, R.drawable.introduction_white_clicked);
                imageView.setOnClickListener(rightBtnClickListener(btn));
            }
        }
    }



    @Override
    public void updateH5Title(String title) {
        updateTitle(title);
    }


    private OnClickListener rightBtnClickListener(final H5RightButtonBean btn) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStoreNormalHtmlFragment.mWebView.loadUrl("javascript:window." + btn.getAction());
            }
        };
    }


    @Override
    protected void onResume() {
        UiUtil.hideInput(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
