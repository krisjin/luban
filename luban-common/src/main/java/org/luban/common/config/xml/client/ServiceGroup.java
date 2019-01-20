package org.luban.common.config.xml.client;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.luban.common.config.xml.common.XmlPropertyElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 从配置文件中读取group的配置
 * <p/>
 * User: libinxy
 * Date: 11-10-19
 * Time: 上午11:32
 *
 * @author dongyong.wang@ran-inc.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class ServiceGroup {

    @XmlJavaTypeAdapter(ClientMapAdapter.class)
    private Groups groups = null;

    /**
     * 取得group的配置
     *
     * @return
     */
    public Map<String, Group> getGroups() {
        if (groups == null) {
            return Maps.newHashMap();
        }
        return groups.groups;
    }

    /**
     * 取出所有group使用的公共配置
     *
     * @return
     */
    public Map<String, String> getGroupsProperties() {
        if (groups == null) {
            return Maps.newHashMap();
        }
        return groups.properties;
    }

    /**
     * 封装groups的信息,包括group列表及仅有配置properties属性
     */
    public static final class Groups {
        private Map<String, Group> groups = Maps.newHashMap();
        private Map<String, String> properties = Maps.newHashMap();

        public Groups(Map<String, Group> groups, Map<String, String> properties) {
            this.groups = groups;
            this.properties = properties;
        }
    }

    /**
     * 从配置文件中生成对象的配置
     */
    public static final class ClientMapAdapter extends XmlAdapter<ClientConfigType, Groups> {
        public ClientMapAdapter() {
        }

        @Override
        public Groups unmarshal(ClientConfigType v) throws Exception {
            Map<String, Group> groups = Maps.newHashMap();
            for (Group p : v.getGroups()) {
                Preconditions.checkArgument(!groups.containsKey(p.getName()), "Duplicate key %s", p.getName());
                groups.put(p.getName(), p);
            }
            return new Groups(groups, v.getProperties());
        }

        @Override
        public ClientConfigType marshal(Groups v) throws Exception {
            List<Group> list = Lists.newArrayList();
            Set<Map.Entry<String, Group>> entries = v.groups.entrySet();
            for (Map.Entry<String, Group> entry : entries) {
                list.add(entry.getValue());
            }
            Map<String, String> properties = v.properties;
            ClientConfigType clientConfigType = new ClientConfigType();
            clientConfigType.setGroups(list);
            clientConfigType.setProperties(properties);
            return clientConfigType;
        }


    }

    /**
     * xml配置点
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ClientConfigType {
        @XmlElement(name = "group")
        private List<Group> groups;

        @XmlJavaTypeAdapter(XmlPropertyElement.MapAdapter.class)
        private Map<String, String> properties = Maps.newHashMap();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }
    }
}


