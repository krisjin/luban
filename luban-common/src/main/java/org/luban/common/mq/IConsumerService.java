package org.luban.common.mq;

import org.luban.common.mq.message.IQueueMessage;
import org.luban.common.service.ILifeService;

/**
 * 消费者服务接口
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/10
 * Time: 20:12
 */
public interface IConsumerService<T extends IQueueMessage> extends ILifeService {


    /**
     * 启动消费者线程
     */
    void startConsumer();

    /**
     * 终止消费者线程
     */
    void stopConsumer();

    /**
     * 入队是“不阻塞”的，保证生产者(API)线程不会卡顿
     * 一旦队列满了，不阻塞等待，立即返回
     * 要求生产者自行处理失败的情景，比如:记录本地log
     *
     * @param data
     * @return
     */
    boolean offer(T data);

    /**
     * 出队是“阻塞”的，保证消费线程不会无效空转
     *
     * @return
     */
    T take() throws InterruptedException;
}
