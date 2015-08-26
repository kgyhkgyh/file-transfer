/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-5   comment
 * chenpengliang  2015-3-5  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import com.apusic.adxp.agent.netty.adxp.storage.FileStorage;

/**
 * @author chenpengliang
 *
 */
public class FileTask {

    private String fileName;
    
    private String targetFilePath;
    
    private long fileSize;
    
    private long position;
    
    private String targetCode;
    
    private String md5;
    
    private FileStorage fileStorage;
    
    /**
     * 
     */
    public FileTask() {
        // TODO Auto-generated constructor stub
    }

    public FileTask(String fileName, String targetFilePath, long fileSize, long position, String targetCode, String md5) {
        super();
        this.fileName = fileName;
        this.targetFilePath = targetFilePath;
        this.fileSize = fileSize;
        this.position = position;
        this.targetCode = targetCode;
        this.md5 = md5;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getTargetFilePath() {
        return targetFilePath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    } 

}
