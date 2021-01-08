package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.dangdang.gx.ui.utils.AppUtil;

public class DDRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

	public DDRadioButton(Context context) {
		super(context);
		init(context);
	}

	public DDRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DDRadioButton(Context context, AttributeSet attrs, int defStyle) {
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

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Drawable drawable = this.getBackground();
		if (drawable != null)
			drawable.setCallback(null);
	}
}
