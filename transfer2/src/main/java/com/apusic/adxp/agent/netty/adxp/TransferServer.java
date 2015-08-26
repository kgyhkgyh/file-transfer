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
public interface TransferServer {

    /**
     * 
     * @param code
     * @param port
     * @param transferListener
     * @throws Exception
     */
    public void start(final String code, int port, TransferListener transferListener) throws Exception;
    
    /**
     * @throws Exception
     */
    public void stop() throws Exception;
    
    /**
     * @param targetCode
     * @param msg
     * @throws Exception
     */
    public boolean send(String targetCode, Object msg) throws Exception;
    
    /**
     * @param hostService
     * @throws Exception
     */
    public void setHostService(HostService hostService) throws Exception;
}
