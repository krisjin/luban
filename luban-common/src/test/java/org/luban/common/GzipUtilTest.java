package org.luban.common;

import org.luban.common.io.GzipUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 11:21
 */
public class GzipUtilTest {

    @Test
    public void test() {
        String name = "test";
        try {
            byte[] nameCompress = GzipUtil.compress(name.getBytes());
            byte[] nameDecompress = GzipUtil.decompress(nameCompress);
            System.out.println("origin:" + name.getBytes().length);
            System.out.println("compress:" + nameCompress.length);
            System.out.print("decompress:" + nameDecompress.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
