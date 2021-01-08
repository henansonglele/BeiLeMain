package com.dangdang.gx.ui.dialog;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.view.DDEditText;
import com.dangdang.gx.ui.view.DDTextView;

/**
 *  目前产品中主要使用两类对话框：
 *  第一类包含标题、内容及按钮部分，内容部分主要用于展示文字不支持和用户交互，使用 CommonDialog 类封装
 *  第二类内容部分可以支持更复杂的交互，如用户输入、ListView的滑动展示等，使用 CommonDialog2 类封装
 *
 *  本类主要完成第一种对话框类型的封装，如果使用内容部分是可输入的样式，请使用 CommonDialog2
 */
public class CommonDialog extends IDialog {

	private DDTextView mTitleView;
	private DDTextView mInfoView;
	private DDTextView mMutiInfoView;
	//private DDTextView mCheckInfoView;
	private DDTextView mSureView;
	private DDTextView mCancleView;
    private LinearLayout mCloseLl;

	private View mButtonLayout;

	private DDEditText mEditText;
	private TextWatcher mContentWatcher;

	//private View mCheckLayout;
	//private DDImageView mImageView;
	private boolean isSelect;

	private boolean isFirstShow = true; // 用在 书架 借阅书籍过期时，如果是第一次弹出对话框，点击
										// 立即还书，还要在弹出一次（此处为修改显示提示语）

	public CommonDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CommonDialog(Context context, int theme) {
		super(context, theme);
	}

	public CommonDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreateD() {

		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		View contentView = layoutInflater.inflate(R.layout.common_dialog, null);
		setContentView(contentView);
		ViewGroup.LayoutParams params = contentView.getLayoutParams();
		if (params != null) {
			params.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.8);
			contentView.setLayoutParams(params);
		}
		/*mCheckLayout = findViewById(R.id.checkbox_layout);
		mImageView = (DDImageView) findViewById(R.id.checkbox_img);
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSelect) {
					mImageView.setImageResource(R.drawable.txt_delete_default);
				} else {
					mImageView.setImageResource(R.drawable.txt_delete_select);
				}
				isSelect = !isSelect;
			}
		});
		mCheckInfoView = (DDTextView) findViewById(R.id.checkbox_tv);*/
		mTitleView = (DDTextView) findViewById(R.id.dialog_title);
		mInfoView = (DDTextView) findViewById(R.id.dialog_content_tip);
		mMutiInfoView = (DDTextView) findViewById(R.id.muti_dialog_content_tip);
		mEditText = (DDEditText) findViewById(R.id.dialog_content_edit);
		mButtonLayout = findViewById(R.id.upgrade_bottom_layout);

		mSureView = (DDTextView) findViewById(R.id.make_sure);
		mCancleView = (DDTextView) findViewById(R.id.make_cancle);
        mCloseLl = (LinearLayout) findViewById(R.id.close_ll);

		// getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setCanceledOnTouchOutside(true);
        setCancelable(true);
	}

	/*public void setCheckInfo(String info) {
		mCheckInfoView.setText(info);
	}*/

	// 左按钮事件
	public void setOnLeftClickListener(View.OnClickListener l) {
		mCancleView.setOnClickListener(l);
	}

	// 右按钮事件
	public void setOnRightClickListener(View.OnClickListener l) {
		mSureView.setOnClickListener(l);
	}

	// title 提示
	public void setTitleInfo(String info) {
		mTitleView.setText(info);
	}

	// 正文 提示居中
	public void setInfo(CharSequence info) {
		mInfoView.setText(info);
	}

	public void setInfoGravity(int gravity) {
		mInfoView.setGravity(gravity);
	}

	public void setInfoLineNum(int num) {
		mInfoView.setLines(num);
	}

	public void setMultiInfoLineNum(int num) {
		mMutiInfoView.setLines(num);
	}

	// 正文 提示居左
	public void setMutiInfo(String info) {
		mMutiInfoView.setText(info);
		mMutiInfoView.setVisibility(View.VISIBLE);
	}

	// 左按钮文字
	public void setLeftButtonText(String info) {
		mCancleView.setText(info);
	}

	// 右按钮文字
	public void setRightButtonText(String info) {
		mSureView.setText(info);
	}

	/**
	 * 左按钮为取消功能时可以打开该选项，按钮文字置灰，降低选择倾向性
	 */
	public void setLeftButtonCancelStyle() {
		mCancleView.setTextColor(0xFF393939);
	}

	public void hideRightAndLeftButton() {
		mButtonLayout.setVisibility(View.GONE);
		// mSureView.setVisibility(View.INVISIBLE);
		// mCancleView.setVisibility(View.INVISIBLE);
	}

	public void hideRightButton() {
		mSureView.setVisibility(View.GONE);
	}

	public void hideLeftButton() {
        View divider = findViewById(R.id.divider_btn_middle);
        if (divider != null) {
            divider.setVisibility(View.GONE);
        }
		mCancleView.setVisibility(View.GONE);
	}

	public void setLeftBtnClickAble(boolean clickAble) {
		mCancleView.setClickable(clickAble);
		if (clickAble) {
			mCancleView.setAlpha(1f);
		} else {
			mCancleView.setAlpha(0.6f);
		}
	}

	public void setRightBtnClickAble(boolean clickAble) {
		mSureView.setClickable(clickAble);
		if (clickAble) {
			mSureView.setAlpha(1f);
		} else {
			mSureView.setAlpha(0.6f);
		}
	}

    public void hideTitle() {
        mTitleView.setVisibility(View.GONE);
    }

    public void showTitle() {
        mTitleView.setVisibility(View.VISIBLE);
    }

	public void showEditText() {
		if (mContentWatcher != null) {
			mEditText.addTextChangedListener(mContentWatcher);
		}
		mInfoView.setVisibility(View.GONE);
		mEditText.setVisibility(View.VISIBLE);
	}

	public void setPasswordEdit() {
		mEditText.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mEditText.setHint("请输入密码");
		mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				256) });
	}

	/*public void showCheckBoxLayout() {
		mCheckLayout.setVisibility(View.VISIBLE);
	}*/

	public void hideEditText() {
		mInfoView.setVisibility(View.VISIBLE);
		mEditText.setVisibility(View.GONE);
	}

	public String getEditTextInfo() {
		return mEditText.getText().toString().trim();
	}

	public void setEditTextInfo(String info) {
		mEditText.setText(info);
	}

	public DDEditText getEditText() {
		return mEditText;
	}

	// 书签
	public void setEditTextLines(int count) {
		mEditText.setSingleLine(false);
		mEditText.setLines(count);
		mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				1000) });
		mEditText.setBackgroundResource(R.drawable.mark_sub);
	}

	public void addTextChangedListener(TextWatcher mTextWatcher) {
		mContentWatcher = mTextWatcher;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean isFirstShow() {
		return isFirstShow;
	}

	public void setFirstShow(boolean isFirstShow) {
		this.isFirstShow = isFirstShow;
	}

	/**
	 * 展示对话框右上角的关闭按钮，默认是隐藏的
	 */
    public void showCloseRl() {
        mCloseLl.setVisibility(View.VISIBLE);

		// 关闭按钮的默认行为是关闭对话框
		mCloseLl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
    }

    /**
     * 设置文本行间距
     */
    public void setInfoLineSpace(float add, float mult) {
        if(mInfoView!=null)
            mInfoView.setLineSpacing(add,mult);
    }
    /**
     * 设置文本行间距
     */
    public void setInfoTextColor(int color) {
        if(mInfoView!=null)
            mInfoView.setTextColor(color);
    }
}
