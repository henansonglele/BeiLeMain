package com.dangdang.gx.ui.TaskStarter;

import java.util.List;
import java.util.concurrent.Executor;

public interface ITask {

    void run();

    /**
     * Task执行所在的线程池，可指定，一般默认
     */
    Executor runOnExecutor();

    /**
     * 存放需要先执行的task任务集合(也就是添加需要先执行的依赖)
     */
    List<Class<? extends ITask>> dependentArr();

    /**
     * 开始锁
     * */
    void startLock();

    /**
     * 解锁
     * */
    void unlock();

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     *
     * @return
     */
    boolean needWait();

    /**
     * 是否在主线程执行
     */
    boolean runOnMainThread();

    /**
     * Task主任务执行完成之后需要执行的任务
     */
    Runnable getTailRunnable();
}
