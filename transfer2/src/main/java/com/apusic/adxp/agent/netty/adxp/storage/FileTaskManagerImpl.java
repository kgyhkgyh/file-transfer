/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-9   comment
 * chenpengliang  2015-3-9  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FilenameUtils;

import com.apusic.adxp.agent.netty.adxp.FileTaskCallback;
import com.apusic.adxp.agent.netty.adxp.modal.FileReceiveTask;
import com.apusic.adxp.agent.netty.adxp.modal.Indicate;
import com.apusic.adxp.agent.netty.adxp.modal.PriorityTaskQueue;
import com.apusic.adxp.agent.netty.adxp.protocal.FileCheckListRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileCheckListResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskRequest;

/**
 * @author chenpengliang
 * 
 */
public class FileTaskManagerImpl implements FileTaskManager, Runnable {

    /**
     * 工作目录
     */
    private String baseDir;

    /**
     * 持久化任务队列
     */
    private PriorityBlockingQueue<FileReceiveTask> taskQueue = null;
    
    /**
     * 当前接收队列
     */
    private PriorityTaskQueue currentQueue = new PriorityTaskQueue();
    

    /* 从等待队列中取得任务并发送
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while(true) {
            try {
                FileReceiveTask currentTask = taskQueue.take();
                boolean flag = currentQueue.isAddable(currentTask);
                if(flag) {
                    FileReceiveTask switchedTask = currentQueue.addOrSwitchTask(currentTask);
                    if(switchedTask != null) {
                        taskQueue.put(switchedTask);
                    }
                    
                    FileTransferRequest req = new FileTransferRequest();
                    req.setFilePath(currentTask.getSourceDir() + "/" +currentTask.getRelativeFilePath());
                    req.setFileSize(currentTask.getFileSize());
                    req.setTaskId(currentTask.getTaskId());
                    Indicate indicate = currentTask.getIndicate();
                    indicate.sendMsg(req);
                }else {
                    taskQueue.put(currentTask);
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorageManager#addFileStorageTask(java.lang.String, java.lang.String, long)
     */
    public FileReceiveTask addFileStorageTask(String targetDir, FileTransferTaskRequest req, Indicate indicate) throws Exception {
        FileReceiveTask fileTask = null;
        try {
            String filePath = FilenameUtils.concat(targetDir, req.getFilePath());
            File file = new File(filePath);
            File parentFile = file.getParentFile();
            if(!parentFile.exists()) {
                parentFile.mkdirs();
            }
            FileStorage fileStorage = FileStorageFactory.createFileStorage(file.getAbsolutePath(), req.getMd5(), req.getFileSize(), req.getSize());
            fileTask = new FileReceiveTask();
            fileTask.setRelativeFilePath(req.getFilePath());
            fileTask.setFileSize(req.getFileSize());
            fileTask.setTargetCode(req.getTarget());
            fileTask.setPosition(0);
            fileTask.setMd5(req.getMd5());
            fileTask.setOpts(req.getMd5());
            fileTask.setCmdId(req.getCmdId());
            fileTask.setInstanceId(UUID.randomUUID().toString());
            fileTask.setExchangeType(req.getTaskType());
            fileTask.setTargetFilePath(filePath);
            fileTask.setFileStorage(fileStorage);
            fileTask.setTarget(req.getTarget());
            fileTask.setTargetDir(targetDir);
            fileTask.setSourceDir(req.getSourceDir());
            fileTask.setSource(req.getSource());
            fileTask.setWeight(req.getWeight());
            fileTask.setTaskId(file.getAbsolutePath());
            fileTask.setIndicate(indicate);
            taskQueue.put(fileTask);
            System.out.println("任务加入等待队列"+fileTask.getTaskId());
        } catch (Exception e) {
            throw new Exception(e);
        }
        return fileTask;
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorageManager#handleFileTask(com.apusic.adxp.FileTask)
     */
    public void handleFileTask(long position, long size, byte[] content, String taskId, FileTaskCallback callback) throws Exception {
        try {
            FileReceiveTask fileTask = null;
            fileTask = currentQueue.findTaskByTaskId(taskId);
            if (fileTask != null) {
                FileStorage fileStorage = fileTask.getFileStorage();
                fileStorage.write(position, size, content);
                fileTask.setPosition(position);

                if (fileStorage.finished()) {
                    if (fileStorage.success()) {
                        currentQueue.remove(fileTask);
                        System.out.println("任务完成"+ taskId);
                        if(callback != null) {
                            callback.onFinished(fileTask);
                        }
                    }else {
                        if(callback != null) {
                            System.out.println("校验不通过，需要重新发送");
                            FileCheckListRequest checkListRequset = fileStorage.genCheckList();
                            checkListRequset.setCode(fileTask.getSource());
                            checkListRequset.setFixSize(fileStorage.getFixSize());
                            checkListRequset.setSize((int) size);
                            checkListRequset.setFilePath(FilenameUtils.concat(fileTask.getSourceDir(), fileTask.getRelativeFilePath()));
                            checkListRequset.setTaskId(taskId);
                            callback.onReSend(checkListRequset);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    
    public void resetFileTask(FileCheckListResponse resp, String taskId) {
        FileReceiveTask fileTask = null;
        fileTask = currentQueue.findTaskByTaskId(taskId);
        if(fileTask != null) {
            FileStorage fileStorage = fileTask.getFileStorage();
            List<Long> checkList = resp.getCheckList();
            fileStorage.reset(checkList);
        }
    }

    public FileReceiveTask getFileTask() {
        return taskQueue.peek();
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorageManager#setBaseDir(java.lang.String)
     */
    public void init(String baseDir) {
        this.baseDir = baseDir;
        
        taskQueue = new PriorityBlockingQueue<FileReceiveTask>(10, new Comparator<FileReceiveTask>() {
            public int compare(FileReceiveTask o1, FileReceiveTask o2) {
                return Float.compare(o2.getWeight(), o1.getWeight());
             }
         });
        FileStorageFactory.initBaseDir(baseDir);
        new Thread(this).start();
    }

}
