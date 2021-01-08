package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class DDImageView extends androidx.appcompat.widget.AppCompatImageView {
	
	public DDImageView(Context context){
		super(context);
	}
	
	public DDImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DDImageView(Context context, AttributeSet attrs, int style) {
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
