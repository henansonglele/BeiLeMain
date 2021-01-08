package com.dangdang.gx.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dangdang.gx.R;
import java.util.ArrayList;
import java.util.List;

public class CustomBottomLayout extends FrameLayout {

    private boolean showAni = false;
    private Context context;
    private BottomClickListener listener;
    private int current = 0;//默认选中0
    private List<Drawable> normalIconList = new ArrayList<>();
    private List<Drawable> selIconList = new ArrayList<>();
    private List<AnimationDrawable> normalAnimationList = new ArrayList<>();
    private List<AnimationDrawable> selectedAnimationList = new ArrayList<>();
    private List<DDImageView> tabIvList = new ArrayList<>();
    private List<DDTextView> tabTitleList = new ArrayList<>();

    public CustomBottomLayout(@NonNull Context context) {
        this(context,null);

    }
    public CustomBottomLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBottomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context, attrs);
        intView();
        initListener();
    }
    /**
     * 初始化自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypedAttay = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomLayout);
        showAni = mTypedAttay.getBoolean(R.styleable.CustomBottomLayout_showAni, true);
        mTypedAttay.recycle();
    }

    void intView(){
        LayoutInflater.from(context).inflate(R.layout.bottom_tab_layout,this);
        initListView();
        initAnimation();
    }

    private void initListView() {
        DDImageView stroeIv = (DDImageView)findViewById(R.id.tab_store_iv);
        DDImageView typeIv = (DDImageView)findViewById(R.id.tab_type_iv);
        DDImageView shelfIv = (DDImageView)findViewById(R.id.tab_shelf_iv);
        DDImageView circleIv = (DDImageView)findViewById(R.id.tab_circle_iv);
        DDImageView personalIv = (DDImageView)findViewById(R.id.tab_personal_iv);


        DDTextView stroeTv = (DDTextView)findViewById(R.id.tab_store_tv);
        DDTextView typeTv = (DDTextView)findViewById(R.id.tab_type_tv);
        DDTextView shelfTv = (DDTextView)findViewById(R.id.tab_shelf_tv);
        DDTextView circleTv = (DDTextView)findViewById(R.id.tab_circle_tv);
        DDTextView personalTv = (DDTextView)findViewById(R.id.tab_personal_tv);

        tabIvList.add(stroeIv);
        tabIvList.add(typeIv);
        tabIvList.add(shelfIv);
        tabIvList.add(circleIv);
        tabIvList.add(personalIv);


        tabTitleList.add(stroeTv);
        tabTitleList.add(typeTv);
        tabTitleList.add(shelfTv);
        tabTitleList.add(circleTv);
        tabTitleList.add(personalTv);

    }


    private void initAnimation() {


        if(showAni){
            normalAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_store_nomal));
            normalAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_type_nomal));
            normalAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_shelf_nomal));
            normalAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_circle_nomal));
            normalAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_personal_nomal));


            selectedAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_store_selected));
            selectedAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_type_selected));
            selectedAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_shelf_selected));
            selectedAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_circle_selected));
            selectedAnimationList.add(
                    (AnimationDrawable) getResources().getDrawable(R.drawable.anim_personal_selected));
        }else{
            normalIconList.add(getResources().getDrawable(R.drawable.home_store_unsel));
            normalIconList.add(getResources().getDrawable(R.drawable.home_type_unsel));
            normalIconList.add(getResources().getDrawable(R.drawable.home_shelf_unsel ));
            normalIconList.add(getResources().getDrawable(R.drawable.home_circle_unsel));
            normalIconList.add(getResources().getDrawable(R.drawable.home_personal_unsel));

            selIconList.add(getResources().getDrawable(R.drawable.home_store_sel));
            selIconList.add(getResources().getDrawable(R.drawable.home_type_sel));
            selIconList.add(getResources().getDrawable(R.drawable.home_shelf_sel ));
            selIconList.add(getResources().getDrawable(R.drawable.home_circle_sel));
            selIconList.add(getResources().getDrawable(R.drawable.home_personal_sel));
        }


    }
    void initListener(){
        findViewById(R.id.tab_store_rl).setOnClickListener(mlistener);
        findViewById(R.id.tab_type_rl).setOnClickListener(mlistener);
        findViewById(R.id.tab_shelf_rl).setOnClickListener(mlistener);
        findViewById(R.id.tab_circle_rl).setOnClickListener(mlistener);
        findViewById(R.id.tab_personal_rl).setOnClickListener(mlistener);
    }
    OnClickListener mlistener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.tab_store_rl:
                    if(listener!=null)
                        listener.onItemClick(0);
                    break;
                case R.id.tab_type_rl:
                    if(listener!=null)
                        listener.onItemClick(1);
                    break;
                case R.id.tab_shelf_rl:
                    if(listener!=null)
                        listener.onItemClick(2);
                    break;
                case R.id.tab_circle_rl:
                    if(listener!=null)
                        listener.onItemClick(3);
                    break;
                case R.id.tab_personal_rl:
                    if(listener!=null)
                        listener.onItemClick(4);
                    break;
            }

        }
    };

    public void setCurrent(int index){
        if(index == current)
            return;
        updageUI(index);
        current = index;
    }
    private void updageUI(int index){
        if(index<0)
            return;
        if(showAni){
            tabIvList.get(current).setImageDrawable(normalAnimationList.get(current));
            normalAnimationList.get(current).start();

            tabIvList.get(index).setImageDrawable(selectedAnimationList.get(index));
            selectedAnimationList.get(index).start();

        }else{
            tabIvList.get(current).setImageDrawable(normalIconList.get(current));
            tabIvList.get(index).setImageDrawable(selIconList.get(index));
        }
        tabTitleList.get(current).setSelected(false);
        tabTitleList.get(index).setSelected(true);

    }
    public void setListener(BottomClickListener listener){
        this.listener = listener;
    }

    public interface  BottomClickListener{
        void onItemClick(int i);
    }
}
