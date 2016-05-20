package org.bscl.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供给线程使用的工具类
 */
public final class ThreadUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtil.class);

    private ThreadUtil() {
    }

    /**
     * 处理Thread产生的{@link InterruptedException},记录异常日志,重置当前线程的中断状态
     *
     * @param e
     * @see {@link Thread#interrupt()}
     */
    public static void onInterruptedException(InterruptedException e) {
        LOGGER.error("Catch an interruppted exception,reset the interrupted status", e);
        Thread.currentThread().interrupt();
    }
}
