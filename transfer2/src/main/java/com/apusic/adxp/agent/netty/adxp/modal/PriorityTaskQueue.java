/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-4-9   comment
 * chenpengliang  2015-4-9  Created
 */
package com.apusic.adxp.agent.netty.adxp.modal;

import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.util.concurrent.AtomicDouble;

/**
 * @author chenpengliang
 *
 */
public class PriorityTaskQueue {
    
    private final int TASK_SIZE = 5;

    private ArrayBlockingQueue<FileReceiveTask> currentTaskMap = new ArrayBlockingQueue<FileReceiveTask>(TASK_SIZE);
    
    private AtomicDouble minWeight = new AtomicDouble(0);
    
    public boolean isAddable(FileReceiveTask currentTask) {
        if(currentTaskMap.size() < TASK_SIZE || currentTask.getWeight() > minWeight.doubleValue()) {
            return true;
        }
        return false;
    }
    
    public FileReceiveTask addOrSwitchTask(FileReceiveTask currentTask) throws Exception{
        FileReceiveTask switchedTask = null;
        try {
            if(currentTaskMap.size() < TASK_SIZE) {
                currentTaskMap.put(currentTask);
            }else {
                for (FileReceiveTask task : currentTaskMap) {
                    if(task.getWeight() == minWeight.floatValue()) {
                        currentTaskMap.remove(task);
                        switchedTask = task;
                        currentTaskMap.put(currentTask);
                        break;
                    }
                }
            }
            calculateMinWeight();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return switchedTask;
    }
    
    public FileReceiveTask findTaskByTaskId(String taskId){
        FileReceiveTask fileTask = null;
        for (FileReceiveTask task : currentTaskMap) {
            if(task.getTaskId().equals(taskId)) {
                fileTask = task;
            }
        }
        return fileTask;
    }
    
    public boolean remove(FileReceiveTask task){
        return currentTaskMap.remove(task);
    }
    
    private void calculateMinWeight() {
        double min = Double.MAX_VALUE;
        for (FileReceiveTask task : currentTaskMap) {
            if(task.getWeight() < min) {
                min = task.getWeight();
            }
        }
        minWeight.set(min);
    }
}
