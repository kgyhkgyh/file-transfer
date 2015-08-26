/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-1-20   comment
 * chenpengliang  2015-1-20  Created
 */
package com.apusic.adxp.agent.netty.adxp;

/**
 * @author chenpengliang
 * 
 */
public class AgentTransferTask {

    /**
     * id
     */
    private String id;

    /**
     * 文件名
     */
    private String relativeFilePath;

    /**
     * 
     */
    private String sourceDir;

    /**
     * 命令id
     */
    private String cmdId;
    
    /**
     * 流程运行唯一id
     */
    private String instanceId;

    /**
     * 目标前置机编码
     */
    private String targetCode;

    /**
     * 权重
     */
    private float weight;

    /**
     * 操作集合
     */
    private String opts;

    /**
     * 任务类型
     */
    private String taskType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    public void setRelativeFilePath(String relativeFilePath) {
        this.relativeFilePath = relativeFilePath;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getOpts() {
        return opts;
    }

    public void setOpts(String opts) {
        this.opts = opts;
    }
}
