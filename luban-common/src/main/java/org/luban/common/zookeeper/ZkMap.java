package org.luban.common.zookeeper;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bscl.common.thread.ThreadUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.luban.common.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用Zookepper作为数据源的Map,以制定的nodePath作为根节点,该节点下的每一个节点都视作一个key － value对。
 * 节点的名称作为key,节点的data作为value.
 * <p/>
 * 对于ZkMap,我们设定了两种模式:严格模式和普通模式.
 * 严格模式,{@link #restrict}为true
 * 在该模式下,如果与zookeeper server的联系发生错误({@link #broken}为true时),认为本地的数据不可信,会禁用本地数据,适用与对一致性要求严格的场景;在这种模式下,{@link #get(Object)}会抛出{@link IllegalStateException}
 * 普通模式:
 * 在该模式下,如果与zookeeper server的联系发生错误({@link #broken}为true时),本地的数据仍然可用,但是此时本地的数据可能已经与zk server不同步了，适用于对一致性要求不严格的场景
 * <p/>
 */
public final class ZkMap<V> extends ForwardingMap<String, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkMap.class);
    private final BackoffHelper backoffHelper = new BackoffHelper();
    private final String nodePath;
    private final Function<byte[], V> decoder;
    private final boolean restrict;
    private volatile ConcurrentMap<String, V> localMap = Maps.newConcurrentMap();
    private volatile boolean broken = false;
    private volatile boolean reWatch = false;
    private final ZkClient zkClient;
    private final Object mapLock = new Object();

    /**
     * @param zkClient
     * @param nodePath
     * @param decoder
     * @param <V>
     * @return
     * @throws IllegalStateException
     */
    public static <V> ZkMap<V> createZkMap(ZkClient zkClient, String nodePath, Function<byte[], V> decoder) {
        ZkMap<V> zkMap = new ZkMap<V>(zkClient, nodePath, decoder, false);
        zkMap.init();
        return zkMap;
    }

    /**
     * @param zkClient
     * @param nodePath
     * @param decoder
     * @param <V>
     * @return
     * @throws IllegalStateException
     */
    public static <V> ZkMap<V> createRestrictZkMap(ZkClient zkClient, String nodePath, Function<byte[], V> decoder) {
        ZkMap<V> zkMap = new ZkMap<V>(zkClient, nodePath, decoder, true);
        zkMap.init();
        return zkMap;
    }

    /**
     * @param zkClient
     * @param nodePath
     * @param decoder
     * @throws IllegalStateException
     */
    private ZkMap(ZkClient zkClient, String nodePath, Function<byte[], V> decoder, boolean restrict) {
        this.zkClient = Preconditions.checkNotNull(zkClient);
        this.nodePath = Preconditions.checkNotNull(nodePath);
        this.decoder = Preconditions.checkNotNull(decoder);
        this.restrict = restrict;
    }

    public V get(Object key) {
        if (restrict && broken) {
            throw new IllegalStateException("The ZkMap is broken at restirct mode,can't trust the local data,please procces this at your code :(.");
        }
        return super.get(key);
    }

    @Override
    public V remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V put(String key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Map<String, V> delegate() {
        return localMap;
    }

    /**
     * 执行初始化操作
     *
     * @throws IllegalStateException 初始化失败后会抛出这个异常
     */
    private void init() {
        Watcher expriedWatcher = zkClient.registerExpirationHandler(new Runnable() {
            @Override
            public void run() {
                onKeeperException(new IllegalStateException("Mach exipration event,try to recover."), "");
            }
        });
        DisconnectedWatcher disconnectedWatcher = new DisconnectedWatcher();
        zkClient.register(disconnectedWatcher);
        try {
            updateChildren();
        } catch (Exception e) {
            zkClient.unregister(expriedWatcher);
            zkClient.unregister(disconnectedWatcher);
            throw new IllegalStateException("Can't init the map for nodePath " + this.nodePath);
        }
    }

    /**
     * 从zk取得所有的子节点构建本地map
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void updateChildren() throws InterruptedException, KeeperException {
        synchronized (mapLock) {
            Stat exists = getZkClient().exists(nodePath, new RootNodeWatcher());
            if (exists == null) {
                throw new KeeperException.NoNodeException(nodePath);
            }
            List<String> children = getZkClient().getChildren(nodePath, new ChildrenWatcher());
            LOGGER.debug("Get children from zookeeper node path {},values:{}.", this.nodePath, children);
            HashSet<String> zkChildren = Sets.newHashSet(children);
            updateLocalMap(zkChildren);
        }
    }


    /**
     * 更新本地map
     *
     * @param zkChildren
     */
    private void updateLocalMap(Set<String> zkChildren) throws InterruptedException, KeeperException {
        synchronized (mapLock) {
            Set<String> addedChildren = Sets.difference(zkChildren, localMap.keySet());
            Set<String> removedChildren = Sets.difference(localMap.keySet(), zkChildren);
            for (String child : addedChildren) {
                addChild(child);
            }
            for (String child : removedChildren) {
                localMap.remove(child);
            }
        }
    }

    /**
     * 取得zk上child的数据后加入到本地的map中
     *
     * @param child
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void addChild(final String child) throws InterruptedException, KeeperException {
        synchronized (mapLock) {
            this.addChild(child, localMap);
        }
    }

    /**
     * @param child
     * @param map
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void addChild(final String child, ConcurrentMap<String, V> map) throws InterruptedException, KeeperException {
        final Watcher nodeWatcher = new ChildDataWatcher(child);
        try {
            V value = decoder.apply(getZkClient().getData(makePath(child), nodeWatcher, null));
            map.put(child, value);
        } catch (KeeperException.NoNodeException e) {
            map.remove(child);
        }
    }

    /**
     * 发生Zookeeper的异常,对于这种异常,目前认为是不可恢复的,需要重新从zk服务器拉数据
     *
     * @param e
     * @param msg
     */
    private void onKeeperException(Exception e, String msg) {
        LOGGER.error(msg, e);
        synchronized (mapLock) {
            if (reWatch) {
                LOGGER.warn("Rewatch still running,ignore it.");
                return;
            }
            this.reWatch = true;
            this.broken = true;
            //在异步线程中持续恢复，直到成功为止
            backoffHelper.doUntilSuccessAtExecutor(new Function<Void, Boolean>() {
                @Override
                public Boolean apply(@Nullable Void input) {
                    try {
                        synchronized (mapLock) {
                            updateChildren();
                            broken = false;
                            reWatch = false;
                        }
                        return Boolean.TRUE;
                    } catch (Exception e) {
                        LOGGER.error("Recover map for node " + nodePath + " fail.", e);
                    }
                    return Boolean.FALSE;
                }
            });
        }
    }

    private void onInterrupted(InterruptedException e) {
        ThreadUtil.onInterruptedException(e);
    }

    /**
     * @return
     */
    private ZooKeeper getZkClient() {
        return zkClient.get(ZookeeperConstants.CONNECTION_TIMEOUT);
    }

    /**
     * 构造节点的路径
     *
     * @param child
     * @return
     */
    private String makePath(String child) {
        return this.nodePath + "/" + child;
    }

    private class DisconnectedWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.None && event.getState() == Event.KeeperState.Disconnected) {
                LOGGER.warn("Disconnect from zookeeper server,event{}", event);
                if (ZkMap.this.restrict) {
                    onKeeperException(new IllegalStateException("Disconnect from zookeeper server at restrict mode,try to recover it."), "");
                }
            }
        }
    }


    /**
     * The watcher of the nodePath
     */
    private class RootNodeWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            synchronized (mapLock) {
                if (event.getType() == Event.EventType.NodeDeleted) {
                    LOGGER.warn("The node {} has been deleted,clear local map and try to watch the root node.", nodePath);
                    localMap.clear();
                    try {
                        getZkClient().exists(nodePath, new RootNodeWatcher());
                    } catch (KeeperException e) {
                        onKeeperException(e, "Watch the node " + nodePath + " fail");
                    } catch (InterruptedException e) {
                        onInterrupted(e);
                    }
                } else if (event.getType() == Event.EventType.NodeCreated) {
                    LOGGER.warn("The node {} has beean created,refetch", nodePath);
                    try {
                        updateChildren();
                    } catch (KeeperException e) {
                        onKeeperException(e, "Prcoess event " + event + " fail.");
                    } catch (InterruptedException e) {
                        onInterrupted(e);
                    }
                }
            }
        }
    }

    /**
     * The watcher of the children of the nodePath.
     */
    private class ChildrenWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            synchronized (mapLock) {
                if (broken) {
                    LOGGER.warn("The map is broken,skip event {}", event);
                    return;
                }
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        updateChildren();
                    } catch (KeeperException e) {
                        onKeeperException(e, "Prcoess event " + event + " fail.");
                    } catch (InterruptedException e) {
                        onInterrupted(e);
                    }
                }
            }
        }
    }

    /**
     * 子节点的数据变化Watcher
     */
    private class ChildDataWatcher implements Watcher {
        private final String child;

        public ChildDataWatcher(String child) {
            this.child = child;
        }

        @Override
        public void process(WatchedEvent event) {
            synchronized (mapLock) {
                if (event.getType() == Event.EventType.NodeDataChanged) {
                    try {
                        addChild(child);
                    } catch (KeeperException e) {
                        onKeeperException(e, "Prcoess event " + event + " fail.");
                    } catch (InterruptedException e) {
                        onInterrupted(e);
                    }
                } else if (event.getType() == Event.EventType.NodeDeleted) {
                    localMap.remove(child);
                }
            }
        }
    }
}
