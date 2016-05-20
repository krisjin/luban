package org.bscl.common.config.xml.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/30
 * Time: 16:56
 */
public class XmlPropertyElement {
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PropertiesType {
        @XmlElement(name = "property")
        private List<PropEntry> properties;

        public List<PropEntry> getProperties() {
            return properties;
        }

        public void setProperties(List<PropEntry> properties) {
            this.properties = properties;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PropEntry {
        @XmlAttribute(name = "name")
        private String key;
        @XmlValue
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static final class MapAdapter extends XmlAdapter<PropertiesType, Map<String, String>> {
        public MapAdapter() {
        }

        @Override
        public Map<String, String> unmarshal(PropertiesType v) throws Exception {
            Map<String, String> map = Maps.newHashMap();
            for (PropEntry p : v.getProperties()) {
                Preconditions.checkArgument(!map.containsKey(p.key), "Duplicate key %s", p.key);
                map.put(p.key, p.value);
            }
            return map;
        }

        @Override
        public PropertiesType marshal(Map<String, String> v) throws Exception {
            List<PropEntry> list = Lists.newArrayList();
            Set<Map.Entry<String, String>> entries = v.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                PropEntry p = new PropEntry();
                p.key = entry.getKey();
                p.value = entry.getValue();
                list.add(p);
            }
            PropertiesType propertiesType = new PropertiesType();
            propertiesType.setProperties(list);
            return propertiesType;
        }
    }
}