package com.dangdang.gx.ui.TaskStarter;

import android.os.Looper;
import android.os.MessageQueue;
import java.util.LinkedList;
import java.util.Queue;


///延迟加载器
public class IldeTaskManager {

    private Queue<Task> ildeTaskQueue = new LinkedList<>();

    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler(){

        @Override
        public boolean queueIdle() {

            if(ildeTaskQueue.size() > 0){

                // 如果CPU空闲了，
                Task idleTask = ildeTaskQueue.poll();
                new TaskRunnable(idleTask).run();
            }
            // 如果返回false，则移除该 IldeHandler
            return !ildeTaskQueue.isEmpty();
        }
    };

    public IldeTaskManager addTask(Task task){

        ildeTaskQueue.add(task);
        return this;
    }

    /**
     * 执行空闲方法，因为用了DispatchRunnable，所以会优先处理需要依赖的task，再处理本次需要处理的task，顺序执行
     * */
    public void start(){
        Looper.myQueue().addIdleHandler(idleHandler);
    }
}
