package org.bscl.common.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 读取写入XML格式的{@link java.util.Properties}
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/29
 * Time: 15:08
 */
public final class XmlProp {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlProp.class);

    private XmlProp() {
    }

    /**
     * 加载指定名称的XML配置文件,按照如下的顺序查找配置文件:
     * 1. class path
     * 2. file path
     *
     * @param xmlPropertiesPath
     * @return 如果未找到对应的配置文件, 则返回null
     */
    public static Map<String, String> loadFromXml(String xmlPropertiesPath) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(xmlPropertiesPath), "xmlPropertiesPath");
        InputStream in = null;
        try {
            in = XmlProp.class.getClassLoader().getResourceAsStream(xmlPropertiesPath);
            if (in != null) {
                LOGGER.info("Found the xml properties [{}] in class path,use it", xmlPropertiesPath);
                return loadFromXml(in);
            }
            File inFile = new File(xmlPropertiesPath);
            if (inFile.isFile()) {
                LOGGER.info("Found the xml properties [{}] in file path,use it", xmlPropertiesPath);
                in = new FileInputStream(new File(xmlPropertiesPath));
                return loadFromXml(in);
            }
        } catch (Exception e) {
            LOGGER.error("Load xml properties [" + xmlPropertiesPath + "] error.", e);
        } finally {
            Closeables.closeQuietly(in);
        }
        LOGGER.warn("Can't find the xml properties file [{}] in both class and file path", xmlPropertiesPath);
        return null;
    }

    /**
     * 从输入流中加载配置文件
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static Map<String, String> loadFromXml(InputStream in) throws IOException {
        Preconditions.checkNotNull(in, "in");
        Properties properties = new Properties();
        properties.loadFromXML(in);
        Map<String, String> map = Maps.newHashMap();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }
        return map;
    }

    /**
     * 将配置以xml格式写入到输出流中
     *
     * @param map
     * @param out
     * @throws IOException
     */
    public static void storeToXML(Map<String, String> map, OutputStream out, String comment) throws IOException {
        Preconditions.checkNotNull(map, "map");
        Preconditions.checkNotNull(out, "out");
        Properties properties = new Properties();
        properties.putAll(map);
        properties.storeToXML(out, comment);
    }

}
