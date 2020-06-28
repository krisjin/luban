package org.luban.common.io;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * User:krisjin
 * Date:2020-05-19
 */
public class FileZipUtil {


    /**
     * zip file compress
     *
     * @param inputFile
     * @param outputFile
     */

    public static void zipCompress(String inputFile, String outputFile) throws Exception {
        //创建zip输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(zipOutputStream);
        File input = new File(inputFile);
        compress(zipOutputStream, bos, input, null);
        bos.close();
        zipOutputStream.close();
    }

    /**
     * zip compress ,use recursion
     *
     * @param zipOutputStream
     * @param bos
     * @param input
     * @param fileName
     * @throws IOException
     */
    public static void compress(ZipOutputStream zipOutputStream, BufferedOutputStream bos, File input, String fileName) throws IOException {
        if (fileName == null) {
            fileName = input.getName();
        }

        if (input.isDirectory()) {
            File[] listFiles = input.listFiles();

            if (listFiles.length == 0) {
                zipOutputStream.putNextEntry(new ZipEntry(fileName + "/"));
            } else {
                for (int i = 0; i < listFiles.length; i++) {
                    compress(zipOutputStream, bos, listFiles[i], fileName + "/" + listFiles[i].getName());
                }
            }
        } else { //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len = -1;
            //将源文件写入到zip文件中
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bis.close();
            fos.close();
        }
    }

    /**
     * zip uncompress
     *
     * @param inputFile
     * @param destDirPath
     */

    public static void ZipUncompress(String inputFile, String destDirPath) throws Exception {
        File srcFile = new File(inputFile);//获取当前压缩文件
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new Exception(srcFile.getPath() + "the file is not exist...");
        }
        //开始解压
        //构建解压输入流
        ZipInputStream zIn = new ZipInputStream(new FileInputStream(srcFile));
        ZipEntry entry = null;
        File file = null;
        while ((entry = zIn.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                file = new File(destDirPath, entry.getName());
                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();//创建此文件的上级目录
                }
                OutputStream out = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                int len = -1;
                byte[] buf = new byte[1024];
                while ((len = zIn.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                bos.close();
                out.close();
            }
        }
    }

}
