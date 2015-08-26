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
    public static FileStorage createFileStorage(String filePath, String md5, long fileSize, int fixSize) {
        FileStorageImpl fileStorage = new FileStorageImpl(filePath, md5, fileSize, fixSize);
        fileStorage.setBaseDir(BASE_DIR);
        return fileStorage;
    }
    
}
