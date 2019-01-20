package org.luban.common;

import org.bscl.common.io.FileUtil;
import org.junit.Test;
import org.luban.common.io.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * User: shijingui
 * Date: 2016/9/6
 */
public class FileUtilTest {

    @Test
    public void test1() {
        File file = new File("d:/a.txt");
        try {
            String str = FileUtil.file2String(file);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        File file = new File("d:/b.txt");
        String str = "hello docker!!欢迎来您的到来！！";
        FileUtil.string2File(file, str, true);
    }
}
