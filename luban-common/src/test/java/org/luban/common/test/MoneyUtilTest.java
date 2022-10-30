package org.luban.common.test;

import org.junit.Test;
import org.luban.common.money.MoneyUtil;

/**
 * @author kris
 * @date 2022/10/24
 */
public class MoneyUtilTest {

    @Test
    public void test() {
        double d = 111111.225;
        long cent = Math.round(d * 100);

        long ee = MoneyUtil.convertToLong("121.235", 2);
        System.err.println(ee);
    }

}
