package org.luban.common;

import org.luban.common.config.XmlProp;
import org.luban.common.config.XmlProp;

import java.util.Map;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/29
 * Time: 15:11
 */
public class ConfigTest {

    public static void main(String[] args) {
        Map<String, String> val = XmlProp.loadFromXml("redis.conf.xml");

        String s;
    }
}
