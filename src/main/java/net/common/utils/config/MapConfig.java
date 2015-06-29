package net.common.utils.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/29
 * Time: 16:29
 */
public class MapConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapConfig.class);

    private String appConfPath;

    /**
     * 从配置中取得int类型的值,如果name不存在,则返回默认值
     *
     * @param name
     * @param config
     * @param defaultValue
     * @return
     */
    public static int getInt(@Nonnull String name, @Nullable Map<String, String> config, int defaultValue) {
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
    public static byte getByte(@Nonnull String name, @Nullable Map<String, String> config, byte defaultValue) {
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
    public static long getLong(@Nonnull String name, @Nullable Map<String, String> config, long defaultValue) {
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
    public static String getString(@Nonnull String name, @Nullable Map<String, String> config, String defaultValue) {
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
    public static boolean getBoolean(@Nonnull String name, @Nullable Map<String, String> config, boolean defaultValue) {
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


    public static ImmutableMap<String, String> parseConf(String appConfPath) {
        Map<String, String> all = Maps.newHashMap();
        if (appConfPath != null) {
            String[] appConfs = appConfPath.split(",");
            List<String> configList = Lists.newArrayList();
            for (final String conf : appConfs) {
                if (configList.contains(conf)) {
                    LOGGER.warn("Found duplicate same config file name: {} ", conf);
                    continue;
                }
                configList.add(conf);
                LOGGER.info("Load config from " + conf);
                Map<String, String> confMap = XmlProp.loadFromXml(conf);
                if (confMap != null) {
                    Set<Map.Entry<String, String>> entries = confMap.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        String key = entry.getKey();
                        String s = all.get(key);
                        if (s != null) {
                            LOGGER.warn("Found duplicate key {}:{},will be overrided by new value{},config:{}", new Object[]{key, s, entry.getValue(), conf});
                        }
                        all.put(key, entry.getValue());
                    }
                }
            }
        }
        return ImmutableMap.<String, String>builder().putAll(all).build();
    }

    public String getAppConfPath() {
        return appConfPath;
    }

    public void setAppConfPath(String appConfPath) {
        this.appConfPath = appConfPath;
    }
}
