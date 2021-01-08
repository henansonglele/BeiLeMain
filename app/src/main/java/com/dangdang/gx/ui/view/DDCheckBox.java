package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dangdang.gx.ui.utils.AppUtil;


public class DDCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

    public DDCheckBox(Context context) {
        super(context);
        init(context);
    }

    public DDCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DDCheckBox(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context);
    }

    protected void init(Context context) {
        try {
            Typeface ty = AppUtil.getInstance(context).getTypeface();
            if (ty != null)
                setTypeface(ty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
