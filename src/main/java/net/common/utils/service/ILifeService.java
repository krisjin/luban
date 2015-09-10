package net.common.utils.service;

import com.google.common.util.concurrent.Service;

/**
 * 所有带有启动和停止的生命周期的service基本接口
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/10
 * Time: 20:08
 */
public interface ILifeService extends Service {

    /**
     * 取得服务的名称
     *
     * @return
     */
    public String getName();
}
