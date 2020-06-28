package org.luban.common.sort;

import java.util.BitSet;
import java.util.Random;

/**
 * User:krisjin
 * Date:2019/2/23
 *  
 */
public class Test {

    public static void main(String[] args) {

        int[] aa = init(100);

        System.out.println(aa);
        String str = sortNums(aa);
        System.err.println(str);
    }


    // 初始化一千万整数
    private static int[] init(int bound) {
        long start = System.currentTimeMillis();
        Random random = new Random();
        random.nextInt(bound);


        int[] nums = new int[bound];
        for (int i = 1; i < bound; i++) {
            nums[i] = random.nextInt(bound);
        }
        System.out.println("生成数据完成,耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return nums;
    }


    // 使用BitSet进行排序
    private static String sortNums(int[] nums) {
        long start = System.currentTimeMillis();
        System.out.println("开始排序");
        int len = nums.length;
        StringBuilder sb = new StringBuilder();
        BitSet bitSet = new BitSet(len);

        bitSet.set(0, len, false);
        for (int i = 0; i < len; i++) {
            bitSet.set(nums[i], true);
        }
        for (int i = 0; i < len; i++) {
            if (bitSet.get(i)) {
                sb.append(i).append(",");
            }
        }
        System.out.println("排序完成,耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return sb.toString();

    }
}
