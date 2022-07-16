package org.luban.bloomfilter;

import org.luban.bloomfilter.bloom.BloomFilter;
import org.luban.bloomfilter.redis.RedisClient;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author kris on 2022/7/16
 */
public class BloomFilterTest {

    public static void main(String[] args) {
        Jedis jedis = new RedisClient().getPool();
        BloomFilter bloomFilter = new BloomFilter(jedis, "PageUrl", 5, 10000);
        String s = UUID.randomUUID().toString();
        bloomFilter.put(s);
        bloomFilter.put("aaa");


        System.out.println(bloomFilter.exist("aaa"));
    }

}
