/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-6   comment
 * linxueqin  2015-2-6  Created
 */
package com.apusic.adxp.agent.netty.adxp;


/**
 * @author linxueqin
 *
 */
public interface Indicate {

    /**
     * @return
     */
    public String getCode();
    
    /**
     * @param msg
     * @throws Exception
     */
    public void sendMsg(Object msg) throws Exception;
    
    /**
     * @param targetCode
     * @param msg
     * @throws Exception
     */
    public void sendMsg(String targetCode, Object msg) throws Exception;
    
}
