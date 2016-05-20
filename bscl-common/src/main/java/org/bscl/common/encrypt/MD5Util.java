package org.bscl.common.encrypt;

import com.google.common.base.Preconditions;
import org.bscl.common.codec.ByteUtil;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5摘要
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/28
 * Time: 19:17
 */
public final class MD5Util {

    private MD5Util() {
    }

    /**
     * 计算md5摘要
     *
     * @param source 原始数据
     * @return md5摘要
     */
    public static byte[] digest(byte[] source) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("MD5 does not support", e);
        }
        md.update(source);
        return md.digest();
    }

    /**
     * 计算md5摘要
     *
     * @param source 原始数据
     * @return md5摘要
     */
    public static byte[] digest(String source) {
        Preconditions.checkArgument(source != null, "source string is null");
        try {
            return digest(source.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("internal error", e);
        }
    }

    /**
     * 计算md5摘要，使用十六进制字符串方式返回
     *
     * @param source 原始数据
     * @return md5摘要
     */
    public static String digestHex(byte[] source) {
        return ByteUtil.byte2hex(digest(source));
    }

    /**
     * 计算md5摘要，使用十六进制字符串方式返回
     *
     * @param source 原始数据
     * @return md5摘要
     */
    public static String digestHex(String source) {
        return ByteUtil.byte2hex(digest(source));
    }


    /**
     * 通过MD5加密文件
     *
     * @param fileInputStream 文件路径
     * @return 已经通过MD5算法加密的32位字符串
     */
    public final static String fileToMD5(FileInputStream fileInputStream) {
        int bufferSize = 256 * 1024;// 定义缓冲区大小
        DigestInputStream digestInputStream = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0)
                ;
            messageDigest = digestInputStream.getMessageDigest();
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (Exception e) {
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e2) {
            }
        }
        return null;
    }

    /**
     * @param byteArray byte数组
     * @return 转后后的32位的字符串
     */
    private static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];// 每个字节用 16 进制表示的话，使用两个字符

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        // 从第一个字节开始，对MD5的每一个字节转换成16进制字符的转换
        for (byte b : byteArray) {
            // 取字节中高 4 位的数字转换
            //>>> 为逻辑右移（即无符号右移），将符号位一起右移
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            // 取字节中低4位的数字转换
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

}
