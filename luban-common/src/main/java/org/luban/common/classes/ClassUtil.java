package org.luban.common.classes;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/7/9
 * Time: 17:37
 */
public class ClassUtil {
    /**
     * 查找包含相同类名的jar文件
     *
     * @param clazz 查找的类
     * @return 返回包含类的jar文件
     * @throws IOException
     */
    public static String findContainingJar(Class<?> clazz) {
        ClassLoader loader = clazz.getClassLoader();
        String classFile = clazz.getName().replaceAll("\\.", "/") + ".class";
        try {
            for (final Enumeration<URL> itr = loader.getResources(classFile); itr.hasMoreElements(); ) {
                final URL url = itr.nextElement();
                if ("jar".equals(url.getProtocol())) {
                    String path = url.getPath();
                    if (path.startsWith("file:")) {
                        path = path.substring("file:".length());
                    }
                    path = URLDecoder.decode(path, "UTF-8");
                    return path.replaceAll("!.*$", "");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void main(String[] args) {

        String jarName = findContainingJar(ClassUtil.class);
        System.out.println(jarName);
    }
}
