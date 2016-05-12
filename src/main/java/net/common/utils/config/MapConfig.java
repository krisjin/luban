package net.common.utils.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String confPath;

    private MapConfig() {
    }


    public static ImmutableMap<String, String> parseConf(String confPath) {
        Map<String, String> all = Maps.newHashMap();
        if (confPath != null) {
            String[] appConfs = confPath.split(",");
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

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }
}
