package org.luban.bloomfilter.bloom;


import redis.clients.jedis.Jedis;

/**
 * 布隆过滤器
 **/
public class BloomFilter {

    /**
     * key的分区，防止热key大key 1024
     */
    private int partition;

    /**
     * 位运算使用的-1 ，用于替换%运算 1023
     */
    private int partitionBit;

    /**
     * 位运算使用右移 ，用于替换/运算 10
     */
    private int partionRight;

    /**
     * hash函数
     */
    private HashFunction[] allHashFunction;

    /**
     * 布隆key
     */
    private String key;

    /**
     * 哈希函数个数
     */
    private int hashFunctionNum;

    /**
     * bit map的长度 分区后的
     */
    private long bitMapLength;

    private Jedis jedis;

    /**
     * @param jedis           jimdb客户端
     * @param key             布隆key
     * @param hashFunctionNum hash函数个数
     * @param bitMapLength    bitMap长度, 最好是2的n次幂
     */
    public BloomFilter(Jedis jedis, String key, int hashFunctionNum, long bitMapLength) {
        this(jedis, key, hashFunctionNum, bitMapLength, 10);
    }

    /**
     * @param jedis           jimdb客户端
     * @param key             布隆key
     * @param hashFunctionNum hash函数个数
     * @param bitMapLength    bitMap长度, 最好是2的n次幂
     */
    public BloomFilter(Jedis jedis, String key, int hashFunctionNum, long bitMapLength, int partionRight) {
        if (hashFunctionNum > HashFunction.SEED_ARR.length) {
            throw new RuntimeException("hashFunctionNum can not more than " + HashFunction.SEED_ARR.length + ", but " + hashFunctionNum);
        }
        if (hashFunctionNum <= 0 || bitMapLength <= 0L) {
            throw new RuntimeException("hashFunctionNum or bitMapLength can not less than 0.");
        }
        if (key == null && key == "") {
            throw new RuntimeException("key can not be blank.");
        }
        if (jedis == null) {
            throw new RuntimeException("jimDbClient can not be null.");
        }

        this.jedis = jedis;
        this.key = key;
        this.hashFunctionNum = hashFunctionNum;
        this.bitMapLength = bitMapLength;
        this.partionRight = partionRight;
        this.partition = (1 << partionRight);
        this.partitionBit = (1 << partionRight) - 1;
        initHashFunction(hashFunctionNum);
    }


    /**
     * 插入元素到布隆过滤器
     *
     * @param element
     */
    public void put(String element) throws RuntimeException {
        try {
            long[] hashesResult = hashesResult(element);
            for (int i = 0; i < hashesResult.length; i++) {
                String key = buildKey(i, hashesResult[i]);
                jedis.setbit(key, hashesResult[i] >>> partionRight, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 检查元素是否可能存在
     *
     * @param element
     */
    public boolean exist(String element) throws RuntimeException {
        return existUseMaster(element, false);
    }

    /**
     * 检查元素是否可能存在 使用主集群
     *
     * @param element
     */
    public boolean existUseMaster(String element, boolean userMaster) throws RuntimeException {
        try {
            long[] hashesResult = hashesResult(element);
            for (int i = 0; i < hashesResult.length; i++) {
                String key = buildKey(i, hashesResult[i]);
                Boolean isExist = jedis.getbit(key, hashesResult[i] >>> partionRight);
                if (!isExist) {
                    return false;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return true;
    }

    /**
     * 销毁bloom过滤器内容释放空间
     */
    public void destory() {
        for (int i = 0; i < allHashFunction.length; i++) {
            for (int j = 0; j < partition; j++) {
                String partitionKey = buildKey(i, j);
                jedis.del(partitionKey);
            }
        }
    }

    /**
     * 初始化哈希函数
     *
     * @param hashFunctionNum hash函数个数
     */
    private void initHashFunction(int hashFunctionNum) {
        allHashFunction = new HashFunction[hashFunctionNum];
        for (int i = 0; i < hashFunctionNum; i++) {
            // 把一整个bit拆分成hash个数
            allHashFunction[i] = new HashFunction(bitMapLength / hashFunctionNum, HashFunction.SEED_ARR[i]);
        }
    }

    /**
     * 构建缓存key, 通过哈希函数以及哈希值进行分key处理，防止热key和大key
     *
     * @param index
     * @param hash
     * @return
     */
    private String buildKey(int index, long hash) {
        return key + ":" + index + ":" + (partitionBit & hash) + ":";
    }

    /**
     * 获取元素的hash地址
     *
     * @param element
     * @return
     */
    private long[] hashesResult(String element) {
        long[] result = new long[allHashFunction.length];
        for (int i = 0; i < allHashFunction.length; i++) {
            result[i] = allHashFunction[i].hash(element);
        }
        return result;
    }

}
