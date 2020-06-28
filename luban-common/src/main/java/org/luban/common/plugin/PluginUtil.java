package org.luban.common.plugin;


import org.luban.common.network.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件工具类
 *
 * @author hexiaofeng
 * @version 1.0.0
 * @since 12-12-12 下午8:42
 */
public class PluginUtil {

    private static ConcurrentHashMap<String, ServicePlugin> plugins = new ConcurrentHashMap<String, ServicePlugin>();

    /**
     * 使用SPI加载插件
     *
     * @param clazz
     * @param url
     * @param <T>
     * @return
     */
    public static <T extends ServicePlugin> T createService(Class<T> clazz, URL url) {
        if (url == null || clazz == null) {
            return null;
        }

        T result = null;
        String key = null;
        //判断是否共享
        Share share = clazz.getAnnotation(Share.class);
        if (share != null) {
            // 判断是否排除
            boolean excluded = false;
            String[] excludes = share.excludes();
            if (excludes != null) {
                for (String exclude : excludes) {
                    if (url.getProtocol().equals(exclude)) {
                        excluded = true;
                        break;
                    }
                }
            }
            if (!excluded) {
                if (share.shareKey() == Share.ShareKey.ADDRESS) {
                    key = clazz.getName() + "_" + url.getProtocol() + "://" + url.getAddress();
                } else {
                    key = clazz.getName() + "_" + url.toString();
                }
                result = (T) plugins.get(key);
                if (result != null) {
                    return result;

                }
            }
        }

        ServiceLoader<T> loader = ServiceLoader.load(clazz, clazz.getClassLoader());
        for (T service : loader) {
            if (url.getProtocol().equalsIgnoreCase(service.getType())) {
                //找到该类型插件
                result = service;
                result.setUrl(url);
                if (share != null && key != null) {
                    //共享，缓存一份
                    T old = (T) plugins.putIfAbsent(key, result);
                    if (old != null) {
                        return old;
                    }
                }
                return result;
            }
        }
        return null;

    }

    /**
     * 加载插件
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends ServicePlugin> List<T> loadPlugins(Class<T> clazz) {
        List<T> plugins = new ArrayList<T>();
        ServiceLoader<T> loader = ServiceLoader.load(clazz, clazz.getClassLoader());
        for (T plugin : loader) {
            plugins.add(plugin);
        }
        return plugins;
    }
}
