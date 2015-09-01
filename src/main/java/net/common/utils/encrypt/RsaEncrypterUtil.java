package net.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密器
 */
public class RsaEncrypterUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RsaEncrypterUtil.class);

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA密钥长度
     */
    private static final int RSA_KEY_SIZE = 1024;

    /**
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(RSA_KEY_SIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<String, Object>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (Exception e) {
            LOGGER.error("gen key pair failed", e);
        }
        return null;
    }

    /**
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, Key privateKey) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                offSet += MAX_ENCRYPT_BLOCK;
            }
            return out.toByteArray();
        } catch (Exception e) {
            LOGGER.error("encrypt by privateKey failed", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error("close ByteArrayOutputStream failed", e);
            }
        }
        return null;
    }

    /**
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, Key privateKey) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                offSet += MAX_DECRYPT_BLOCK;
            }
            return out.toByteArray();
        } catch (Exception e) {
            LOGGER.error("decrypt by privateKey failed", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error("close ByteArrayOutputStream failed", e);
            }
        }
        return null;
    }

    /**
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, Key publicKey) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                offSet += MAX_ENCRYPT_BLOCK;
            }
            return out.toByteArray();
        } catch (Exception e) {
            LOGGER.error("encrypt by publicKey failed", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error("close ByteArrayOutputStream failed", e);
            }
        }
        return null;
    }

    /**
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, Key publicKey) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                offSet += MAX_DECRYPT_BLOCK;
            }
            return out.toByteArray();
        } catch (Exception e) {
            LOGGER.error("decrypt by publicKey failed", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error("close ByteArrayOutputStream failed", e);
            }
        }
        return null;
    }

    /**
     * 使用私钥获取私钥字符串
     *
     * @param privateKey
     * @return
     */
    public static String getPrivateKey(Key privateKey) {
        return new String(Base64.encodeBase64(privateKey.getEncoded()));
    }

    /**
     * 使用私钥字符串获取私钥
     *
     * @param privateKey
     * @return
     */
    public static Key getPrivateKey(String privateKey) {
        byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
        return getPrivateKey(keyBytes);
    }

    /**
     * 使用私钥二进制获取私钥
     *
     * @param privateKey
     * @return
     */
    public static Key getPrivateKey(byte[] privateKey) {
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            LOGGER.error("get privateKey failed", e);
        }
        return null;
    }

    /**
     * 使用公钥获取公钥字符串
     *
     * @param publicKey
     * @return
     */
    public static String getPublicKey(Key publicKey) {
        return new String(Base64.encodeBase64(publicKey.getEncoded()));
    }

    /**
     * 使用公钥字符串获取公钥
     *
     * @param publicKey
     * @return
     */
    public static Key getPublicKey(String publicKey) {
        byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
        return getPublicKey(keyBytes);
    }

    /**
     * 使用公钥二进制获取公钥
     *
     * @param publicKey
     * @return
     */
    public static Key getPublicKey(byte[] publicKey) {
        try {
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            LOGGER.error("get publicKey failed", e);
        }
        return null;
    }

    /**
     * 使用DER格式公钥获取公钥
     *
     * @param publicKey
     * @return
     */
    public static Key getDERPublicKey(String publicKey) {
        byte[] temp = Base64.decodeBase64(publicKey);
        return getDERPublicKey(temp);
    }

    /**
     * 使用DER公钥二进制获取公钥
     *
     * @param publicKey
     * @return
     */
    public static Key getDERPublicKey(byte[] publicKey) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(publicKey));
            return certificate.getPublicKey();
        } catch (Exception e) {
            LOGGER.error("get publicKey failed", e);
        }
        return null;
    }
}
