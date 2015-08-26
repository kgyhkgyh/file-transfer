/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-6   comment
 * linxueqin  2015-2-6  Created
 */
package com.apusic.adxp.agent.netty.adxp.protocal;

import java.io.Serializable;

/**
 * @author linxueqin
 *
 */
@SuppressWarnings("serial")
public class Introduction implements Serializable {

    private String code;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    
}
