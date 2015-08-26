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
 * @author linxueqin
 *
 */
@SuppressWarnings("serial")
public class FileTransferResponse implements Serializable  {

    /**
     * 来源的编码 
     */
    protected String code;
    
    /**
     * 文件路径
     */
    protected String filePath;
    
    /**
     * 目标目录
     */
    protected String targetDir;
    
    /**
     * 开始位置
     */
    protected long position;
    
    /**
     * 文件大小
     */
    protected long fileSize;
    
    /**
     * 内容大小
     */
    protected int size;
    
    /**
     * 任务唯一id
     */
    private String taskId;
    
    /**
     * 内容
     */
    private byte[] content;

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

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    
}
