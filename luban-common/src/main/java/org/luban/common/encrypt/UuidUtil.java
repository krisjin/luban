package org.luban.common.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * UUID工具类
 */
public final class UuidUtil {

    private UuidUtil() {
    }

    /**
     * 原始UUID的连接符
     */
    private static final String STR_LINK = "-";

    /**
     * 被替换成空字符串
     */
    private static final String STR_NULL = "";

    /**
     * 生成原始带分隔符的UUID
     *
     * @return
     */
    public static String genOriginUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成去除分隔符的UUID
     *
     * @return
     */
    public static String genTerseUuid() {
        return genOriginUuid().replace(STR_LINK, STR_NULL);
    }


    public static String uuidToBase64(UUID uuid) {
        Base64 base64 = new Base64();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return new String(base64.encode(bb.array()));
    }

    public static String uuidFromBase64(String str) {
        byte[] bytes = Base64.decodeBase64(str.getBytes());
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }

    public static String generate24UUID() {
        UUID uuid = UUID.randomUUID();
        return uuidToBase64(uuid);
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());
        String shortUUID = uuidToBase64(uuid);
        System.out.println(shortUUID);
        System.out.println(generateUUID());

    }

}