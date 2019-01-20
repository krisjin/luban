package org.luban.common.encrypt;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;

/**
 * 基于AES的加密解密器
 */
public class AESEncryptor implements Encryptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESEncryptor.class);
    private static final int KEY_LENGTH = 128;
    private static final int ITERATIONS = 17;
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final byte[] DEFAULT_SALT = {(byte) 0xD9, (byte) 0xAB, (byte) 0xD8, (byte) 0x42, (byte) 0x66, (byte) 0x45, (byte) 0xF3, (byte) 0x13};
    private static final byte[] DEFAULT_IV = {(byte) 0xB9, (byte) 0xA0, (byte) 0xE8, (byte) 0x62, (byte) 0x96, (byte) 0xA5, (byte) 0x13, (byte) 0xF3};

    /**
     * 生成的秘钥
     */
    private final SecretKey secretKey;
    /**
     * 初始化向量
     */
    private final IvParameterSpec iv;

    /**
     * 使用默认的salt{@link #DEFAULT_SALT}构建AES加密实例
     *
     * @param passPhrase 密码串
     */
    public AESEncryptor(String passPhrase) {
        this(passPhrase, DEFAULT_SALT);
    }

    /**
     * @param passPhrase 密码串
     * @param salt       指定的干扰盐
     */
    public AESEncryptor(String passPhrase, byte[] salt) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(passPhrase));
        Preconditions.checkArgument(salt != null);
        this.secretKey = genKey(passPhrase, salt);
        byte[] ivb = new byte[16];
        for (int i = 0; i < ivb.length; i++) {
            ivb[i] = DEFAULT_IV[i % DEFAULT_IV.length];
        }
        iv = new IvParameterSpec(ivb);

    }


    @Override
    public String encrypt(String str) {
        try {
            SecretKeySpec key = cloneSecretKeySpec();
            Cipher ecipher = Cipher.getInstance(TRANSFORMATION);
            ecipher.init(Cipher.ENCRYPT_MODE, key, iv);
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
            Cipher dcipher = Cipher.getInstance(TRANSFORMATION);
            dcipher.init(Cipher.DECRYPT_MODE, key, iv);
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
     * @param passPhrase
     * @param salt
     * @return
     */
    private SecretKey genKey(String passPhrase, byte[] salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKey tmp = secretKeyFactory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 由于不清楚this.secretKey是否线程安全,这里clone一个出来
     *
     * @return
     */
    private SecretKeySpec cloneSecretKeySpec() {
        return new SecretKeySpec(this.secretKey.getEncoded(), this.secretKey.getAlgorithm());
    }
}
