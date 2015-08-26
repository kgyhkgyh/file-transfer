/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-3   comment
 * chenpengliang  2015-3-3  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FilenameUtils;

import com.apusic.adxp.util.security.SecurityUtil;

import sun.security.krb5.internal.crypto.CksumType;

/**
 * @author chenpengliang
 * 
 */
class FileStorageImpl implements FileStorage {
    
    /**
     * 工作目录
     */
    private String baseDir = null;
    
    /**
     * 目标相对路径
     */
    private String targetDir = null;
    
    /**
     * 源目录下的层级结构路径
     */
    private String relativePath = null;
    
    /**
     * 临时检查文件路径
     */
    private String tempCheckFilePath =null;
    
    /**
     * 目标文件路径
     */
    private String targetPath = null;
    
    /**
     * 文件md5
     */
    private String md5 = null;
    
    /**
     * 文件大小
     */
    private long fileSize = 0;
    
    /**
     * 目标文件名称
     */
    private String fileName = null;
    
    /**
     * 检查文件
     */
    private RandomAccessFile checkFile = null;
    
    /**
     * 数据文件
     */
    private RandomAccessFile dataFile = null;
    
    /**
     * 文件锁
     */
    private ReentrantLock lock = new ReentrantLock();
    
    /**
     * 
     */
    public FileStorageImpl(String fileName, String targetDir, String relativePath, String md5, long fileSize) {
        this.fileName = fileName;
        this.targetDir = targetDir;
        this.relativePath = relativePath;
        this.md5 = md5;
        this.fileSize = fileSize;
    }
    
    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorage#write(long, long, byte[])
     */
    public void write(long position, long size, byte[] data) throws Exception {
        lock.lock();
        try {
         // 写记录
            RandomAccessFile checkFileOutput = getCheckFile();
            checkFileOutput.writeLong(position);
            checkFileOutput.writeLong(size);
            // 写文件
            RandomAccessFile dataRandomAccessFile = getDataFile();
            dataRandomAccessFile.seek(position);
            dataRandomAccessFile.write(data);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * @return
     */
    protected RandomAccessFile getCheckFile() throws Exception {
        if (checkFile == null) {
            File ckFile = new File(tempCheckFilePath);
            checkFile = new RandomAccessFile(ckFile,"rw");
            if (ckFile.length() < 1) {
                checkFile.writeUTF(fileName);
                checkFile.writeUTF(md5);
                checkFile.writeLong(fileSize);
            }
            checkFile.seek(ckFile.length());
        }
        return checkFile;
    }
    
    protected RandomAccessFile getDataFile() throws Exception {
        if (dataFile == null) {
            File df = new File(targetPath);
            dataFile = new RandomAccessFile(df, "rw");
        }
        return dataFile;
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorage#success()
     */
    public boolean success() {
        lock.lock();
        try {
            long currentSize = 0;
            List<Segment> segments = resolveSegments();
            for (Segment segment : segments) {
                currentSize += segment.getSize();
            }
            
            if (currentSize != fileSize) {
                return false;
            }
            
            File file = new File(targetPath);
            String tempMd5 = SecurityUtil.calculateMd5(file);
            if(!tempMd5.equals(md5)) {
                return false;
            }
            
            if(dataFile != null) {
                dataFile.close();
            }
            
            File checkFile = new File(tempCheckFilePath);
            checkFile.delete();
            
            return true;
        } catch( Exception e) {
            // TODO LOG
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
        
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorage#computeMissingSegments()
     */
    public List<Segment> computeMissingSegments() {
        try {
            List<Segment> segments = resolveSegments();
            List<Segment> missingSegments = new ArrayList<Segment>();
            Collections.sort(segments);
            long startPosition = 0;
            for (Segment segment : segments) {
                if (segment.getPosition() > startPosition) {
                    Segment missingSegment = new Segment();
                    missingSegment.setPosition(startPosition);
                    missingSegment.setSize(segment.getPosition() - startPosition);
                    missingSegments.add(missingSegment);
                }
                startPosition = segment.getPosition() + segment.getSize();
            }
            return missingSegments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.storage.FileStorage#transferToTarget()
     */
    public void transferToTarget() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File tempFile = null;
        try {
            tempFile = new File(targetPath);
            File file = new File(targetPath);
            fis = new FileInputStream(tempFile);
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int i = 0;
            while((i = fis.read(b)) > 0) {
                fos.write(b, 0 , i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tempFile.delete();
    }

    protected List<Segment> resolveSegments() throws Exception {
        DataInputStream checkFileInput = null;
        List<Segment> segments = new ArrayList<Segment>();
        lock.lock();
        try {
            if (checkFile != null) {
                checkFile.close();
            }
            checkFileInput = new DataInputStream(new FileInputStream(tempCheckFilePath));
            checkFileInput.readUTF();
            checkFileInput.readUTF();
            checkFileInput.readLong();
            
            try {
                while(true) {
                    long position = checkFileInput.readLong();
                    long size = checkFileInput.readLong();
                    segments.add(new Segment(position, size));
                }
            } catch (EOFException e) {
                
            }
        } finally {
            lock.unlock();
            if (checkFileInput != null) {
                checkFileInput.close();
            }
        }
        return segments;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
        tempCheckFilePath = baseDir + "/" +targetDir+"/" +relativePath + "/" + fileName + ".ck";
        String dirPath = baseDir + "/" +targetDir+"/" + relativePath;
        targetPath = baseDir + "/" +targetDir+"/" + relativePath + "/" + fileName;
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
    }
    
}
