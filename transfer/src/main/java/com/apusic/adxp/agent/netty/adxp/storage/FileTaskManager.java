/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-9   comment
 * chenpengliang  2015-3-9  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

import com.apusic.adxp.agent.netty.adxp.FileReceiveTask;
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
    public FileReceiveTask addFileStorageTask(String targetDir, FileTransferTaskRequest req)  throws Exception ;
    
    
    /**
     * @param position
     * @param size
     * @param content
     * @param taskId
     * @return FileReceiveTask
     */
    public FileReceiveTask handleFileTask(long position, long size, byte[] content, String taskId)  throws Exception ;

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
    
}
