package net.common.utils.xml;

import com.google.common.io.Closeables;
import com.google.common.io.Resources;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URL;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/30
 * Time: 16:33
 */
public final class JAXBUtil {

    private JAXBUtil() {
    }


    /**
     * 从指定的xml文件中解析出<code>T</code>类型的对象
     *
     * @param clazz
     * @param xmlInClassPath
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(Class<T> clazz, String xmlInClassPath) {
        Reader reader = null;
        try {
            URL xmlUrl = Resources.getResource(xmlInClassPath);
            reader = new InputStreamReader(xmlUrl.openStream(), "UTF-8");
            JAXBContext context = JAXBContext.newInstance(clazz);
            return (T) context.createUnmarshaller().unmarshal(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unmarshal object for class:" + clazz + " xml:" + xmlInClassPath, e);
        } finally {
            Closeables.closeQuietly(reader);
        }
    }

    /**
     * 从指定的xml字符串中解析出<code>T</code>类型的对象
     *
     * @param clazz
     * @param xmlContent
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshalByContent(Class<T> clazz, String xmlContent) {
        Reader reader = null;
        try {
            reader = new StringReader(xmlContent);
            JAXBContext context = JAXBContext.newInstance(clazz);
            return (T) context.createUnmarshaller().unmarshal(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unmarshal object for class:" + clazz + " xmlContent:" + xmlContent, e);
        } finally {
            Closeables.closeQuietly(reader);
        }
    }

    public static void marshal(Object obj, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, out);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public static void marshal(Object obj, Writer writer) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, writer);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }
}
