package org.luban.bloomfilter.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author kris
 * @date 2022/7/15
 */
public class RedisClient {
    private Jedis jedis = null;
    private String ip = "localhost";
    private Integer port = 6379;

    public Jedis getPool() {
        return jedis;
    }

    /**
     * 构造函数初始化
     */
    public RedisClient() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        JedisPool pool = new JedisPool(ip, port);
        try (Jedis j = pool.getResource()) {
            jedis = j;
        }
    }

}
