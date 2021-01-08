package com.dangdang.gx.ui.flutterbase;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dangdang.gx.R;
import com.idlefish.flutterboost.containers.BoostFlutterActivity;
import java.lang.reflect.Method;
import java.util.Map;

import static com.dangdang.gx.ui.flutterbase.DDFlutterConst.PARAMS_KEY_SHOW_BOTTOM_DIALOG;
import static com.dangdang.gx.ui.flutterbase.DDFlutterConst.PARAMS_VALUE_SHOW_BOTTOM_DIALOG;

/**
 * Created by liuzhongtao on 2019/9/11.
 */
public class DDFlutterActivity extends BoostFlutterActivity {
    public static final String ANIM_OFF = "anim_off";
    private boolean showBottomDialog = false;

    //关闭界面动画是否关闭
    public static final String OUT_ANIM_OFF = "out_anim_off";
    public static final String OUT_ANIM_KEY = "out_anim";
    //关闭界面动画是否关闭 false 动画不关闭，true 动画关闭
    private boolean closeOutAnim = false;

    public static void launch(Activity activity,
                              @NonNull String url,
                              Map<String, Object> params,
                              int requestCode) {
        SerializableMap serializableMap = new SerializableMap();
        serializableMap.setMap(params);

        Intent intent = new Intent(activity, DDFlutterActivity.class)
                .putExtra(EXTRA_BACKGROUND_MODE, BackgroundMode.opaque.name())
                .putExtra(EXTRA_DESTROY_ENGINE_WITH_ACTIVITY, false)
                .putExtra(EXTRA_URL, url)
                .putExtra(EXTRA_PARAMS, serializableMap);

        if (requestCode > 0) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }
    //包外部获取 BoostFlutterActivity的参数
    public static String getExtraDestroyEngineWithActivity(){
        return  EXTRA_DESTROY_ENGINE_WITH_ACTIVITY;
    }
    public static String getExtraBackgoundMode(){
        return  EXTRA_BACKGROUND_MODE;
    }
    public static String geExtratUrl(){
        return  EXTRA_URL;
    }
    public static String getExtraParam(){
        return  EXTRA_PARAMS;
    }


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean showInAnim = true;
        if (getContainerUrlParams() != null) {
            if (ANIM_OFF.equals(getContainerUrlParams().get("showInAnim"))) {
                showInAnim = false;
            }
            if (OUT_ANIM_OFF.equals(getContainerUrlParams().get(OUT_ANIM_KEY))){
                closeOutAnim = true;
            }
            if (PARAMS_VALUE_SHOW_BOTTOM_DIALOG.equals(getContainerUrlParams().get(PARAMS_KEY_SHOW_BOTTOM_DIALOG))) {
                showBottomDialog = true;
            }
        }
        if (showInAnim) {
            overridePendingTransition(R.anim.book_review_activity_in,
                    R.anim.book_review_group_activity_out);
        } else {
            overridePendingTransition(0, 0);
        }

        if(showBottomDialog){
            setBottomDialogStyle();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if(showBottomDialog || closeOutAnim){
            overridePendingTransition(0, 0);
        }else{
            overridePendingTransition(R.anim.book_review_group_activity_in,
                    R.anim.book_review_activity_out);
        }

    }

    private void setBottomDialogStyle(){
        //设置activity背景透明
        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().getDecorView().setBackground(null);
            Method activityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            activityOptions.setAccessible(true);
            Object options = activityOptions.invoke(DDFlutterActivity.this);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> aClass = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    aClass = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent", aClass, ActivityOptions.class);
            method.setAccessible(true);
            method.invoke(DDFlutterActivity.this, null, options);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //设置高度
        Window window = getWindow();
        if (window == null) return;
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;//DRUiUtility.getScreenHeight() * 3 / 4;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        overridePendingTransition(0, 0);
    }
}
