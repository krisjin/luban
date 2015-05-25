package net.common.utils;

import junit.framework.TestCase;
import net.common.utils.encrypt.DesEncrypterUtil;

/**
 * Unit test for simple App.
 */
public class DesTest extends TestCase {
    public static void main(String[] args) {

        String cipherStr = "Lp6dKGZd9YH0Fd6pfLyzVe/+GE7slLu7Tw5eJYucxWg=";

        DesEncrypterUtil encrypter = new DesEncrypterUtil("gs7n$en76$3@ad1s");

        String ciphertext = encrypter.encrypt("shijingui|1986|chaoyangmen|nan");

        String plainText = encrypter.decrypt(cipherStr);

        System.out.println(ciphertext.length());
        System.out.println(plainText);
    }
}
