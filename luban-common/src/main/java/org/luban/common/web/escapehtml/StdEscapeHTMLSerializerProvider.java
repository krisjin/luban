package org.luban.common.web.escapehtml;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/**
 * 自定义StdEscapeHTMLSerializerProvider的实现
 * 添加JacksonStdImpl注解，则StringArraySerializer使用自身方法处理String
 * 不添加，则判断是否存在String的Serializer存在，存在使用，不存在使用自身方法
 */
public class StdEscapeHTMLSerializerProvider extends StdSerializerProvider {

    @SuppressWarnings("unchecked")
    public StdEscapeHTMLSerializerProvider(SerializationConfig serializationConfig) throws JsonMappingException {
        super(serializationConfig, new StdSerializerProvider(), BeanSerializerFactory.instance);
        JsonSerializer stringEscapeHTMLSerializer = new StringEscapeHTMLSerializer();
        _serializerCache.addAndResolveNonTypedSerializer(String.class, stringEscapeHTMLSerializer, this);
        JavaType javaType = TypeFactory.fromCanonical(String.class.getName());
        _serializerCache.addAndResolveNonTypedSerializer(javaType, stringEscapeHTMLSerializer, this);

    }

    public StdEscapeHTMLSerializerProvider(SerializationConfig config, StdEscapeHTMLSerializerProvider src, SerializerFactory jsf) {
        super(config, src, jsf);
    }

    /**
     * 重写createInstance函数
     *
     * @param config
     * @param jsf
     * @return
     */
    @Override
    protected StdSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new StdEscapeHTMLSerializerProvider(config, this, jsf);
    }

}