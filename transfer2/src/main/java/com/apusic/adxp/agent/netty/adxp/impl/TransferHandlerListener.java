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
public interface TransferHandlerListener {

    /**
     * 
     * @param transferHandler
     * @throws Exception
     */
    public void active(TransferHandler transferHandler) throws Exception;
    
    /**
     * 
     * @param transferHandler
     * @throws Exception
     */
    public void inActive(TransferHandler transferHandler) throws Exception;
    
    /**
     * 
     * @param transferHandler
     * @param msg
     * @throws Exception
     */
    public void receiveMsg(TransferHandler transferHandler, Object msg) throws Exception;
    
    /**
     * @param targetCode
     * @param msg
     * @throws Exception
     */
    public boolean send(String targetCode, Object msg) throws Exception;
    
}
