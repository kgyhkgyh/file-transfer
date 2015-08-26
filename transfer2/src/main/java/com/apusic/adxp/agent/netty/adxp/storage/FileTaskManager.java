/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-9   comment
 * chenpengliang  2015-3-9  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

import com.apusic.adxp.agent.netty.adxp.FileTaskCallback;
import com.apusic.adxp.agent.netty.adxp.modal.FileReceiveTask;
import com.apusic.adxp.agent.netty.adxp.modal.Indicate;
import com.apusic.adxp.agent.netty.adxp.protocal.FileCheckListResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskRequest;

/**
 * @author chenpengliang
 *
 */
public interface FileTaskManager {


    /**
     * @param filePath
     * @param md5
     * @param fileSize
     * @param targetCode
     * @return FileReceiveTask
     */
    public FileReceiveTask addFileStorageTask(String targetDir, FileTransferTaskRequest req, Indicate indicate)  throws Exception ;
    
    
    /**
     * @param position
     * @param size
     * @param content
     * @param taskId
     * @param callback
     * @throws Exception
     */
    public void handleFileTask(long position, long size, byte[] content, String taskId, FileTaskCallback callback)  throws Exception ;

    /**
     * 获取当前优先级最高任务
     * @return
     */
    public FileReceiveTask getFileTask();
    /**
     * 
     * @param baseDir
     * @param agentContext
     */
    public void init(String baseDir);
    
    /**
     * @param resp
     * @param taskId
     */
    public void resetFileTask(FileCheckListResponse resp, String taskId);
    
}
