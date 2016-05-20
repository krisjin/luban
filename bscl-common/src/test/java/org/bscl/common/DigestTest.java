package org.bscl.common;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/27
 * Time: 15:35
 */
public class DigestTest {

    public static void main(String[] args) {
        String cipherText = DigestUtils.md5Hex("111111");

        System.out.println(cipherText+":"+cipherText.length());
    }
}
