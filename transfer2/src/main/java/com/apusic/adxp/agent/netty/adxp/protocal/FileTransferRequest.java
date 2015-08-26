/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-1-27   comment
 * linxueqin  2015-1-27  Created
 */
package com.apusic.adxp.agent.netty.adxp.protocal;

import java.io.Serializable;

/**
 * 文件传输请求
 * @author linxueqin
 *
 */
@SuppressWarnings("serial")
public class FileTransferRequest implements Serializable {

    /**
     * 来源的编码 
     */
    protected String code;
    
    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件大小
     */
    private long fileSize;
    
    /**
     * 文件位置
     */
    private long position;
    
    /**
     * 命令id
     */
    private String cmdId;
    
    /**
     * 传输任务唯一id
     */
    private String taskId;

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the position
     */
    public long getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(long position) {
        this.position = position;
    }
    
    
}
