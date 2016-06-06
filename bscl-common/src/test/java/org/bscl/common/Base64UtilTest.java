package org.bscl.common;

import org.bscl.common.encrypt.Base64Util;
import org.junit.Test;

/**
 * User: shijingui
 * Date: 2016/6/6
 */
public class Base64UtilTest {

    @Test
    public void decode() {
        String str = Base64Util.decode("5L2g5aW9");
        System.out.println(str);
    }

    @Test
    public void encode() {
        System.out.println(Base64Util.encode("你好"));
    }
}
