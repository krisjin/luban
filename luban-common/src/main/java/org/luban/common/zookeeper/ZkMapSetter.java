package org.luban.common.zookeeper;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.luban.common.concurrent.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 基于zk实现的设置键值对的工具类
 */
public class ZkMapSetter<V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkMapSetter.class);
    private final ZkClient zkClient;
    private final String nodePath;
    private final Function<V, byte[]> encoder;
    private final ConcurrentMap<String, V> localMap = Maps.newConcurrentMap();
    private final boolean persistent;
    private final Object lock = new Object();
    private final BackoffHelper backoffHelper = new BackoffHelper();

    /**
     * @param zkClient   zk client
     * @param nodePath   map在zk上的根节点
     * @param encoder    编码器
     * @param persistent 是否是持久化的节点,true:持久节点;false,非持久的节点
     */
    public ZkMapSetter(ZkClient zkClient, String nodePath, Function<V, byte[]> encoder, boolean persistent) {
        this.zkClient = Preconditions.checkNotNull(zkClient);
        this.nodePath = Preconditions.checkNotNull(nodePath);
        this.encoder = Preconditions.checkNotNull(encoder);
        this.persistent = persistent;
        if (!this.persistent) {
            this.zkClient.registerExpirationHandler(new Runnable() {
                @Override
                public void run() {
                    LOGGER.warn("Catch a expiration event,try to reput all functions.");
                    backoffHelper.doUntilSuccessAtExecutor(new TryToPutAllFunction());
                }
            });
        }
    }

    /**
     * 增加key -> value 键值对
     *
     * @param key
     * @param data
     * @throws InterruptedException
     * @throws KeeperException
     */
    public boolean put(String key, V data) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(data);
        ZooKeeper zooKeeper = getZk();
        if (zooKeeper == null) {
            return false;
        }
        String path = makePath(key);
        CreateMode mode = this.persistent ? CreateMode.PERSISTENT : CreateMode.EPHEMERAL;
        synchronized (lock) {
            try {
                if (zooKeeper.exists(path, false) == null) {
                    List<ACL> acl = ZkUtils.createACL(zkClient.getZkUser(), zkClient.getZkPassword());
                    zooKeeper.create(path, encoder.apply(data), acl, mode);
                } else {
                    zooKeeper.setData(path, encoder.apply(data), -1);
                }
                //zk写入成功了,记录本地的状态
                if (!persistent) {
                    localMap.put(key, data);
                }
                return true;
            } catch (InterruptedException e) {
                ThreadUtil.onInterruptedException(e);
            } catch (KeeperException e) {
                LOGGER.error("Add key [" + key + "] for node [" + nodePath + "] fail.", e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("Add key [" + key + "] for node [" + nodePath + "] fail.", e);
            }
        }
        return false;
    }

    /**
     * 删除指定的key
     *
     * @param key
     * @return true, 删除成功;false,删除失败
     * @throws InterruptedException
     * @throws KeeperException
     */
    public boolean remove(String key) {
        ZooKeeper zooKeeper = getZk();
        if (zooKeeper == null) {
            return false;
        }
        synchronized (lock) {
            try {
                zooKeeper.delete(makePath(key), -1);
                if (!persistent) {
                    localMap.remove(key);
                }
                return true;
            } catch (InterruptedException e) {
                ThreadUtil.onInterruptedException(e);
            } catch (KeeperException.NoNodeException e) {
                LOGGER.error("Remove key [" + key + "] from node [" + nodePath + "] not exist.", e);
                return true;
            } catch (KeeperException e) {
                LOGGER.error("Remove key [" + key + "] from node [" + nodePath + "] fail.", e);
            }
        }
        return false;
    }


    /**
     * @return
     */
    private ZooKeeper getZk() {
        try {
            return zkClient.get(ZookeeperConstants.CONNECTION_TIMEOUT);
        } catch (RuntimeException e) {
            LOGGER.error("Can't get zkClient in [" + ZookeeperConstants.CONNECTION_TIMEOUT + "] ms.", e);
        }
        return null;
    }

    private String makePath(String key) {
        return nodePath + "/" + key;
    }

    /**
     * 尝试将本地localMap中的数据中信提交到zk的函数
     */
    private class TryToPutAllFunction implements Function<Void, Boolean> {
        @Override
        public Boolean apply(@Nullable Void input) {
            synchronized (lock) {
                boolean result = true;
                for (String key : localMap.keySet()) {
                    V data = localMap.get(key);
                    if (!put(key, data)) {
                        result = false;
                        break;
                    }
                }
                return result;
            }
        }
    }
}
