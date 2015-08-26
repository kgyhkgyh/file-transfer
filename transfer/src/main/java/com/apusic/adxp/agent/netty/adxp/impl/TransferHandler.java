/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-6   comment
 * linxueqin  2015-2-6  Created
 */
package com.apusic.adxp.agent.netty.adxp.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.apusic.adxp.agent.netty.adxp.Indicate;
import com.apusic.adxp.agent.netty.adxp.protocal.Introduction;

/**
 * @author linxueqin
 * 
 */
public class TransferHandler extends ChannelInboundHandlerAdapter implements Indicate {

    /**
     * 
     */
    private TransferHandlerListener listener;

    /**
     * 
     */
    private String code;

    /**
     * 目标编码
     */
    private String targetCode;

    /**
     * 通道
     */
    private Channel channel;

    /**
     * 
     * @param code
     * @param targetCode
     * @param server
     * @param listener
     */
    public TransferHandler(String code, TransferHandlerListener listener) {
        super();
        this.code = code;
        this.listener = listener;
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.Indicate#getCode()
     */
    public String getCode() {
        return targetCode;
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        Introduction introduction = new Introduction();
        introduction.setCode(code);
        ctx.writeAndFlush(introduction);
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (targetCode == null && msg instanceof Introduction) {
            targetCode = ((Introduction) msg).getCode();
            listener.active(this);
        } else {
            listener.receiveMsg(this, msg);
        }
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (targetCode != null) {
            listener.inActive(this);
        }
    }

    /**
     * @param msg
     * @return
     */
    public boolean writeMsg(Object msg) {
        if (channel.isWritable()) {
            channel.writeAndFlush(msg);
            return true;
        } else {
            return false;
        }
    }
    
    /* (non-Javadoc)
     * @see com.apusic.adxp.Indicate#sendMsg(java.lang.Object)
     */
    public void sendMsg(Object msg) throws Exception {
        listener.send(targetCode, msg);
    }


    /* (non-Javadoc)
     * @see com.apusic.adxp.Indicate#sendMsg(java.lang.String, java.lang.Object)
     */
    public void sendMsg(String targetCode, Object msg) throws Exception {
        listener.send(targetCode, msg);
    }

    /**
     * @throws Exception
     */
    public void close() throws Exception {
        channel.close();
    }

}
