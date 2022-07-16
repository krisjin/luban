package org.luban.bloomfilter.bitmap;

import org.data.bloom.redis.RedisClient;
import redis.clients.jedis.Jedis;

/**
 * @author shijingui
 * @date 2022/7/15
 */
public class RedisBitmap {

    public static void main(String[] args) {

        String testName = "name";
        Jedis jedis = new RedisClient().getPool();
        jedis.set(testName, "kris");

        System.out.printf(jedis.get(testName));
    }

}
