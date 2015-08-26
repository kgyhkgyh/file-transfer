/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-3   comment
 * chenpengliang  2015-3-3  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

/**
 * @author chenpengliang
 *
 */
public class Segment implements Comparable<Segment> {

    /**
     * 位置
     */
    private long position;
    
    /**
     * 大小
     */
    private long size;

    /**
     * 
     */
    public Segment() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param position
     * @param size
     */
    public Segment(long position, long size) {
        super();
        this.position = position;
        this.size = size;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Segment o) {
        return (int) (this.position - o.position);
    }
    
}
