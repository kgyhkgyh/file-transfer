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

/**
 * @author chenpengliang
 *
 */
@SuppressWarnings("serial")
public class FileCheckListResponse implements Serializable {

    private String taskId;
    
    private List<Long> checkList;

    /**
     * 
     */
    public FileCheckListResponse() {
        // TODO Auto-generated constructor stub
    }
    
    public FileCheckListResponse(String taskId, List<Long> checkList) {
        super();
        this.taskId = taskId;
        this.checkList = checkList;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Long> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Long> checkList) {
        this.checkList = checkList;
    }
    
    
}
