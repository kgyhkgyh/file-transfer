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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.apusic.adxp.agent.netty.adxp.FileReceiveTask;
import com.apusic.adxp.agent.netty.adxp.PersistExchangeQueue;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskRequest;

/**
 * @author chenpengliang
 * 
 */
public class FileTaskManagerImpl implements FileTaskManager {

    /**
     * 工作目录
     */
    private String baseDir;

    /**
     * 任务列表
     */
    private Map<String, FileReceiveTask> taskMap = new ConcurrentHashMap<String, FileReceiveTask>();

    /**
     * 持久化任务队列
     */
    private PersistExchangeQueue taskQueue = null;

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorageManager#addFileStorageTask(java.lang.String, java.lang.String, long)
     */
    public FileReceiveTask addFileStorageTask(String targetDir, FileTransferTaskRequest req) throws Exception {
        FileReceiveTask fileTask = null;
        String filePath = req.getFilePath();
        try {
            // 判断文件是否有下级目录,如果有就拆分 如果没有就直接生成
            int dirIndex = filePath.lastIndexOf(File.separator);
            String dirPath = "";
            String fileName = filePath;
            if (dirIndex != -1) {
                dirPath = filePath.substring(0, dirIndex);
                fileName = filePath.substring(dirIndex, filePath.length());
            }
            FileStorage fileStorage = FileStorageFactory.createFileStorage(fileName, targetDir, dirPath, req.getMd5(), req.getFileSize());
            fileTask = new FileReceiveTask();
            fileTask.setFileName(filePath);
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
            fileTask.setTaskId(fileName);
            taskQueue.addTask(fileTask);
            taskMap.put(fileName, fileTask);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return fileTask;
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorageManager#handleFileTask(com.apusic.adxp.FileTask)
     */
    public FileReceiveTask handleFileTask(long position, long size, byte[] content, String taskId) throws Exception {
        try {
            FileReceiveTask fileTask = taskMap.get(taskId);
            if (fileTask != null) {
                FileStorage fileStorage = fileTask.getFileStorage();
                fileStorage.write(position, size, content);
                taskQueue.updateTask(fileTask, position);

                long fileSize = fileTask.getFileSize();

                if (position + size >= fileSize) {
                    if (fileStorage.success()) {
                        // fileStorage.transferToTarget();
                        System.out.println("-------------->任务移除"+fileTask.getCmdId());
                        taskQueue.removeTask(fileTask);
                        taskMap.remove(taskId);
                        return fileTask;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    }

    public FileReceiveTask getFileTask() {
        return taskQueue.getTask();
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorageManager#setBaseDir(java.lang.String)
     */
    public void init(String baseDir) {
            this.baseDir = baseDir;
        FileStorageFactory.initBaseDir(baseDir);
    }

}
