package com.dangdang.gx.ui.TaskStarter.taskImp;

import com.dangdang.gx.ui.TaskStarter.ITask;
import com.dangdang.gx.ui.TaskStarter.Task;
import com.dangdang.gx.ui.log.LogM;
import java.util.List;

public class InitTest2Task extends Task {

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
            Thread.sleep(500);
            System.out.println("InitTest2Task运行完毕，它所在的线程是："+Thread.currentThread().getName());
            LogM.d("abd", getClass().getSimpleName()+"运行完毕，它所在的线程是："+Thread.currentThread().getName());

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
