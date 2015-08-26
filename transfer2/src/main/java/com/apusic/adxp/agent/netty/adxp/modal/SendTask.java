/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-6   comment
 * chenpengliang  2015-3-6  Created
 */
package com.apusic.adxp.agent.netty.adxp.modal;


/**
 * @author chenpengliang
 *
 */
public class SendTask {

    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件大小
     */
    private long fileSize;
    
    /**
     * 片段位置
     */
    private long position;
    
    /**
     * 任务id
     */
    private String taskId;
    
    /**
     * 发送器
     */
    private Indicate indicate;

    /**
     * 
     */
    public SendTask() {
        // TODO Auto-generated constructor stub
    }

    public SendTask(String filePath, long fileSize, long position, String taskId, Indicate indicate) {
        super();
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.position = position;
        this.taskId = taskId;
        this.indicate = indicate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Indicate getIndicate() {
        return indicate;
    }

    public void setIndicate(Indicate indicate) {
        this.indicate = indicate;
    }
    
}
