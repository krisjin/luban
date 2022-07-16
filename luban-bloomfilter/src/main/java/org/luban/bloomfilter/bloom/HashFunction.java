package org.luban.bloomfilter.bloom;

/**
 * 哈希函数 (BKDRHash)
 **/
public class HashFunction {
    /**
     * 定义15个素数
     */
    public static final long[] SEED_ARR = {53L, 97L, 193L, 389L, 769L, 1543L, 3079L, 6151L, 12289L, 24593L, 49157L, 98317L, 196613L, 393241L, 786443L};
    /**
     * 二进制向量数组大小
     */
    private long size;
    /**
     * 随机数种子
     */
    private long seed;

    public HashFunction(long size, long seed) {
        this.size = size;
        this.seed = seed;
    }

    /**
     * 计算hash, 保证算出来的值在size内, 不用外层在绝对值取余
     *
     * @param value
     * @return
     */
    public long hash(String value) {
        long result = 0;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            result = seed * result + value.charAt(i);
        }
        return (size - 1) & result;
    }
}
