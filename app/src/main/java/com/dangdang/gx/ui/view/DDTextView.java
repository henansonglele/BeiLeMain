package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dangdang.gx.ui.utils.AppUtil;


public class DDTextView extends androidx.appcompat.widget.AppCompatTextView {

	public DDTextView(Context context) {
		super(context);
		init(context);
	}

	public DDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DDTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
