package org.bscl.common.mq;

import org.bscl.common.mq.message.IQueueMessage;

/**
 * 队列消息处理
 * 提供{@link IProcessorService} 接口，具体业务队列消息处理需要实现此接口。
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/10
 * Time: 17:36
 */
public interface IProcessorService<T extends IQueueMessage> {

    /**
     * 处理消息
     *
     * @param message
     * @return
     */
    boolean process(T message);

    String msgToString(T message);
}
