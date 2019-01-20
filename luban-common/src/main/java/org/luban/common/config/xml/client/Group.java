package org.luban.common.config.xml.client;


import com.google.common.collect.Maps;
import org.luban.common.config.xml.common.XmlPropertyElement;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * servergroup节点
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "service-groups")
public class Group {
    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlElement(name = "servers", required = true)
    private ServerList serverList;
    @XmlJavaTypeAdapter(XmlPropertyElement.MapAdapter.class)
    private Map<String, String> properties = Maps.newHashMap();

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServerList getServerList() {
        return serverList;
    }

    public void setServerList(ServerList serverList) {
        this.serverList = serverList;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ServerList {
        @XmlElement(name = "server")
        private List<ServerElement> serverElements;

        public List<ServerElement> getServerElements() {
            return serverElements == null ? new ArrayList<ServerElement>() : serverElements;
        }

        public void setServerElements(List<ServerElement> serverElements) {
            this.serverElements = serverElements;
        }
    }
}

