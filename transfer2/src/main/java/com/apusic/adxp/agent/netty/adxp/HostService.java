/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-28   comment
 * linxueqin  2015-2-28  Created
 */
package com.apusic.adxp.agent.netty.adxp;


/**
 * 主机服务
 * @author linxueqin
 *
 */
public interface HostService {

    /**
     * 主机Id
     * @param hostId
     * @return
     */
    public Host findHost(String hostId);
    
}
