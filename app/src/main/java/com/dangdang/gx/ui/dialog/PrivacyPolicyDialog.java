package com.dangdang.gx.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dangdang.gx.R;
import com.dangdang.gx.ui.utils.ClickUtil;
import com.dangdang.gx.ui.utils.DeviceUtil;


public class PrivacyPolicyDialog extends IDialog {

    public PrivacyPolicyDialog(Context context, int theme) {
        super(context, theme);
    }

    public PrivacyPolicyDialog(Context context) {
        super(context);
    }

    public void setClickListener(View.OnClickListener listener) {
        findViewById(R.id.confirm_tv).setOnClickListener(listener);
        findViewById(R.id.cancel_tv).setOnClickListener(listener);
    }

    @Override
    public void onCreateD() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View contentView = layoutInflater.inflate(R.layout.dialog_privacy_policy, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (DeviceUtil.getInstance(mContext).getDisplayWidth() * 0.8);
            dialogWindow.setAttributes(lp);
        }

        TextView contentTv = contentView.findViewById(R.id.content_tv);
        SpannableStringBuilder ss = new SpannableStringBuilder(mContext.getResources().getString(R.string.privacy_policy_content));
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (ClickUtil.checkFastClick())
                    return;
//                LaunchUtils.launchAboutActivity((Activity) mContext);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0XFF00C29A);
                ds.setUnderlineText(false);
            }
        }, 40, 53, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (ClickUtil.checkFastClick())
                    return;
//                LaunchUtils.launchPrivateRulesActivity((Activity) mContext);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0XFF00C29A);
                ds.setUnderlineText(false);
            }
        }, 54, 65, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        contentTv.setText(ss);
        contentTv.setHighlightColor(Color.TRANSPARENT);
    }
}
