package org.bscl.common.encrypt;

/**
 * 加密机接口
 */
public interface Encryptor {
    /**
     * 对字符串进行加密处理
     *
     * @param str
     * @return
     */
    String encrypt(String str);

    /**
     * 对字符串进行解密处理
     *
     * @param str
     * @return
     */
    String decrypt(String str);
}
