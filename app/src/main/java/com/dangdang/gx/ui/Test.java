package com.dangdang.gx.ui;

import android.view.Choreographer;
import com.dangdang.gx.ui.log.LogM;

public class Test implements Choreographer.FrameCallback {
    static long lastTime = 0;
    static  Test singleStone = null;

    static Test getInstance(){
        if(singleStone == null){
            synchronized (Test.class){
                if(singleStone == null){
                    singleStone = new Test();
                }
            }
        }
        lastTime = System.currentTimeMillis();
        return  singleStone;
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        //大于48ms 默认丢帧。
        if((System.currentTimeMillis() - lastTime) > 48 ){
             LogM.d("abddz","丢帧"+(System.currentTimeMillis() - lastTime));
        }
        lastTime = System.currentTimeMillis();
        Choreographer.getInstance().postFrameCallback(Test.getInstance());
    }
}
