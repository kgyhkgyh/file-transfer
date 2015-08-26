/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-3   comment
 * chenpengliang  2015-3-3  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

import java.util.List;


public interface FileStorage {

    /**
     * 写文件
     * @param position
     * @param size
     * @param data
     * @throws Exception
     */
    public void write(long position, long size, byte[] data) throws Exception;
    
    /**
     * 文件接收是否成功
     * @return
     */
    public boolean success();
    
    /**
     * @return
     */
    public List<Segment> computeMissingSegments();
    
    /**
     * 
     */
    public void transferToTarget();
    
}
