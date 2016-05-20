package org.bscl.common.config.xml.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class Servers {
    @XmlJavaTypeAdapter(ServerMapAdapter.class)
    private Map<String, Server> servers = Maps.newHashMap();

    /**
     * 取得服务器列表map:
     * key: 服务器名称
     * value: 服务器实例
     *
     * @return
     */
    public Map<String, Server> getServers() {
        return servers;
    }

    public void setServers(Map<String, Server> servers) {
        this.servers = servers;
    }

    public static final class ServerMapAdapter extends XmlAdapter<ServerConfigType, Map<String, Server>> {
        public ServerMapAdapter() {
        }

        @Override
        public Map<String, Server> unmarshal(ServerConfigType v) throws Exception {
            Map<String, Server> map = Maps.newHashMap();
            for (Server p : v.getServers()) {
                Preconditions.checkArgument(!map.containsKey(p.getName()), "Duplicate key %s", p.getName());
                map.put(p.getName(), p);
            }
            return map;
        }

        @Override
        public ServerConfigType marshal(Map<String, Server> v) throws Exception {
            List<Server> list = Lists.newArrayList();
            Set<Map.Entry<String, Server>> entries = v.entrySet();
            for (Map.Entry<String, Server> entry : entries) {
                list.add(entry.getValue());
            }
            ServerConfigType serverConfigType = new ServerConfigType();
            serverConfigType.setServers(list);
            return serverConfigType;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ServerConfigType {
        @XmlElement(name = "server")
        private List<Server> servers;

        public List<Server> getServers() {
            return servers;
        }

        public void setServers(List<Server> servers) {
            this.servers = servers;
        }
    }

}
