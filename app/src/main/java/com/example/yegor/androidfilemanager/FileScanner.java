package com.example.yegor.androidfilemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    public boolean copyFileOrDirectory(String srcDir, String dstDir) {
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir);

            if (src.isDirectory()) {
                if (!dst.exists()) {
                    boolean mkdirs = dst.mkdirs();
                    if (!mkdirs) {
                        return false;
                    }
                }
                String files[] = src.list();
                for (String file : files) {
                    String src1 = new File(src, file).getPath();
                    String dst1 = new File(dst, file).getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                if (!copyFile(src, dst)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            if (!destFile.createNewFile()) {
                return false;
            }
        }

        try (FileChannel source = new FileInputStream(sourceFile).getChannel();
             FileChannel destination = new FileOutputStream(destFile).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        }
        return true;
    }
}