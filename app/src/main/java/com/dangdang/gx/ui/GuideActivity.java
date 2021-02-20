package com.dangdang.gx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.base.BaseActivity;
import com.dangdang.gx.ui.dialog.PrivacyPolicyDialog;
import com.dangdang.gx.ui.utils.ClickUtil;

public class GuideActivity extends BaseActivity {

    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        startMain();
        //showPrivacyPolicyDialog();
    }

    private void showPrivacyPolicyDialog() {
        final PrivacyPolicyDialog dialog = new PrivacyPolicyDialog(this, R.style.dialog_commonbg);
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtil.checkFastClick()) {
                    return;
                }
                switch (v.getId()) {
                    case R.id.confirm_tv:
                        dialog.dismiss();
                        startMain();
//                        mIsShowPrivacyDialog = false;
//                        FirstGuideManager.getInstance(GuideActivity.this).setPrivacyPolicyFirst(false);
//
//                        onConfirmPrivacyPolicy();
//                        BIStatisticsUtils.insertEntity(BIConstants.PRIVACY_DIALOG_PAGE_ID, BIConstants.PRIVACY_DIALOG_AGREE_CLICK_EVENT_ID, "",
//                                biStartTime, biCms, biFloor, biLastPageID, biLastGuandID , BIConstants.TYPE_ClICL,
//                                "", BIConstants.getCustId(mContext));
                        break;
                    case R.id.cancel_tv:
                        dialog.dismiss();
                        startMain();
//                        mIsShowPrivacyDialog = false;
//                        FirstGuideManager.getInstance(GuideActivity.this).setPrivacyPolicyFirst(true);
//
//                        onCancelPrivacyPolicy();
//                        BIStatisticsUtils.insertEntity(BIConstants.PRIVACY_DIALOG_PAGE_ID, BIConstants.PRIVACY_DIALOG_CANCEL_CLICK_EVENT_ID, "",
//                                biStartTime, biCms, biFloor, biLastPageID, biLastGuandID, BIConstants.TYPE_ClICL,
//                                "", BIConstants.getCustId(mContext));
                        break;
                }
            }
        });
        dialog.show();
//        mIsShowPrivacyDialog = true;
    }

    private void startMain() {
        Intent intent = getIntent();
        intent.setClass(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
