package org.luban.common.zookeeper;

import com.google.common.collect.Lists;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * zookeeper的工具类
 */
public final class ZkUtils {
    private ZkUtils() {

    }

    /**
     * 创建zk的acl,如果user和password都不为空,则构建只有user:password才有权限的ACL。
     * 否则返回{@link org.apache.zookeeper.ZooDefs.Ids#OPEN_ACL_UNSAFE},即所有的人都有权限
     *
     * @param user
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static List<ACL> createACL(String user, String password) throws NoSuchAlgorithmException {
        if (user != null && password != null) {
            String digest = DigestAuthenticationProvider.generateDigest(user + ":" + password);
            ACL all = new ACL(ZooDefs.Perms.ALL, new Id("digest", digest));
            return Lists.newArrayList(all);
        } else {
            return ZooDefs.Ids.OPEN_ACL_UNSAFE;
        }
    }
}
