/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-4-7   comment
 * chenpengliang  2015-4-7  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import com.apusic.adxp.agent.netty.adxp.modal.FileReceiveTask;
import com.apusic.adxp.agent.netty.adxp.protocal.FileCheckListRequest;

/**
 * @author chenpengliang
 *
 */
public interface FileTaskCallback {

    public void onFinished(FileReceiveTask fileTask)  throws Exception;
    
    public void onReSend(FileCheckListRequest req)  throws Exception;
    
}
