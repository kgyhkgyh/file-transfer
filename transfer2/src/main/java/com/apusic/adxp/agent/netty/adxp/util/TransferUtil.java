/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-4-7   comment
 * chenpengliang  2015-4-7  Created
 */
package com.apusic.adxp.agent.netty.adxp.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.apusic.adxp.agent.netty.adxp.storage.Segment;

/**
 * @author chenpengliang
 *
 */
public class TransferUtil {

    public static List<Segment> createCheckList(String targetPath, int size) throws Exception {
        RandomAccessFile raf = null;
        List<Segment> checkList = new ArrayList<Segment>();
        try {
            File df = new File(targetPath);
            long fileSize = df.length();
            raf = new RandomAccessFile(df, "r");
            long position = 0;
            byte[] b = new byte[size];
            while(position < fileSize) {
                if(position + size > fileSize) {
                    size = (int) (fileSize - position);
                }
                Segment segment = new Segment();
                segment.setPosition(position);
                segment.setSize(size);
                raf.seek(position);
                raf.read(b);
                ByteArrayInputStream bis = new ByteArrayInputStream(b);
                String cMd5 = SecurityUtil.calculateMd5(bis);
                segment.setMd5(cMd5);
                checkList.add(segment);
                position += size;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            raf.close();
        }
        return checkList;
    }
}
