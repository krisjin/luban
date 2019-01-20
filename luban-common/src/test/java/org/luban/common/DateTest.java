package org.luban.common;

import org.luban.common.date.DateUtil;
import org.junit.Test;
import org.luban.common.date.DateUtil;

import java.util.Calendar;
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
        String currentDate = DateUtil.getCurrentDateTimeStr();
        System.out.println(date);
        System.out.println(dateStr);
        System.out.println(currentDate);
        Date d = DateUtil.getPreMonth();

        System.out.println(DateUtil.formatDate(d, DateUtil.DATETIME_STR));

        Date date1 = DateUtil.getDate(-15);
        System.out.println(date1);
        Date date2 = DateUtil.getCurrentDayEndTime();
        System.out.println(date2);

        System.out.println(DateUtil.formatBeforeDate(new Date()));

        Date date3 = DateUtil.getDate(0);
        System.out.println(date3);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -4);
        System.out.println(c.getTime());


        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DATE, -3);
        Date d3 = new Date();
        boolean isBefore = c.getTime().before(c3.getTime());
        System.out.println(isBefore);
        boolean b1 = false, b2 = false, b3 = true;

        if (b1 || b2 || b3) {
            System.out.println("ddd");
        }


        String ddd = "2016-1";
        System.out.println(DateUtil.getMaxDayOfYearMonth(ddd));

        String season = "2016.1";
        System.out.println(DateUtil.getMaxDayOfSeason(season));

    }
}
