package org.bscl.common.money;

import com.google.common.base.Strings;

import java.math.BigDecimal;

/**
 * 钱有关的工具类
 */
public final class MoneyUtil {

    private MoneyUtil() {
    }

    /**
     * 元和分转换的小数位数
     */
    public static final int PRECISION_CENT_YUAN = 2;

    /**
     * 精确到"元", 倍率, 一元=100分
     */
    public static final int SCALE_YUAN = 100;

    /**
     * 将long型的值按照precision指定的小数位数进行转换
     *
     * @param value     原始值
     * @param precision 小数位数
     * @return 转换后的值
     */
    public static String convertToDecimal(long value, int precision) {
        String valueStr = String.valueOf(value);

        boolean minus = value < 0;

        if (minus) {
            valueStr = valueStr.substring(1);
        }

        int padding = precision - valueStr.length() + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            builder.append('0');
        }
        builder.append(valueStr);
        builder.insert(builder.length() - precision, '.');

        if (minus) {
            builder.insert(0, '-');
        }

        return builder.toString();
    }

    /**
     * 根据小数位将数值放大相应倍数，转换成long型数值
     * 如果传入数值的实际小数位数大于precision指定的小数位数，则抛出异常
     *
     * @param decimal   含有小数的数值
     * @param precision 小数位数
     * @return 转换成的long型数值
     */
    public static long convertToLong(String decimal, int precision) {
        if (Strings.isNullOrEmpty(decimal)) {
            return 0L;
        }
        long time = 1L;
        for (int i = 0; i < precision; i++) {
            time = time * 10L;
        }
        BigDecimal bigDecimal = new BigDecimal(decimal);
        if (time != 1L) {
            bigDecimal = bigDecimal.multiply(new BigDecimal(time));
        }
        return bigDecimal.longValueExact();
    }


    /**
     * 金额单位转换
     *
     * @param price      实际金额
     * @param priceScale 金额与值转换比例
     * @return 转换后金额
     */
    public static long convPrice(long price, int priceScale) {
        return price * priceScale;
    }

    /**
     * 金额单位转换
     *
     * @param price      实际金额
     * @param priceScale 金额与值转换比例
     * @return 转换后金额
     */
    public static long convPrice(double price, int priceScale) {
        return (long) (price * priceScale);
    }

    /**
     * 将存储金额转换为单位元
     *
     * @param price 存储的金额
     * @return 金额, 单位元
     */
    public static double convPriceByYuan(long price) {
        return 1D * price / SCALE_YUAN;
    }
}
