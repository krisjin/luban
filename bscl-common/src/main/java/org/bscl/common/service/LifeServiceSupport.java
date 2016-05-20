package org.bscl.common.service;


import com.google.common.util.concurrent.AbstractService;


/**
 * 有启动和停止阶段的服务基类,即服务的启动通过{}
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/10
 * Time: 20:21
 */
public class LifeServiceSupport extends AbstractService implements ILifeService {

    @Override
    protected final void doStart() {
        try {
            execStart();
            notifyStarted();
        } catch (Exception e) {
            notifyFailed(e);
        }
    }

    @Override
    protected final void doStop() {
        try {
            execStop();
            notifyStopped();
        } catch (Exception e) {
            notifyFailed(e);
        }
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * 实际的启动操作
     */
    protected void execStart() {

    }

    /**
     * 实际的停止操作
     */
    protected void execStop() {

    }
}