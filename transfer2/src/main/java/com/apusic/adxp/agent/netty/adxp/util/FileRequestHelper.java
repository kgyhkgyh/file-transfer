/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-6   comment
 * chenpengliang  2015-3-6  Created
 */
package com.apusic.adxp.agent.netty.adxp.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.apusic.adxp.agent.netty.adxp.modal.Indicate;
import com.apusic.adxp.agent.netty.adxp.modal.SendTask;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.IdleSignal;

/**
 * @author chenpengliang
 * 
 */
public class FileRequestHelper implements Runnable {

    /**
     * 发送任务列表
     */
    private List<SendTask> sendTaskList = Collections.synchronizedList(new ArrayList<SendTask>());
    
    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    /**
     * 
     */
    public FileRequestHelper() {
        // TODO Auto-generated constructor stub
    }

    public void init() {
        new Thread(this).start();
    }

    public void setSendTask(SendTask sendTask) {
        try {
            lock.lock();
            try {
                condition.signal();
                //TODO 优先级插入
                sendTaskList.add(0, sendTask);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            try {
                if (sendTaskList.isEmpty()) {
                    lock.lock();
                    try {
                        condition.await();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    RandomAccessFile raf = null;
                    try {
                        //取出优先级最高任务
                        SendTask currentSendTask = sendTaskList.get(0);
                        //执行分片
                        String filePath = currentSendTask.getFilePath();
                        Indicate indicate = currentSendTask.getIndicate();
                        File file = new File(filePath);
                        if(file.exists()) {
                            raf = new RandomAccessFile(file, "r");
                            boolean done = false;
                            long position = currentSendTask.getPosition();
                            long fileSize = currentSendTask.getFileSize();
                            int size = 100 * 1024;
                            byte[] b = null;
                            if (position + size >= fileSize) {
                                size = (int) (fileSize - position);
                                done = true;
                            }
                            b = new byte[size];
                            raf.seek(position);
                            raf.read(b);
                            
                            //拼装成响应
                            FileTransferResponse fileResp = new FileTransferResponse();
                            fileResp.setFileSize(fileSize);
                            fileResp.setFilePath(filePath);
                            fileResp.setSize(size);
                            fileResp.setContent(b);
                            fileResp.setPosition(position);
                            fileResp.setTaskId(currentSendTask.getTaskId());
                            indicate.sendMsg(fileResp);
                            position += size;
                            currentSendTask.setPosition(position);
                            if(done) {
                                sendTaskList.remove(0);
//                                if(sendTaskList.size() != 0) {
//                                    currentSendTask = sendTaskList.get(0);
//                                } else {
//                                    indicate.sendMsg(new IdleSignal());
//                                }
                            }
                        }else {
                            sendTaskList.remove(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        raf.close();
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

}
