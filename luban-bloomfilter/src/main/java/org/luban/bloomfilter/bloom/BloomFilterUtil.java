package org.luban.bloomfilter.bloom;

/**
 * BloomFilterUtil
 * https://hur.st/bloomfilter/ ，看了其他文档log函数是以e为底的, 该网址最后一个k是以10为低的, 存在gap, 整体相差不大可以参考使用
 **/
public class BloomFilterUtil {

    /**
     * 计算预估元素个数
     * @param p
     * @param m
     * @param k
     * @return
     */
    public static double n(double p, double m, double k) {
        return Math.ceil(m / (-k / Math.log(1 - Math.exp(Math.log(p) / k))));
    }

    /**
     * 计算误判率
     * @param n
     * @param m
     * @param k
     * @return
     */
    public static double p(double n, double m, double k) {
        return Math.pow(1 - Math.exp(-k / (m / n)), k);
    }

    /**
     * 计算需要使用的bitMap位数
     * @param n
     * @param p
     * @return
     */
    public static double m(double n, double p) {
        return Math.ceil((n * Math.log(p)) / Math.log(1.0D / Math.pow(2.0D, Math.log(2.0D))));
    }

    /**
     * 计算hash需要使用的个数
     * @param n
     * @param m
     * @return
     */
    public static double k(double n, double m) {
        return Math.round((m / n) * Math.log(2.0D));
    }


    public static void main(String[] args) {
        System.out.println(n(0.001, 100000000, 5));//5785364.0
        System.out.println(p(5785364.0D, 100000000.0D, 5));//0.001
        System.out.println(m(5785364.0D, 0.001));
        System.out.println(k(5785364.0D, 100000000.0D));
    }

}
