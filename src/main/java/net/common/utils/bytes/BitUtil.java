package net.common.utils.bytes;

/**
 * 位操作工具类
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/18
 * Time: 17:15
 */
public class BitUtil {


    /**
     * 是否偶数,为0就是偶数
     *
     * @param i
     * @return
     */
    public static boolean isEven(int i) {
        if (Integer.valueOf(i) == null) return false;
        if ((i & 1) == 0) {
            return true;
        } else return false;
    }

    /**
     * 是否为奇数
     *
     * @param i
     * @return
     */
    public static boolean isOdd(int i) {
        if (Integer.valueOf(i) == null) return false;
        if ((i & 1) == 1) {
            return true;
        } else return false;
    }

    /**
     * @param i
     * @param j
     */
    public static void swap(Integer i, Integer j) {
        System.out.println("i=" + i + ", j=" + j);
        if (i != j) {
            i ^= j;
            j ^= i;
            i ^= j;
        }
        System.out.println("i=" + i + ", j=" + j);

    }

    public static void main(String[] args) {
        boolean ret = isOdd(8);
        System.out.println(ret);
        int i = 2, j = 3;
        swap(i, j);

        String a = Integer.toBinaryString(i);
        String b = Integer.toBinaryString(j);
        System.out.println(a);
        System.out.println(b);
        System.out.println(i ^ j);
    }


}
