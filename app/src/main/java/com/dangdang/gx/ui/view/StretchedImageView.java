package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class StretchedImageView extends androidx.appcompat.widget.AppCompatImageView
{
	public StretchedImageView( Context context )
	{
		this( context, null, 0 );
	}

	public StretchedImageView( Context context, AttributeSet attributes )
	{
		this( context, attributes, 0 );
	}

	public StretchedImageView( Context context, AttributeSet attributes, int defStyle )
	{
		super( context, attributes, defStyle );

		setImageMatrix( new Matrix() );
		setScaleType( ScaleType.MATRIX );
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		Drawable drawable = getDrawable();

		if( drawable != null )
		{
			// Scale image to match parent.
			float scale = getInitialScale( MeasureSpec.getSize( widthMeasureSpec ),
										   drawable.getIntrinsicWidth(),
										   MeasureSpec.getSize( heightMeasureSpec ),
										   drawable.getIntrinsicHeight() );

			getImageMatrix().setScale( scale, scale );

			int width = (int) ( drawable.getIntrinsicWidth() * scale );
			int height = (int) ( drawable.getIntrinsicHeight() * scale );

			setMeasuredDimension( width, height );
		}
		else
		{
			super.onMeasure( widthMeasureSpec, heightMeasureSpec );
		}
	}

	/**
	 * Get the initial scale factor to match the parent's width or height.
	 *
	 * @param viewWidth
	 * @param imageWidth
	 * @param viewHeight
	 * @param imageHeight
	 * @return
	 */
	protected float getInitialScale( int viewWidth, int imageWidth, int viewHeight, int imageHeight )
	{
		float widthScale = viewWidth / (float) imageWidth;
		float heightScale = viewHeight / (float) imageHeight;

		if( widthScale > 0 && heightScale > 0 )
		{
			return Math.min( widthScale, heightScale );
		}
		else if( widthScale > 0 && heightScale == 0 )
		{
			return widthScale;
		}
		else if( widthScale == 0 && heightScale > 0 )
		{
			return heightScale;
		}
		else
		{
			return 0;
		}
	}
}