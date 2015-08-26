/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-3   comment
 * chenpengliang  2015-3-3  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

import java.io.Serializable;

/**
 * @author chenpengliang
 *
 */
@SuppressWarnings("serial")
public class Segment implements Comparable<Segment>, Serializable {

    /**
     * 位置
     */
    private long position;
    
    /**
     * 大小
     */
    private int size;
    
    /**
     * 
     */
    private String md5;

    /**
     * 
     */
    public Segment() {
        // TODO Auto-generated constructor stub
    }
    

    public Segment(long position, int size, String md5) {
        super();
        this.position = position;
        this.size = size;
        this.md5 = md5;
    }


    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Segment o) {
        return (int) (this.position - o.position);
    }


    public String getMd5() {
        return md5;
    }


    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof Segment) {
            Segment s =(Segment)obj;
            return this.position == s.position && this.md5.equals(s.md5);
        }
        return false;
    }
    
}
