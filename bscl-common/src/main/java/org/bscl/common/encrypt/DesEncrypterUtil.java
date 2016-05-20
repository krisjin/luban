package org.bscl.common.encrypt;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * 基于DES的加密解密器
 */
public class DesEncrypterUtil implements Encryptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesEncrypterUtil.class);

    private static final byte[] DEFAULT_SALT = {(byte) 0xB9, (byte) 0xAB, (byte) 0xD8, (byte) 0x42, (byte) 0x66, (byte) 0x45, (byte) 0xF3, (byte) 0x13};
    /**
     * 56 bit key,DES 加密算法
     */
    public static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES";
    /**
     * 168 bit key,Triple DES 加密算法
     */
    public static final String PBE_WITH_MD5_AND_TRIPLE_DES = "PBEWithMD5AndTripleDES";
    /**
     * 加密算法
     */
    private final String alogrithName;
    /**
     * 迭代次數
     */
    private final int iterationCount = 17;
    private final byte[] salt;
    private final SecretKey secretKey;

    /**
     * 使用默认的slat {@link #DEFAULT_SALT} 和{@link #PBE_WITH_MD5_AND_DES}构建
     *
     * @param passPhrase 密码
     * @see {@link #DesEncrypterUtil(String, String)}
     */
    public DesEncrypterUtil(String passPhrase) {
        this(passPhrase, PBE_WITH_MD5_AND_DES);
    }

    /**
     * 使用指定的slat 和 {@link #PBE_WITH_MD5_AND_DES} 构建
     *
     * @param passPhrase
     * @param newSalt
     */
    public DesEncrypterUtil(String passPhrase, byte[] newSalt) {
        this(passPhrase, PBE_WITH_MD5_AND_DES, newSalt);
    }

    /**
     * @param passPhrase   密码
     * @param alogrithName 加密算法
     */
    public DesEncrypterUtil(String passPhrase, String alogrithName) {
        this(passPhrase, alogrithName, DEFAULT_SALT);
    }


    /**
     * @param passPhrase   密码
     * @param algorithName 加密算法,可选的有{@link #PBE_WITH_MD5_AND_TRIPLE_DES},{@link #PBE_WITH_MD5_AND_DES}
     * @param newSalt      干扰盐
     */
    public DesEncrypterUtil(String passPhrase, String algorithName, byte[] newSalt) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(passPhrase));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(algorithName));
        Preconditions.checkArgument(newSalt != null);
        this.salt = Arrays.copyOf(newSalt, newSalt.length);
        this.alogrithName = algorithName;
        this.secretKey = genKey(passPhrase.toCharArray(), this.salt, iterationCount);
    }


    @Override
    public String encrypt(String str) {
        try {
            SecretKeySpec key = cloneSecretKeySpec();
            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(this.salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, pbeParameterSpec);
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            return Base64.encodeBase64String(enc);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encrypt error: str[" + str + "]  ", e);
        } catch (BadPaddingException e) {
            LOGGER.error("encrypt error: str[" + str + "]  ", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error("encrypt error: str[" + str + "]  ", e);
        } catch (Exception e) {
            LOGGER.error("encrypt error: str[" + str + "]  ", e);
        }
        return null;
    }


    @Override
    public String decrypt(String str) {
        try {
            SecretKeySpec key = cloneSecretKeySpec();
            Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(this.salt, iterationCount);
            dcipher.init(Cipher.DECRYPT_MODE, key, pbeParameterSpec);
            byte[] dec = Base64.decodeBase64(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (IOException e) {
            LOGGER.error("decrypt error: str[" + str + "]  ", e);
        } catch (BadPaddingException e) {
            LOGGER.error("decrypt error: str[" + str + "]  ", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error("decrypt error: str[" + str + "]  ", e);
        } catch (Exception e) {
            LOGGER.error("decrypt error: str[" + str + "]  ", e);
        }

        return null;
    }

    /**
     * 客户端上传DES加密字段解密算法
     * <p/>
     * 如果解密失败，log级别为error
     *
     * @param dataStr
     * @param decryptKey
     * @return
     */
    public static byte[] decryptDES(String dataStr, byte[] decryptKey) {
        byte[] data = Base64.decodeBase64(dataStr);
        return decryptDES(data, decryptKey, false);
    }

    /**
     * “沉默”地处理，客户端上传DES加密字段解密算法
     * <p/>
     * 如果解密失败，log级别为info
     *
     * @param dataStr
     * @param decryptKey
     * @return
     */
    public static byte[] decryptDESQuietly(String dataStr, byte[] decryptKey) {
        byte[] data = Base64.decodeBase64(dataStr);
        return decryptDES(data, decryptKey, true);
    }

    /**
     * 客户端上传DES加密字段解密算法，如果解密失败，输出error级别的日志
     *
     * @param data
     * @param decryptKey
     * @return
     */
    public static byte[] decryptDES(byte[] data, byte[] decryptKey) {
        return decryptDES(data, decryptKey, false);
    }

    /**
     * 客户端上传DES加密字段解密算法
     *
     * @param data
     * @param decryptKey
     * @param isQuiet    是否沉默，对应报警级别
     * @return
     */
    public static byte[] decryptDES(byte[] data, byte[] decryptKey, boolean isQuiet) {
        try {
            DESKeySpec dks = new DESKeySpec(decryptKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            IvParameterSpec zeroIv = new IvParameterSpec(DEFAULT_SALT);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            if (isQuiet) {
                LOGGER.info("decrypt failed: key[" + new String(decryptKey) + "]");
            } else {
                LOGGER.error("decrypt error: key[" + new String(decryptKey) + "]", e);
            }
        }
        return null;
    }

    /**
     * 客户端上传DES加密字段解密算法
     *
     * @param data
     * @param encryptKey
     * @return
     */
    public static byte[] encryptDES(byte[] data, byte[] encryptKey) {
        try {
            DESKeySpec dks = new DESKeySpec(encryptKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            IvParameterSpec zeroIv = new IvParameterSpec(DEFAULT_SALT);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            LOGGER.error("decrypt error: key[" + new String(encryptKey) + "]", e);
        }
        return null;
    }

    /**
     * 由于不清楚this.secretKey是否线程安全,这里clone一个出来
     *
     * @return
     */
    private SecretKeySpec cloneSecretKeySpec() {
        return new SecretKeySpec(this.secretKey.getEncoded(), this.secretKey.getAlgorithm());
    }

    private SecretKey genKey(char[] passPhrase, byte[] salt, int iterationCount) {
        KeySpec keySpec = new PBEKeySpec(passPhrase, salt, iterationCount);
        try {
            return SecretKeyFactory.getInstance(alogrithName).generateSecret(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
