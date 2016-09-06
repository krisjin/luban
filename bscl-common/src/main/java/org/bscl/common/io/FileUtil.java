package org.bscl.common.io;

import java.io.*;

/**
 * 文件操作工具类
 * User: shijingui
 * Date: 2016/9/6
 */
public class FileUtil {
    private FileUtil() {
    }

    public static String file2String(File file) throws IOException {
        if (file == null || !file.isFile() || !file.canRead() || !file.exists()) {
            return null;
        }
        FileReader reader = null;
        StringWriter writer = null;
        try {
            reader = new FileReader(file);
            writer = new StringWriter();
            char[] charBuf = new char[1024];
            int len = 0;
            while ((len = reader.read(charBuf)) != -1) {
                writer.write(charBuf, 0, len);
            }
            return writer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 字符流写文件 较快
     *
     * @param file
     * @param data
     * @param append 如果为 true，则将字节写入文件末尾处，而不是写入文件开始处
     * @return
     */
    public static boolean string2File(File file, String data, boolean append) {
        FileWriter write = null;
        boolean result = true;
        try {
            write = new FileWriter(file, append);
            write.write(data);
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (write != null) {
                try {
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
