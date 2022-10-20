package org.luban.common.codec;


/**
 * User : krisibm@163.com
 * Date: 2015/5/28
 */
public final class ByteUtil {

    private ByteUtil() {
    }

    /**
     * 从源字节数组中截取一部分
     *
     * @param source 源字节数组
     * @param start  起始位置，以0开始
     * @param end    结束位置，不包含
     * @return 截取得到的子数组
     */
    public static byte[] slice(byte[] source, int start, int end) {
        if (start < 0 || end > source.length) {
            throw new IllegalArgumentException("start or end is out of bound");
        }
        byte[] target = new byte[end - start];
        System.arraycopy(source, start, target, 0, target.length);
        return target;
    }

    /**
     * 判断两个字节数组是否相等
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean equals(byte[] source, byte[] target) {
        if (source == target) {
            return true;
        }
        if (source == null || target == null) {
            return false;
        }
        if (source.length != target.length) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            if (source[i] != target[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字节数组转换成十六进制字符串形式
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String byte2hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            String hex = (Integer.toHexString(b & 0XFF));
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(hex);
        }
        return builder.toString();
    }


}
