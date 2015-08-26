/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-4-3   comment
 * chenpengliang  2015-4-3  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import com.apusic.adxp.agent.netty.adxp.impl.TransferServerImpl;

/**
 * @author chenpengliang
 *
 */
public class TestAgent2 {

    public static void main(String[] args) throws Exception {
        TransferServerImpl transferServer = new TransferServerImpl();
        transferServer.setHostService(new HostService() {
            
            public Host findHost(String hostId) {
                Host host = new Host();
                host.setIp("127.0.0.1");
                host.setPort("agent1".equals(hostId) ? 8080 : ("agent2".equals(hostId) ? 8081 : 8082));
                return host;
            }
        });
        transferServer.start("agent2", 8081, new FileTransferListener("agent2", "D:\\adxp-workspace\\embed\\"));
    }
    
}
