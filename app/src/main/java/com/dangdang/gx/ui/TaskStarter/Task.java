package com.dangdang.gx.ui.TaskStarter;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public abstract class Task implements ITask {

    // 当前Task依赖的Task数量（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
    private CountDownLatch taskCountDownLatch = new CountDownLatch(dependentArr() == null ? 0 : dependentArr().size());

    /**
     * 当前Task等待，让依赖的Task先执行
     */
    @Override
    public void startLock() {
        try {
            taskCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 依赖的Task执行完一个
     */
    @Override
    public void unlock() {
        taskCountDownLatch.countDown();
    }

    /**
     * Task的优先级，运行在主线程则不要去改优先级
     */
    //@Override
    public int priority() {
        return  Thread.NORM_PRIORITY;
        //return Process.THREAD_PRIORITY_BACKGROUND;
    }

    /**
     * Task执行在哪个线程池，默认在IO的线程池；
     */
    @Override
    public ExecutorService runOnExecutor() {
       //return Executors.newFixedThreadPool(3);
        return DispatcherExecutor.getIOExecutor1();
    }

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     */
    @Override
    public boolean needWait() {
        return false;
    }

    /**
     * 当前Task依赖的Task集合（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
     */
    @Override
    public List<Class<? extends ITask>> dependentArr() {
        return null;
    }

    @Override
    public boolean runOnMainThread() {
        return false;
    }

    @Override
    public Runnable getTailRunnable() {
        return null;
    }

    public boolean needRunAsSoon() {
        return false;
    }
}

