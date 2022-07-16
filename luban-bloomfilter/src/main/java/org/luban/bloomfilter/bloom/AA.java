package org.luban.bloomfilter.bloom;

import org.data.bloom.redis.RedisClient;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author shijingui
 * @date 2022/7/15 5:39 下午
 */
public class AA {

    public static void main(String[] args) {

        Jedis jedis = new RedisClient().getPool();
        // 支持100W个元素
        BloomFilter bloomFilter = new BloomFilter(jedis, "yodaDeviceId", 5, 10000);
        String s = UUID.randomUUID().toString();
        bloomFilter.put(s);
        bloomFilter.put("aaa");


        System.out.println( bloomFilter.exist("aaa"));
//            Assert.assertFalse(bloomFilter.exist(s+"3"));

    }
}
