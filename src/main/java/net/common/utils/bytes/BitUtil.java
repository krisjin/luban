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

    public static void main(String[] args) {
        boolean ret = isOdd(8);
        System.out.println(ret);
    }


}
