package org.bscl.common;

import junit.framework.TestCase;
import org.bscl.common.encrypt.DesEncrypterUtil;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class DesTest extends TestCase {

    @Test
    public void test() {

        String cipherStr = "Lp6dKGZd9YH0Fd6pfLyzVe/+GE7slLu7Tw5eJYucxWg=";

        DesEncrypterUtil encrypter = new DesEncrypterUtil("gs7n$en76$3@ad1s");

        String ciphertext = encrypter.encrypt("shijingui|1986|chaoyangmen|nan");

        String plainText = encrypter.decrypt(cipherStr);

        System.out.println(ciphertext.length());
        System.out.println(plainText);
    }
}
