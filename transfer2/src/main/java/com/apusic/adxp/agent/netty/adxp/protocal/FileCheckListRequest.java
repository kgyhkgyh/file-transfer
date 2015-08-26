/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-4-7   comment
 * chenpengliang  2015-4-7  Created
 */
package com.apusic.adxp.agent.netty.adxp.protocal;

import java.io.Serializable;
import java.util.List;

import com.apusic.adxp.agent.netty.adxp.storage.Segment;

/**
 * @author chenpengliang
 *
 */
@SuppressWarnings("serial")
public class FileCheckListRequest implements Serializable{

    private String code;
    
    private String filePath;
    
    private int size;
    
    private int fixSize;
    
    private List<Segment> checkList;
    
    private String taskId;
    
    /**
     * 
     */
    public FileCheckListRequest() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param code
     * @param filePath
     * @param size
     * @param checkList
     */
    public FileCheckListRequest(String code, String filePath, int size, List<Segment> checkList) {
        super();
        this.code = code;
        this.filePath = filePath;
        this.size = size;
        this.checkList = checkList;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Segment> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Segment> checkList) {
        this.checkList = checkList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getFixSize() {
        return fixSize;
    }

    public void setFixSize(int fixSize) {
        this.fixSize = fixSize;
    }
    
    
}
