package org.luban.common.config.xml.server;


import com.google.common.collect.Maps;
import org.luban.common.config.xml.common.XmlPropertyElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * redis服务器配置节点
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "host")
public class Server {

    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlAttribute(name = "port", required = true)
    private String port;
    @XmlAttribute(name = "host", required = true)
    private String host;
    @XmlAttribute(name = "password", required = false)
    private String password = "";
    @XmlAttribute(name = "passport", required = false)
    private String passport = "";

    @XmlJavaTypeAdapter(XmlPropertyElement.MapAdapter.class)
    private Map<String, String> properties = Maps.newHashMap();

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        StringBuilder server = new StringBuilder();
        server.append("{");
        server.append("name:").append(this.name).append(",");
        server.append("port:").append(this.port).append(",");
        server.append("host:").append(this.host).append(",");
        server.append("password:").append(this.password);
        server.append("}");
        return server.toString();
    }
}