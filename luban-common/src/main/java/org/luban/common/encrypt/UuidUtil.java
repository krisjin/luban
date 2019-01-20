package org.luban.common.encrypt;

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

}