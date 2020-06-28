package org.luban.common.web.json;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.StringWriter;

/**
 * User: krisjin
 * Date: 2016/10/8
 */
public final class JsonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonFactory jsonFactory = new JsonFactory();

    static {
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }


    public static <T> T fromJson(String jsonAsString, Class<T> clazz) throws Exception {
        return (T) objectMapper.readValue(jsonAsString, clazz);
    }

    public static String toJson(Object object, boolean prettyPrint) {
        StringWriter stringWriter = new StringWriter();
        try {
            JsonGenerator jg = jsonFactory.createJsonGenerator(stringWriter);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            objectMapper.writeValue(jg, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }


    public static String toJson(Object object) {
        return toJson(object, false);
    }

    public static <T> T fromJson(String jsonContent, TypeReference typeReference) {
        T rtnRst = null;
        try {
            rtnRst = (T) objectMapper.readValue(jsonContent, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnRst;
    }
}
