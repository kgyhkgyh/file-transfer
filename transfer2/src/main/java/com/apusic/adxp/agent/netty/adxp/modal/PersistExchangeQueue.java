/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-2-28   comment
 * chenpengliang  2015-2-28  Created
 */
package com.apusic.adxp.agent.netty.adxp.modal;

import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 持久化发送任务队列
 * 
 * @author chenpengliang
 * 
 */
public class PersistExchangeQueue {


    /**
     * 内存任务队列
     */
    private Queue<FileReceiveTask> taskQueue;

    /**
     * 读写锁
     */
    private Lock lock = new ReentrantLock();
    

    /**
     * 增加任务
     * 
     * @param task
     * @throws Exception 
     */
    public void addTask(FileReceiveTask task) throws Exception {
            taskQueue.offer(task);
    }

    /**
     * 移除任务
     * 
     * @param task
     * @throws Exception 
     */
    public void removeTask(FileReceiveTask task) throws Exception {
        taskQueue.remove(task);
    }

    /**
     * 更新任务信息
     * 
     * @param fileTransferResponse
     */
    public void updateTask(FileReceiveTask fileTask, long position) throws Exception{
            for (FileReceiveTask rTask : taskQueue) {
                if (fileTask.getRelativeFilePath().equals(rTask.getRelativeFilePath())) {
                    rTask.setPosition(position);
                }
            }
    }

    /**
     * 获得最新任务
     * 
     * @return
     */
    public FileReceiveTask getTask() {
        return taskQueue.peek();
    }


}
