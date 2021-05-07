package com.dangdang.gx.ui.TaskStarter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherExecutor {

    // 获得当前CPU的核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    // 设置线程池的核心线程数2-4之间,但是取决于CPU核数
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    // 创建线程池
    static  ExecutorService executorService /*= Executors.newFixedThreadPool(CORE_POOL_SIZE)*/;

    //实验证明，根据这个公式来算出的线程数是最合理的，至于为什么是这样算，不是全占用了CPU数是最好的吗?
    //表示不信作者的同学你就自己看下面的公式，欢迎来怼作者!

    static ExecutorService getIOExecutor(){
        return  executorService;
    }

    static ExecutorService getIOExecutor1(){
        if(executorService == null){
            synchronized (ExecutorService.class){
                if(executorService == null){
                    executorService = Executors.newFixedThreadPool(CORE_POOL_SIZE);
                }
            }

        }
        return executorService;
    }
}
