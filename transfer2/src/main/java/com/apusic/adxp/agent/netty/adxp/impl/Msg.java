/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-9   comment
 * linxueqin  2015-2-9  Created
 */
package com.apusic.adxp.agent.netty.adxp.impl;

/**
 * @author linxueqin
 *
 */
public class Msg {

    private String targetCode;
    
    private Object content;

    /**
     * 
     */
    public Msg() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param targetCode
     * @param content
     */
    public Msg(String targetCode, Object content) {
        super();
        this.targetCode = targetCode;
        this.content = content;
    }
    
    
    /**
     * @return the targetCode
     */
    public String getTargetCode() {
        return targetCode;
    }


    /**
     * @param targetCode the targetCode to set
     */
    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }


    /**
     * @return the content
     */
    public Object getContent() {
        return content;
    }


    /**
     * @param content the content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

}
