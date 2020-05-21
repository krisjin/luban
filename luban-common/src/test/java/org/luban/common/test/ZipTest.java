package org.luban.common.test;

import org.luban.common.io.FileZipUtil;

/**
 * User:krisjin
 * Date:2020-05-18
 */
public class ZipTest {


    public static void main(String[] args) {
        String inputFile = "/usr/local/gitrep/Alink";
        String outputFile = "/usr/local/tools/ziptest/ab.zip";


        try {
            FileZipUtil.zipCompress(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ZipFileUtil.unZip(f, "/usr/local/tools/ziptest/");
    }
}
