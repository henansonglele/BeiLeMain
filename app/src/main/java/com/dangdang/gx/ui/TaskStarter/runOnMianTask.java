package com.dangdang.gx.ui.TaskStarter;

import java.util.List;

public class runOnMianTask extends Task {

    @Override
    public boolean needWait() {
        return false;
    }

    @Override
    public List<Class<? extends ITask>> dependentArr() {
        return null;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
            System.out.println("runOnMianTask运行完毕，它所在的线程是："+Thread.currentThread().getName());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean runOnMainThread(){
        return true;
    }
}
