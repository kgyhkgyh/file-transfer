/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-4   comment
 * chenpengliang  2015-3-4  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.IdleSignal;
import com.apusic.adxp.agent.netty.adxp.storage.FileTaskManager;
import com.apusic.adxp.agent.netty.adxp.storage.FileTaskManagerImpl;

/**
 * @author chenpengliang
 * 
 */
public class FileTransferListener implements TransferListener {

    /**
     * 褰撳墠鍓嶇疆鏈虹紪鐮�
     */
    private String code;

    /**
     * 宸ヤ綔鐩綍
     */
    private String baseDir;
    
    /**
     * 鏂囦欢鍒嗙墖宸ヤ綔绫�
     */
    private FileRequestHelper fileRequestHelper;
    
    
    private static final Logger log = LoggerFactory.getLogger(FileTransferListener.class);
    
    
    /**
     * 鍒嗙墖浠诲姟澶勭悊绫�
     */
    private FileTaskManager fileTaskManager = new FileTaskManagerImpl();

    public FileTransferListener(String code, String baseDir) {
        super();
        try {
            this.code = code;
            this.baseDir = baseDir;
            fileRequestHelper = new FileRequestHelper();
            fileRequestHelper.init();
            fileTaskManager.init(baseDir);
        } catch (Exception e) {
            if(log.isInfoEnabled()) {
                log.info("初始化失败"+e.getMessage());
            }
        }
    }

    public void active(Indicate indicate) throws Exception {
        System.out.println(code + ":客户端"+ indicate.getCode() + "连接");
    }

    public void inActive(Indicate indicate) throws Exception {
    	System.out.println(code + ":客户端"+ indicate.getCode() + "断开");
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.TransferListener#receiveMsg(com.apusic.adxp.Indicate, java.lang.Object)
     */
    public void receiveMsg(Indicate indicate, Object msg) throws Exception {
        if (msg != null) {
            if (msg instanceof FileTransferResponse) {
                processResponse(indicate, (FileTransferResponse) msg);
            } else if (msg instanceof FileTransferRequest) {
                processRequest(indicate, (FileTransferRequest) msg);
            } else if (msg instanceof IdleSignal) {
                processIdle(indicate);
            }else if (msg instanceof FileTransferTaskResponse) {
                processTaskResponse(indicate, (FileTransferTaskResponse) msg);
            }else if (msg instanceof FileTransferTaskRequest) {
                processTaskRequest(indicate, (FileTransferTaskRequest) msg);
            }
        }

    }

    /**
     * @param indicate
     * @param msg
     */
    private void processIdle(Indicate indicate) {
        try {
            FileReceiveTask fileTask = fileTaskManager.getFileTask();
            if(fileTask != null) {
                //鍙戦�鏂囦欢璇锋眰
                FileTransferRequest req = new FileTransferRequest();
                req.setFilePath(fileTask.getSourceDir() + "/" +fileTask.getFileName());
                req.setFileSize(fileTask.getFileSize());
                req.setTaskId(fileTask.getTaskId());
                indicate.sendMsg(req);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 澶勭悊鏂囦欢鍝嶅簲
     * @param indicate
     * @param resp
     * @throws Exception
     */
    private void processResponse(Indicate indicate, FileTransferResponse resp) throws Exception {
        FileReceiveTask fileTask = fileTaskManager.handleFileTask(resp.getPosition(), resp.getSize(), resp.getContent(), resp.getTaskId());
        if(fileTask != null) {
            if(log.isInfoEnabled()) {
                log.info("鎺ユ敹鏂囦欢鎴愬姛");
            }
            String targetCode = fileTask.getTargetCode();
            if (targetCode.equals(code)) {
                if(log.isInfoEnabled()) {
                    log.info("鏂囦欢鍒拌揪鐩爣鍦扮偣:"+code+"寮�澶勭悊");
                }
                if(log.isInfoEnabled()) {
                    log.info("浠诲姟缁撴潫鏃堕棿"+new Date());
                }
            }else {
                if(log.isInfoEnabled()) {
                    log.info("鏂囦欢闈炵洰鐨勫湴锛屾鍦ㄨ浆鍙�杞彂鐩爣:"+targetCode);
                }
            }
        }
    }

    /**
     * 澶勭悊鏂囦欢璇锋眰
     * @param indicate
     * @param req
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    private void processRequest(Indicate indicate, FileTransferRequest req) throws FileNotFoundException, IOException, Exception {
        SendTask sendTask = new SendTask("D:\\adxp-workspace\\embed\\transfer/"+req.getFilePath(), req.getFileSize(), req.getPosition(), req.getTaskId(), indicate);
        fileRequestHelper.setSendTask(sendTask);
    }

    /**
     * 澶勭悊鏂囦欢浠诲姟鍝嶅簲
     * @param indicate
     * @param resp
     * @throws IOException
     */
    private void processTaskResponse(Indicate indicate, FileTransferTaskResponse resp) throws Exception {
        System.out.println("任务接受 开始时间"+new Date());
    }

    /**
     * 澶勭悊鏂囦欢浠诲姟璇锋眰
     * @param indicate
     * @param msg
     * @throws IOException
     * @throws Exception
     */
    private void processTaskRequest(Indicate indicate, FileTransferTaskRequest msg) throws IOException, Exception {
        String sourceFileName = msg.getFilePath();
        FileReceiveTask currentTask = fileTaskManager.addFileStorageTask("transfer/", msg);

        //鍙戦�鏂囦欢浠诲姟鍝嶅簲
        String filePath = sourceFileName;
        FileTransferTaskResponse taskResp = new FileTransferTaskResponse();
        taskResp.setFilePath(baseDir + filePath);
        taskResp.setInstanceId(msg.getInstanceId());
        taskResp.setTarget(code);
        indicate.sendMsg(taskResp);
        
        //鍙戦�鏂囦欢璇锋眰
        FileTransferRequest req = new FileTransferRequest();
        req.setFilePath(currentTask.getSourceDir() + "/" +sourceFileName);
        req.setFileSize(currentTask.getFileSize());
        req.setTaskId(currentTask.getTaskId());
        indicate.sendMsg(req);
    }

}
