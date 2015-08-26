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
 * 交换监听
 * @author linxueqin
 *
 */
public interface TransferListener {

    /**
     * 有客户接入
     * @param indicate
     * @throws Exception
     */
    public void active(Indicate indicate) throws Exception;
    
    /**
     * 断开
     * @param indicate
     * @throws Exception
     */
    public void inActive(Indicate indicate) throws Exception;
    
    /**
     * 接收到消息
     * @param indicate
     * @param msg
     * @throws Exception
     */
    public void receiveMsg(Indicate indicate, Object msg) throws Exception;
    
    
}
