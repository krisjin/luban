package org.luban.common.test;

import org.junit.Test;
import org.luban.common.date.DateTimeUtil;

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

        Date date = DateTimeUtil.parseDate("20150810145612", "yyyyMMddHHmmss");

        String dateStr = DateTimeUtil.formatDate(date, "yyyy-MM-dd");
        String currentDate = DateTimeUtil.getCurrentDateTimeStr();
        System.out.println(date);
        System.out.println(dateStr);
        System.out.println(currentDate);
        Date d = DateTimeUtil.getPreMonth();

        System.out.println(DateTimeUtil.formatDate(d, DateTimeUtil.DATETIME_STR));

        Date date1 = DateTimeUtil.getDate(-15);
        System.out.println(date1);
        Date date2 = DateTimeUtil.getCurrentDayEndTime();
        System.out.println(date2);

        System.out.println(DateTimeUtil.formatBeforeDate(new Date()));

        Date date3 = DateTimeUtil.getDate(0);
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
        System.out.println(DateTimeUtil.getMaxDayOfYearMonth(ddd));

        String season = "2016.1";
        System.out.println(DateTimeUtil.getMaxDayOfSeason(season));

    }
}
