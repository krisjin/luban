package org.luban.common.config;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * 配置基础操作
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/31
 * Time: 10:29
 */
public class MapConfigOperate {

    /**
     * 从配置中取得int类型的值,如果name不存在,则返回默认值
     *
     * @param name
     * @param config
     * @param defaultValue
     * @return
     */
    public int getInt(@Nonnull String name, @Nullable Map<String, String> config, int defaultValue) {
        Preconditions.checkArgument(name != null, "name");
        if (config == null) {
            return defaultValue;
        }
        String s = config.get(name);
        if (s == null) {
            return defaultValue;
        }
        return Integer.parseInt(s);
    }

    /**
     * 从配置中取得byte类型的值,如果name不存在,则返回默认值
     *
     * @param name
     * @param config
     * @param defaultValue
     * @return
     */
    public byte getByte(@Nonnull String name, @Nullable Map<String, String> config, byte defaultValue) {
        Preconditions.checkArgument(name != null, "name");
        if (config == null) {
            return defaultValue;
        }
        String s = config.get(name);
        if (s == null) {
            return defaultValue;
        }
        return Byte.parseByte(s);
    }

    /**
     * 从配置中取得long类型的值,如果name不存在,则返回默认值
     *
     * @param name
     * @param config
     * @param defaultValue
     * @return
     */
    public long getLong(@Nonnull String name, @Nullable Map<String, String> config, long defaultValue) {
        Preconditions.checkArgument(name != null, "name");
        if (config == null) {
            return defaultValue;
        }
        String s = config.get(name);
        if (s == null) {
            return defaultValue;
        }
        return Long.parseLong(s);
    }

    /**
     * 从配置中取得String类型的值,如果name不存在,则返回默认值
     *
     * @param name
     * @param config
     * @param defaultValue
     * @return
     */
    public String getString(@Nonnull String name, @Nullable Map<String, String> config, String defaultValue) {
        Preconditions.checkArgument(name != null, "name");
        if (config == null) {
            return defaultValue;
        }
        String s = config.get(name);
        if (s == null) {
            return defaultValue;
        }
        return s;
    }


    /**
     * 从配置文件中取得boolean类型的值,如果name不存在,则返回默认值<code>defaultValue</code>
     *
     * @param name
     * @param config
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(@Nonnull String name, @Nullable Map<String, String> config, boolean defaultValue) {
        Preconditions.checkArgument(name != null, "name");
        if (config == null) {
            return defaultValue;
        }
        String s = config.get(name);
        if (s == null) {
            return defaultValue;
        }
        return Boolean.valueOf(s);
    }

}
