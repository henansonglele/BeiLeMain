package com.dangdang.gx.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.utils.UiUtil;

/**
 * Created by liuzhongtao on 2017/11/10.
 *
 */

public class DDEditTextWithDeleteButton extends DDEditText {

    private Drawable deleteBtnImage;
    private Context mContext;

    public DDEditTextWithDeleteButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DDEditTextWithDeleteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public DDEditTextWithDeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        deleteBtnImage = mContext.getResources().getDrawable(R.drawable.clear);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();

        int paddingRight =
                getPaddingRight() == 0 ? UiUtil.dip2px(getContext(), 10) : getPaddingRight();
        setPadding(getPaddingLeft(), getPaddingTop(), paddingRight, getPaddingBottom());
    }

    //设置删除图片
    private void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, deleteBtnImage, null);
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (deleteBtnImage != null && event.getAction() == MotionEvent.ACTION_UP) {
            int clickX = (int) event.getX();
            int delBtnRight = getRight() - getLeft();
            int delBtnLeft = delBtnRight - 50 - getPaddingRight();
            if (clickX > delBtnLeft && clickX < delBtnRight) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void setDeleteBtn(Drawable deleteBtnImage) {
        this.deleteBtnImage = deleteBtnImage;
    }

}
