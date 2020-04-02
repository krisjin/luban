package org.luban.common.plugin;


import org.luban.common.network.URL;

/**
 * 服务插件接口
 *
 * @author hexiaofeng
 * @version 1.0.0
 * @since 12-12-12 下午8:47
 */
public interface ServicePlugin {

    /**
     * 返回类型
     *
     * @return
     */
    String getType();

    /**
     * 设置URL
     *
     * @param url
     */
    void setUrl(URL url);
}
