package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by liuboyu on 2015/3/25.
 */
public class DDImageButton extends androidx.appcompat.widget.AppCompatImageButton {

	public DDImageButton(Context context){
		super(context);
	}

	public DDImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DDImageButton(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		Drawable drawable = this.getBackground();
		if(drawable != null)
			drawable.setCallback(null);
		drawable = this.getDrawable();
		if(drawable != null)
			drawable.setCallback(null);
	}
}