/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-4   comment
 * chenpengliang  2015-3-4  Created
 */
package com.apusic.adxp.agent.netty.adxp.storage;

/**
 * @author chenpengliang
 *
 */
public class FileStorageFactory {

    private static String BASE_DIR = null;
    
    /**
     * @param baseDir
     */
    public static void initBaseDir(String baseDir) {
        if (FileStorageFactory.BASE_DIR == null) {
            FileStorageFactory.BASE_DIR = baseDir;
        }
    }
    
    /**
     * @param filePath
     * @param md5
     * @param fileSize
     * @return
     */
    public static FileStorage createFileStorage(String filePath, String targetDir, String tempFileName, String md5, long fileSize) {
        FileStorageImpl fileStorage = new FileStorageImpl(filePath, targetDir, tempFileName, md5, fileSize);
        fileStorage.setBaseDir(BASE_DIR);
        return fileStorage;
    }
    
}
