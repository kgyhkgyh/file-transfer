/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-4-3   comment
 * chenpengliang  2015-4-3  Created
 */
package com.apusic.adxp.agent.netty.adxp;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.apusic.adxp.agent.netty.adxp.impl.TransferServerImpl;
import com.apusic.adxp.agent.netty.adxp.protocal.FileTransferTaskRequest;
import com.apusic.adxp.agent.netty.adxp.util.SecurityUtil;

/**
 * @author chenpengliang
 *
 */
public class TestAgent1 {

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
        transferServer.start("agent1", 8080, new FileTransferListener("agent1", "D:\\adxp-workspace\\embed\\"));
        
//        FileTransferTaskRequest request = new FileTransferTaskRequest();
//        String fileName = "eclipse-jee-indigo-SR2-win32-x86_64.zip";
//        request.setFilePath(fileName);
//        request.setSourceDir("export_data");
//        File sourceFile = new File("D:\\adxp-workspace\\embed\\export_data\\"+fileName);
//        request.setFileSize(sourceFile.length());
//        request.setTarget("agent2");
//        request.setSize(100 * 1024);
//        request.setMd5(SecurityUtil.calculateMd5(sourceFile));
//        transferServer.send("agent2", request);
        
        String dirPath = "D:\\source";
        File fileDir = new File(dirPath);
        List<File> fileList = new ArrayList<File>();
        getAll(fileDir, fileList, null);
        for (File file : fileList) {
            FileTransferTaskRequest request2 = new FileTransferTaskRequest();
            request2.setFilePath(file.getAbsolutePath().substring(dirPath.length()+1));
            request2.setSourceDir(dirPath);
            request2.setFileSize(file.length());
            request2.setTarget("agent2");
            request2.setSize(100 * 1024);
            request2.setWeight(2.1f);
            request2.setMd5(SecurityUtil.calculateMd5(file));
            transferServer.send("agent2", request2);
        }
        
        
//        FileTransferTaskRequest request2 = new FileTransferTaskRequest();
//        String fileName2 = "eclipse-jee-indigo-SR2-win32-x86_64.zip";
//        request2.setFilePath(fileName2);
//        request2.setSourceDir("export_data");
//        File sourceFile2 = new File("D:\\adxp-workspace\\embed\\export_data\\"+fileName2);
//        request2.setFileSize(sourceFile2.length());
//        request2.setTarget("agent2");
//        request2.setSize(100 * 1024);
//        request2.setWeight(2.1f);
//        request2.setMd5(SecurityUtil.calculateMd5(sourceFile2));
//        
//        FileTransferTaskRequest request3 = new FileTransferTaskRequest();
//        String fileName3 = "logBackMonitorTest3.log";
//        request3.setFilePath(fileName3);
//        request3.setSourceDir("export_data");
//        File sourceFile3 = new File("D:\\adxp-workspace\\embed\\export_data\\"+fileName3);
//        request3.setFileSize(sourceFile3.length());
//        request3.setTarget("agent2");
//        request3.setSize(100 * 1024);
//        request3.setWeight(2.0f);
//        request3.setMd5(SecurityUtil.calculateMd5(sourceFile3));
//        
//        FileTransferTaskRequest request4 = new FileTransferTaskRequest();
//        String fileName4 = "logBackMonitorTest4.log";
//        request4.setFilePath(fileName4);
//        request4.setSourceDir("export_data");
//        File sourceFile4 = new File("D:\\adxp-workspace\\embed\\export_data\\"+fileName4);
//        request4.setFileSize(sourceFile4.length());
//        request4.setTarget("agent2");
//        request4.setSize(100 * 1024);
//        request4.setWeight(2.5f);
//        request4.setMd5(SecurityUtil.calculateMd5(sourceFile4));
//        
//        FileTransferTaskRequest request5 = new FileTransferTaskRequest();
//        String fileName5 = "logBackMonitorTest5.log";
//        request5.setFilePath(fileName5);
//        request5.setSourceDir("export_data");
//        File sourceFile5 = new File("D:\\adxp-workspace\\embed\\export_data\\"+fileName5);
//        request5.setFileSize(sourceFile5.length());
//        request5.setTarget("agent2");
//        request5.setSize(100 * 1024);
//        request5.setWeight(4.0f);
//        request5.setMd5(SecurityUtil.calculateMd5(sourceFile5));
//        transferServer.send("agent2", request3);
//        transferServer.send("agent2", request4);
//        transferServer.send("agent2", request5);

    }
    
    private static void getAll(File root, List<File> fileList, FileFilter fileFilter) {
        File[] listFiles = root.listFiles(fileFilter);
        for (File file : listFiles) {
            if(file.isDirectory()) {
                getAll(file, fileList, fileFilter);
            } else {
                fileList.add(file);
            }
        }
    }
    
}
