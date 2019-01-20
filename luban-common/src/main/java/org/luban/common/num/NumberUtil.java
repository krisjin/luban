package org.luban.common.num;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字工具类
 */
public final class NumberUtil {
    /**
     * 默认
     */
    private static final int DEFAULT_ROUND_MODE = BigDecimal.ROUND_HALF_UP;

    private NumberUtil() {
    }

    /**
     * 对数字进行小数位数处理
     *
     * @param origin 原始数字
     * @param scale  保留精度
     * @return
     */
    public static BigDecimal round(BigDecimal origin, int scale) {
        return round(origin, scale, DEFAULT_ROUND_MODE);
    }

    /**
     * 对数字进行小数位数处理
     *
     * @param origin    原始数字
     * @param scale     保留精度
     * @param roundMode 四舍五入方式
     * @return
     */
    public static BigDecimal round(BigDecimal origin, int scale, int roundMode) {
        if (origin == null) {
            return null;
        }
        return origin.setScale(scale, roundMode);
    }

    /**
     * 对数字进行小数位数处理
     *
     * @param origin    原始数字
     * @param scale     保留精度
     * @param roundMode 四舍五入方式
     * @return
     */
    public static String roundStr(String origin, int scale, int roundMode) {
        BigDecimal bigDecimal = round(new BigDecimal(origin), scale, roundMode);
        return bigDecimal.toPlainString();
    }

    /**
     * 对数字进行小数位数处理
     *
     * @param origin 原始数字
     * @param scale  保留精度
     * @return
     */
    public static String roundStr(String origin, int scale) {
        return roundStr(origin, scale, DEFAULT_ROUND_MODE);
    }


    public static double convertInt2Percent(int num) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String ret = decimalFormat.format(Double.valueOf("0." + num));
        return Double.valueOf(ret);
    }

}