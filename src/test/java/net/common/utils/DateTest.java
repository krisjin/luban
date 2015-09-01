package net.common.utils;

import net.common.utils.date.DateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/10
 * Time: 14:55
 */
public class DateTest {

    @Test
    public void test1() {

        Date date = DateUtil.parseDate("20150810145612", "yyyyMMddHHmmss");

        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String currentDate = DateUtil.getDateTimeStr();
        System.out.println(date);
        System.out.println(dateStr);
        System.out.println(currentDate);
    }
}
