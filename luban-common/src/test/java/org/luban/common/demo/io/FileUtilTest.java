package org.luban.common.demo.io;

import org.junit.Test;
import org.luban.common.io.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * User: krisjin
 * Date: 2016/9/6
 */
public class FileUtilTest {

    @Test
    public void test1() {
        File file = new File("/usr/local/tools/testFile.txt");
        try {
            String str = FileUtil.file2String(file);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        File file = new File("/usr/local/tools/testFile.txt");
        String str = "Hello world! ";
        for (int i = 0; i < 10; i++) {

            String newStr = str + i;
            FileUtil.string2File(file, newStr + "\n", false);
        }
    }
}
