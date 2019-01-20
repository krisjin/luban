package org.luban.common.zookeeper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ZooKeeper的客户端
 */
public class ZkClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClient.class);
    /**
     * Zookeeper服务器的地址,多个服务器以,分隔,例如"127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002"
     */
    private final String zkServers;

    /**
     * session time out
     */
    private final int sessiontTimeout;

    /**
     * Zookeeper的实例
     */
    private volatile ZooKeeper zooKeeper;

    /**
     * 是否已经关闭
     */
    private volatile boolean closed = true;

    /**
     * 是否已经shutdown
     */
    private volatile boolean shutdown = false;

    /**
     * 保留的Session状态
     */
    private volatile SessionState sessionState;

    /**
     * Zookeeper事件监听集合
     */
    private final Set<Watcher> watchers = Collections.synchronizedSet(new HashSet<Watcher>());

    /**
     * 默认的Session超时时间
     */
    public static final int DEFAULT_SESSION_TIMEOUT = 30 * 1000;

    /**
     * Zookeeper服务器登录用户名
     */
    private final String zkUser;

    /**
     * Zookeeper服务器登录密码
     */
    private final String zkPassword;


    /**
     * @param zkServers
     */
    public ZkClient(@Nonnull String zkServers, @Nonnull String zkUser, @Nonnull String zkPassword) {
        this(zkServers, DEFAULT_SESSION_TIMEOUT, zkUser, zkPassword);
    }

    /**
     * @param zkServers      ZooKeeper的服务器地址列表
     * @param sessionTimeout ZooKeeper的Session超时时间
     */
    public ZkClient(@Nonnull String zkServers, @Nonnegative int sessionTimeout, @Nonnull String zkUser, @Nonnull String zkPassword) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(zkServers), "zkServers");
        Preconditions.checkArgument(sessionTimeout >= 0, "sessionTimeout");
        this.zkServers = zkServers;
        this.sessiontTimeout = sessionTimeout;
        this.zkUser = zkUser;
        this.zkPassword = zkPassword;
    }

    /**
     * 注册ZooKeeper事件监听
     *
     * @param watcher
     */
    public void register(@Nonnull Watcher watcher) {
        Preconditions.checkArgument(watcher != null, "watcher");
        this.watchers.add(watcher);
    }

    /**
     * 取消ZooKeeper事件的监听
     *
     * @param watcher
     */
    public void unregister(@Nonnull Watcher watcher) {
        Preconditions.checkArgument(watcher != null, "watcher");
        this.watchers.remove(watcher);
    }

    /**
     * 注册一个ZooKeeper Session过期的事件.
     * 当zookeeper session过期时,使用之前的ZooKeeper实例获得的数据将不会再收到任何更新通知.
     * 因此,如果使用ZkClient的应用都应该注册该事件,当session过期的时候获得重新建立数据的机会,
     * 避免数据状态过期的问题
     *
     * @param onExpired
     * @return 注册到ZkClient的监听器
     */
    public Watcher registerExpirationHandler(@Nonnull final Runnable onExpired) {
        Preconditions.checkArgument(onExpired != null, "onExpired");
        Watcher watcher = new ExpirationWatcher(onExpired);
        register(watcher);
        return watcher;
    }

    /**
     * 连接是否关闭
     *
     * @return
     */
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * 取得ZooKeeper服务器的连接,永不超时.
     * 取得的实例不能够存储在类或者对象的属性当中,应为这个实例可能在session失效的时候被重建
     *
     * @return ZooKeeper实例
     * @throws RuntimeException 如果无法在规定的时间内取得ZooKeeper的实例,则会抛出此异常
     */
    public synchronized ZooKeeper get() {
        return this.get(0);
    }

    /**
     * 在指定的超时时间<code>connectionTimeout</code>ms内取得与ZooKeeper服务器的连接
     *
     * @param connectionTimeout 连接超时的时间,单位ms
     * @return ZooKeeper实例
     * @throws RuntimeException 如果无法在规定的时间内取得ZooKeeper的实例,则会抛出此异常
     */
    public synchronized ZooKeeper get(int connectionTimeout) {
        if (shutdown) {
            return null;
        }
        if (zooKeeper != null) {
            return zooKeeper;
        }
        LOGGER.info("Create ZooKeeper connection for " + this.zkServers);
        final CountDownLatch connected = new CountDownLatch(1);
        Watcher watcher = new ConnectWatcher(connected);
        boolean success = false;
        try {
            if (sessionState != null) {
                zooKeeper = new ZooKeeper(this.zkServers, this.sessiontTimeout, watcher, sessionState.sessionId, sessionState.sessionPasswd);
            } else {
                zooKeeper = new ZooKeeper(this.zkServers, this.sessiontTimeout, watcher);
            }
            if (this.zkUser != null && this.zkPassword != null) {
                zooKeeper.addAuthInfo("digest", (this.zkUser + ":" + this.zkPassword).getBytes("UTF-8"));
            }
            if (connectionTimeout > 0) {
                if (!connected.await(connectionTimeout, TimeUnit.MILLISECONDS)) {
                    LOGGER.warn("Can't create the connection to " + this.zkServers + " in " + connectionTimeout + " ms,close it.");
                    throw new RuntimeException("Timed out waiting for a ZK connection after "
                            + connectionTimeout);
                }
            } else {
                connected.await();
            }
            success = true;
        } catch (Throwable e) {
            throw new RuntimeException("Problem connecting to servers: " + this.zkServers, e);
        } finally {
            if (!success) {
                close();
            }
        }
        if (!success) {
            close();
            throw new RuntimeException("Problem connecting to servers: " + this.zkServers);
        }
        sessionState = new SessionState(zooKeeper.getSessionId(), zooKeeper.getSessionPasswd());
        closed = false;
        return zooKeeper;
    }

    /**
     * @return
     */
    public String getZkServers() {
        return zkServers;
    }

    /**
     * @return
     */
    public int getSessiontTimeout() {
        return sessiontTimeout;
    }

    /**
     * 关闭连接
     */
    public synchronized void close() {
        LOGGER.info("Close zooKeeper:{}", zooKeeper);
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warn("Interrupted trying to close zooKeeper");
            } finally {
                zooKeeper = null;
                sessionState = null;
            }
        }
        closed = true;
    }

    public synchronized void shutdown() {
        LOGGER.info("Shutdown ZkClient:" + this);
        this.shutdown = true;
        this.close();
    }

    public String getZkUser() {
        return zkUser;
    }

    public String getZkPassword() {
        return zkPassword;
    }

    /**
     * Zookeeper session的状态
     */
    private static final class SessionState {
        private final long sessionId;
        private final byte[] sessionPasswd;

        private SessionState(long sessionId, byte[] sessionPasswd) {
            this.sessionId = sessionId;
            this.sessionPasswd = sessionPasswd;
        }
    }

    private static class ExpirationWatcher implements Watcher {
        private final Runnable onExpired;

        public ExpirationWatcher(Runnable onExpired) {
            this.onExpired = onExpired;
        }

        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.None && event.getState() == Event.KeeperState.Expired) {
                try {
                    onExpired.run();
                } catch (Exception e) {
                    LOGGER.error("Trigger the exipration event [" + event + "] fail.", e);
                }
            }
        }

        @Override
        public String toString() {
            return "ExpirationWatcher{" +
                    "onExpired=" + onExpired +
                    '}';
        }
    }

    private class ConnectWatcher implements Watcher {
        private final CountDownLatch connected;

        public ConnectWatcher(CountDownLatch connected) {
            this.connected = connected;
        }

        @Override
        public void process(WatchedEvent event) {
            switch (event.getType()) {
                case None:
                    switch (event.getState()) {
                        case Expired:
                            LOGGER.info("Zookeeper session expired. Event: " + event);
                            close();
                            break;
                        case SyncConnected:
                            connected.countDown();
                            LOGGER.info("Zookeeper session created.Event:" + event);
                            break;
                        case Disconnected:
                            LOGGER.warn("Zookeeper session is disconnected.Event:" + event);
                            break;
                        default:
                            LOGGER.warn("UnProcessed Event:" + event);
                    }
            }
            synchronized (watchers) {
                for (Watcher watcher : watchers) {
                    try {
                        LOGGER.info("Process event:" + event + " watcher:" + watcher);
                        watcher.process(event);
                    } catch (Exception e) {
                        LOGGER.error("Watcher.process error. event:" + event, e);
                    }
                }
            }
        }
    }
}
