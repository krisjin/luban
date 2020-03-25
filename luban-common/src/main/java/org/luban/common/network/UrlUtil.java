package org.luban.common.network;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 提供URL及URI的相关处理功能
 * User: krisjin
 * Date: 2015/11/14
 */
public class UrlUtil {

    /**
     * URL prefix for loading from the file system: "file:"
     */
    public static final String FILE_URL_PREFIX = "file:";

    private UrlUtil() {
        super();
    }

    /**
     * 取得资源定位对象
     *
     * @param resourceLocation 资源位置
     * @throws RuntimeException 抛出条件：文件路径不是一个URL, 文件资源不存在
     */
    public static URL getURL(String resourceLocation) throws RuntimeException {
        return getURL(resourceLocation, null);
    }

    /**
     * 取得资源定位对象
     *
     * @param resourceLocation 资源位置
     * @throws RuntimeException 抛出条件：文件路径不是一个URL, 文件资源不存在
     */
    public static URL getURL(String resourceLocation, ClassLoader loader) throws RuntimeException {
        if (resourceLocation.startsWith(FILE_URL_PREFIX)) {
            resourceLocation = resourceLocation.substring(FILE_URL_PREFIX.length());
        }
        File file = new File(resourceLocation);
        if (file.exists()) {
            try {
                return new URL(resourceLocation);
            } catch (MalformedURLException ex) {
                try {
                    return new URL(FILE_URL_PREFIX + resourceLocation);
                } catch (MalformedURLException ex2) {
                    throw new RuntimeException("The [resourceLocation=" + resourceLocation + "] is neither a URL not a well-formed file path.", ex2);
                }
            }
        }
        URL url = null;
        if (loader != null) {
            url = loader.getResource(resourceLocation);
        }
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(resourceLocation);
            if (url == null) {
                url = UrlUtil.class.getClassLoader().getResource(resourceLocation);
                if (url == null) {
                    throw new RuntimeException("Class path resource [" + resourceLocation + "] cannot be resolved to URL because it does not exist.");
                }
            }
        }
        return url;
    }
}
