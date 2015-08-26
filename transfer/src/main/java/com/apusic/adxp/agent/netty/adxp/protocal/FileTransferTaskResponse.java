/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-1-20   comment
 * linxueqin  2015-1-20  Created
 */
package com.apusic.adxp.agent.netty.adxp.protocal;

import java.io.Serializable;

/**
 * 文件传输任务响应
 * @author linxueqin
 *
 */
@SuppressWarnings("serial")
public class FileTransferTaskResponse implements Serializable {

    /**
     * 来源地 
     */
    protected String source;
    
    /**
     * 目标地
     */
    protected String target;
    
    /**
     * 文件名
     */
    protected String filePath;
    
    /**
     * 任务类型
     */
    protected String taskType;
    
    /**
     * 任务权重 
     */
    protected float weight;
    
    /**
     * 文件大小
     */
    protected long fileSize;
    
    /**
     * MD5 值
     */
    protected String md5;
    
    /**
     * 流程运行唯一id
     */
    protected String instanceId;

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
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
     * @return the taskType
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * @param taskType the taskType to set
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    /**
     * @return the weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(float weight) {
        this.weight = weight;
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
     * @return the md5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * @param md5 the md5 to set
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

}
