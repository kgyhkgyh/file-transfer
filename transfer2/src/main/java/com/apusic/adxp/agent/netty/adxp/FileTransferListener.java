/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-4   comment
 * chenpengliang  2015-3-4  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apusic.adxp.agent.netty.adxp.modal.FileReceiveTask;
import com.apusic.adxp.agent.netty.adxp.modal.Indicate;
import com.apusic.adxp.agent.netty.adxp.modal.SendTask;
import com.apusic.adxp.agent.netty.adxp.protocal.FileCheckListRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileCheckListResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskRequest;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskResponse;
import com.apusic.adxp.agent.netty.adxp.protocal.IdleSignal;
import com.apusic.adxp.agent.netty.adxp.storage.FileTaskManager;
import com.apusic.adxp.agent.netty.adxp.storage.FileTaskManagerImpl;
import com.apusic.adxp.agent.netty.adxp.storage.Segment;
import com.apusic.adxp.agent.netty.adxp.util.FileRequestHelper;
import com.apusic.adxp.agent.netty.adxp.util.TransferUtil;

/**
 * @author chenpengliang
 * 
 */
public class FileTransferListener implements TransferListener {

    /**
     * 当前前置机编码
     */
    private String code;

    /**
     * 工作目录
     */
    private String baseDir;
    
    /**
     * 文件分片工作类
     */
    private FileRequestHelper fileRequestHelper;
    
    private static final String TEMP_DIR = "D:/target/transfer/";
    
    
    private static final Logger log = LoggerFactory.getLogger(FileTransferListener.class);
    
    
    /**
     * 分片任务处理类
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
                log.info("初始化监听失败，原因："+e.getMessage());
            }
        }
    }

    public void active(Indicate indicate) throws Exception {
        System.out.println(code + ":有客户" + indicate.getCode() + "接入");
    }

    public void inActive(Indicate indicate) throws Exception {
        System.out.println(code + ":有客户" + indicate.getCode() + "断开");
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
            }else if(msg instanceof FileCheckListRequest) {
                processCheckListRequest(indicate, (FileCheckListRequest)msg);
            }else if(msg instanceof FileCheckListResponse) {
                processCheckListResponse(indicate,(FileCheckListResponse)msg);
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
                //发送文件请求
                FileTransferRequest req = new FileTransferRequest();
                req.setFilePath(fileTask.getSourceDir() + "/" +fileTask.getRelativeFilePath());
                req.setFileSize(fileTask.getFileSize());
                req.setTaskId(fileTask.getTaskId());
                indicate.sendMsg(req);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理文件响应
     * @param indicate
     * @param resp
     * @throws Exception
     */
    private void processResponse(Indicate indicate, FileTransferResponse resp) throws Exception {
        
        final Indicate indicate2 = indicate;
        fileTaskManager.handleFileTask(resp.getPosition(), resp.getSize(), resp.getContent(), resp.getTaskId(), new FileTaskCallback() {
            
            public void onFinished(FileReceiveTask fileTask) {
                if(log.isInfoEnabled()) {
                    log.info("接收文件成功");
                }
                String targetCode = fileTask.getTargetCode();
                if (targetCode.equals(code)) {
                    if(log.isInfoEnabled()) {
                        log.info("文件到达目标地点:"+code+"开始处理");
                    }
                    if(log.isInfoEnabled()) {
                        log.info("任务结束时间"+new Date());
                    }
                }else {
                    if(log.isInfoEnabled()) {
                        log.info("文件非目的地，正在转发 转发目标:"+targetCode);
                    }
                }
            }

            public void onReSend(FileCheckListRequest req) throws Exception {
                indicate2.sendMsg(req);
            }
            
        });
    }

    /**
     * 处理文件请求
     * @param indicate
     * @param req
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    private void processRequest(Indicate indicate, FileTransferRequest req) throws FileNotFoundException, IOException, Exception {
        SendTask sendTask = new SendTask(req.getFilePath(), req.getFileSize(), req.getPosition(), req.getTaskId(), indicate);
        fileRequestHelper.setSendTask(sendTask);
    }

    /**
     * 处理文件任务响应
     * @param indicate
     * @param resp
     * @throws IOException
     */
    private void processTaskResponse(Indicate indicate, FileTransferTaskResponse resp) throws Exception {
        System.out.println("文件任务请求已经被接收");
        System.out.println("任务开始时间"+new Date());
    }

    /**
     * 处理文件任务请求
     * @param indicate
     * @param msg
     * @throws IOException
     * @throws Exception
     */
    private void processTaskRequest(Indicate indicate, FileTransferTaskRequest msg) throws IOException, Exception {
        String sourceFileName = msg.getFilePath();
        FileReceiveTask currentTask = fileTaskManager.addFileStorageTask(TEMP_DIR, msg, indicate);

        //发送文件任务响应
        String filePath = sourceFileName;
        FileTransferTaskResponse taskResp = new FileTransferTaskResponse();
        taskResp.setFilePath(baseDir + filePath);
        taskResp.setInstanceId(msg.getInstanceId());
        taskResp.setTarget(code);
        indicate.sendMsg(taskResp);
    }
    

    /**
     * @param indicate
     * @param msg
     */
    private void processCheckListRequest(Indicate indicate, FileCheckListRequest msg) throws Exception{
        RandomAccessFile raf = null;
        try {
            String sourceFilePath = FilenameUtils.concat(baseDir, msg.getFilePath());
            List<Segment> sourceCheckList = TransferUtil.createCheckList(sourceFilePath, msg.getFixSize());
            List<Segment> targetCheckList = msg.getCheckList();
            sourceCheckList.removeAll(targetCheckList);
            FileCheckListResponse ckResp = new FileCheckListResponse();
            List<Long> ckList = new ArrayList<Long>();
            System.out.println("收到校验列表，正在计算");
            for (Segment segment : sourceCheckList) {
                ckList.add(segment.getPosition());
                System.out.print("片段 "+segment.getPosition());
            }
            System.out.println("缺失");
            ckResp.setCheckList(ckList);
            ckResp.setTaskId(msg.getTaskId());
            indicate.sendMsg(ckResp);
            
            //发送文件片段
            File file = new File(sourceFilePath);
            raf = new RandomAccessFile(file,"r");
            byte[] b = new byte[msg.getSize()];
            for (Segment segment : sourceCheckList) {
                FileTransferResponse resp = new FileTransferResponse();
                raf.seek(segment.getPosition());
                raf.read(b);
                resp.setFileSize(file.length());
                resp.setSize(segment.getSize());
                resp.setContent(b);
                resp.setPosition(segment.getPosition());
                resp.setTaskId(msg.getTaskId());
                indicate.sendMsg(resp);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            raf.close();
        }
    }
    
    /**
     * @param indicate
     * @param msg
     */
    private void processCheckListResponse(Indicate indicate, FileCheckListResponse msg) {
        System.out.println("接收到重置列表，正在重置");
        fileTaskManager.resetFileTask(msg, msg.getTaskId());
    }

}
