package net.common.utils;

import net.common.utils.config.xml.server.Server;
import net.common.utils.config.xml.server.Servers;
import net.common.utils.xml.JAXBUtil;

import java.util.Map;
import java.util.Set;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/30
 * Time: 17:44
 */
public class XmlTest {

    public static void main(String[] args) {

        Servers servers = JAXBUtil.unmarshal(Servers.class, "redis-servers.xml");

        Map<String, Server> map = servers.getServers();
        Set<Map.Entry<String, Server>> entrySet = map.entrySet();
        for (Map.Entry<String, Server> entry : entrySet) {

            String name = entry.getKey();
            Server server = entry.getValue();
        }
    }
}
