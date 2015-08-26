/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-10   comment
 * chenpengliang  2015-3-10  Created
 */
package com.apusic.adxp.agent.netty.adxp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenpengliang
 *
 */
public class MsgQueue {

    /**
     * 
     */
    private static Map<String, MsgQueue> msgQueues = Collections.synchronizedMap(new HashMap<String, MsgQueue>());
    
    /**
     * 
     */
    private static boolean wait = false;
    
    /**
     * 
     */
    private String targetCode;
    
    /**
     * 
     */
    protected TransferHandler transferHandler;
    
    /**
     * 
     */
    protected List<Object> msgs = Collections.synchronizedList(new ArrayList<Object>());
    
    /**
     * 
     */
    private static ReentrantLock lock = new ReentrantLock();
    
    /**
     * 
     */
    private static Condition condition = lock.newCondition();
    
    /**
     * @param targetCode
     * @return
     */
    public static MsgQueue createMsgQueue(String targetCode) {
        return createMsgQueue(targetCode, null);
    }
    
    /**
     * @param targetCode
     * @return
     */
    public static MsgQueue createMsgQueue(String targetCode, MsgQueue.QueueCreateCallback callback) {
        if (!msgQueues.containsKey(targetCode)) {
            synchronized (MsgQueue.class) {
                if (!msgQueues.containsKey(targetCode)) {
                    MsgQueue msgQueue = new MsgQueue(targetCode);
                    msgQueues.put(targetCode, msgQueue);
                }
            }
        }
        MsgQueue msgQueue = msgQueues.get(targetCode);
        if (msgQueue.transferHandler == null) {
            if (callback != null) {
                callback.onCreate(targetCode);
            }
        }
        return msgQueue;
    }
    
    
    /**
     * @param targetCode
     * @return
     */
    public static MsgQueue remove(String targetCode) {
        return msgQueues.remove(targetCode);
    }
    
    /**
     * 
     */
    public static void work() {
        wait = true;
        Collection<MsgQueue> items = msgQueues.values();
        for (MsgQueue msgQueue : items) {
            msgQueue.send();
        }
        if (wait) {
            lock.lock();
            try {
                condition.await(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // TODO 
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * 
     */
    private MsgQueue(String targetCode) {
        this.targetCode = targetCode;
    }
    
    /**
     * @param msg
     */
    public void putMsg(Object msg) {
        msgs.add(msg);
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 
     */
    public void send() {
        if (transferHandler != null && msgs.size() > 0) {
            Object msg = msgs.get(0);
            if (transferHandler.writeMsg(msg)) {
                wait = false;
                msgs.remove(0);
            }
        }
    }
    
    /**
     * @param transferHandler
     */
    public void bind(TransferHandler transferHandler) {
        this.transferHandler = transferHandler;
    }
    
    interface QueueCreateCallback {
        public boolean onCreate(String targetCode);
    }

    public String getTargetCode() {
        return targetCode;
    }
}
