package com.dangdang.gx.ui.TaskStarter;

import java.util.ArrayList;
import java.util.List;

public class InitJPushTask extends Task {

    @Override
    public boolean needWait() {
        return true;
    }

    @Override
    public List<Class<? extends ITask>> dependentArr() {
        List<Class<? extends ITask>> tasks = new ArrayList<>();
        tasks.add(InitShareTask.class);
        return tasks;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
            System.out.println("InitJPushTask运行完毕，它所在的线程是："+Thread.currentThread().getName());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
