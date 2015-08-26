/*$Id: $
*--------------------------------------
* Apusic (Kingdee Middleware)
*--------------------------------------
* Copyright By Apusic ,All right Reserved
* author date comment
* liudongyang 2015-2-4 Created
*/

package com.apusic.adxp.agent.netty.adxp.modal;

import com.apusic.adxp.agent.netty.adxp.storage.FileStorage;

public class FileReceiveTask {

    /**
     * 来源地 
     */
    private String source;
    
    /**
     * 目标地
     */
    private String target;
    
    /**
     * 文件名
     */
    private String relativeFilePath;
    
    /**
     * 
     */
    private String targetFilePath;
    
    /**
     * 
     */
    private String targetCode;
    
    /**
     * 命令id
     */
    private String cmdId;
    
    /**
     * 流程运行唯一id
     */
    private String instanceId;
    
    /**
     * 任务类型
     */
    private String exchangeType;
    
    /**
     * 任务权重 
     */
    private float weight;
    
    /**
     * 文件大小
     */
    private long fileSize;
    
    /**
     * MD5 值
     */
    private String md5;
    
    /**
     * 目标路径
     */
    private String targetDir;
    
    /**
     * 源文件路径
     */
    private String sourceDir;
    
    /**
     * 传输任务唯一
     */
    private String taskId;
    
    /**
     * 操作
     */
    private String opts;
    
    /**
     * 当前传输位置 
     */
    private long position;
    
    /**
     * 
     */
    private FileStorage fileStorage;
    
    /**
     * 
     */
    private Indicate indicate;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getTargetFilePath() {
        return targetFilePath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public Indicate getIndicate() {
        return indicate;
    }

    public void setIndicate(Indicate indicate) {
        this.indicate = indicate;
    }

    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    public void setRelativeFilePath(String relativeFilePath) {
        this.relativeFilePath = relativeFilePath;
    }

}
