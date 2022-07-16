package org.luban.common.date;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * 日期处理工具
 * User:krisjin
 * Date:2019-01-03
 */
public final class DateTimeUtil {
    private static final Logger log = LoggerFactory.getLogger(DateTimeUtil.class);

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    public static final String DATE_STR = "yyyy-MM-dd";
    public static final String DATE_STR1 = "yyyyMMdd";
    public static final String DATE_STR_2 = "yyyy/MM/dd";
    public static final String DATE_STR_3 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String TIME_STR = "HH:mm:ss";
    public static final String DATETIME_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_STAMP = "yyyy-MM-dd HH:mm";
    public static final String DATEHOUR_STR = "yyyy-MM-dd HH";

    public static final int RATE_MILLIS_TO_SECONDS = 1000;
    public static final String ONE_SEASON_LAST_MONTH = "03";
    public static final String TWO_SEASON_LAST_MONTH = "06";
    public static final String THREE_SEASON_LAST_MONTH = "09";
    public static final String FOUR_SEASON_LAST_MONTH = "12";
    /**
     * 每年第一周的最小天数
     */
    private static final int MINIMAL_DAYS_IN_FIRST_WEEK = 7;

    /**
     * 毫秒转换成秒的比例
     */
    private static final long MILLIS_TO_SECOND_RATE = 1000;

    /**
     * 一天的毫秒数
     */
    public static final long DAY_IN_MILLIS = 1000L * 60 * 60 * 24;

    protected final Calendar calendar;

    public static String getLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        String dateStr = sdf.format(calendar.getTime());
        return dateStr;
    }


    public static String setMinutes(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.MINUTE, n);
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_STR);
        String dateStr = sdf.format(calendar.getTime());
        return dateStr;
    }


    private DateTimeUtil(final Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("calendar can not be null");
        }
        this.calendar = calendar;
    }

    /**
     * 获取当前日期
     * 格式: <code>yyyy-MM-dd</code>
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
    }

    /**
     * 获取当前日期和时间
     * 格式: <code>yyyy-MM-dd HH:mm:ss</code>
     *
     * @return
     */
    public static String getCurrentDateTimeStr() {
        return DateFormatUtils.format(new Date(), DATETIME_STR);
    }

    /**
     * 获取指定日期
     * 说明: n=0为当天, 1为明天, -1为昨天
     */
    public static Date getDate(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, n);
        Date date = calendar.getTime();
        String dateStr = formatDate(date);
        SimpleDateFormat format = new SimpleDateFormat(DATE_STR);
        Date retDate = null;
        try {
            retDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retDate;
    }

    /**
     * 格式化当前日期
     * 格式: <code>yyyy-MM-dd</code>
     *
     * @return
     */
    public static String formatDate(Date date) {
        return DateFormatUtils.ISO_DATE_FORMAT.format(date);
    }


    /**
     * 格式化当前日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 解析日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        try {
            return DateUtils.parseDate(dateStr, new String[]{pattern});
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期操作
     * 说明: n=0为当前日, 1为后一天, -1为前一天
     *
     * @param n
     * @return
     */
    public static final Date getDate(Date date, int n) {
        return DateUtils.addDays(date, n);
    }

    /**
     * 获取日期的小时位
     *
     * @param date
     * @return
     */
    public static final int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取每月最后一天
     *
     * @return
     */
    public static final Date getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();

        int lastDayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDayNum);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 指定的格式为yyyy-MM 2016-11
     *
     * @param date
     * @return
     */
    public static String getMaxDayOfMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            int lastDayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            date = date + "-" + lastDayNum;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param season 2016.1  格式为年份.季度
     * @return
     */
    public static String getMaxDayOfSeason(String season) {
        String[] seasons = season.split("\\.");
        int year = Integer.valueOf(seasons[0]);
        int s = Integer.valueOf(seasons[1]);
        String month;
        switch (s) {
            case 1:
                month = ONE_SEASON_LAST_MONTH;
                break;
            case 2:
                month = TWO_SEASON_LAST_MONTH;
                break;
            case 3:
                month = THREE_SEASON_LAST_MONTH;
                break;
            case 4:
                month = FOUR_SEASON_LAST_MONTH;
                break;
            default:
                month = ONE_SEASON_LAST_MONTH;
                break;
        }
        String date = year + "-" + month;
        return getMaxDayOfMonth(date);
    }

    /**
     * 获取下一个月的月份
     *
     * @return
     */
    public static final int getNextMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取前一个月的日期
     *
     * @return
     */
    public static Date getPreMonth() {
        DateTime dateTime = new DateTime();
        DateTime d = dateTime.minusMonths(1);
        Date date = d.toDate();
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_STR);
        String s = format.format(date);
        Date retDate = null;
        try {
            retDate = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retDate;
    }

    /**
     * 检查日期参数,默认日期一个月
     *
     * @param begin
     * @param end
     * @return 返回
     */
    public static final String[] checkDate(String begin, String end) {
        Date beginDate;
        Date endDate;
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.DATE_STR);
        if (!Strings.isNullOrEmpty(begin) && !Strings.isNullOrEmpty(end)) {
            try {
                beginDate = sdf.parse(begin);
                endDate = sdf.parse(end);
            } catch (Exception e) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }
            // 顺序颠倒，置换
            if (beginDate.after(endDate)) {
                Date temp = beginDate;
                beginDate = endDate;
                endDate = temp;
            }
            // 结束时间最迟为今天
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            if (endDate.after(today)) {
                endDate = today;
            }
            // 起始最迟只能取今天，最早只能取一个月前
            if (beginDate.after(today)) {
                calendar.add(Calendar.MONTH, -1);
                beginDate = calendar.getTime();
            }
        } else {
            // 以昨天起始的最近一个月
            Calendar calendar = Calendar.getInstance();
            endDate = calendar.getTime();
            calendar.add(Calendar.MONTH, -1);
            beginDate = calendar.getTime();
        }
        return new String[]{sdf.format(beginDate), sdf.format(endDate)};
    }

    /**
     * 获取当前日期到本月第一天的天数
     */
    public static int getDayOfLastMonth() {
        Calendar nowCalendar = Calendar.getInstance();
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.set(Calendar.DATE, 1);//设为当前月的1号
        return nowCalendar.get(Calendar.DAY_OF_MONTH) - firstCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * <code>statDate</code>天之前<code>days</code>00:00:00到<code>statDate</code>23:59:59秒为单位的时间段
     *
     * @param statDate
     * @param days
     * @return
     */
    public static MilTimeRange getDateInSecRange(Date statDate, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(statDate);
        c.add(Calendar.DATE, 1 - days);
        // 计算实际需要统计的日期
        Date actualStatDate = new Date(c.getTimeInMillis());

        c.setTime(actualStatDate);
        // 计算统计日期当天起始秒数，当天0点
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        final long statDateStartSec = (long) (c.getTimeInMillis() / RATE_MILLIS_TO_SECONDS);

        // 计算统计日期当天结束秒数，第二天0点减去1秒
        c.add(Calendar.DATE, 1);
        final long statDateEndSec = (long) (c.getTimeInMillis() / RATE_MILLIS_TO_SECONDS) - 1;

        // 从统计日期开始days天的最后秒数
        final long lastDaysSec = statDateEndSec + (long) (TimeUnit.DAYS.toSeconds(days - 1));

        MilTimeRange range = new MilTimeRange(statDateStartSec, lastDaysSec);

        return range;
    }


    public static MilTimeRange getDateInSecRange(Date startDate, Date endDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
//        c.add(Calendar.DATE, 1 - days);
        // 计算实际需要统计的日期
        Date actualStatDate = new Date(c.getTimeInMillis());

        c.setTime(actualStatDate);
        // 计算统计日期当天起始秒数，当天0点
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        final long statDateStartSec = (long) (c.getTimeInMillis() / 1000);


        // 计算统计日期当天结束秒数，第二天0点减去1秒
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        final long statDateEndSec = (long) (c.getTimeInMillis() / 1000) - 1;

        MilTimeRange range = new MilTimeRange(statDateStartSec, statDateEndSec);

        return range;
    }

    /**
     * 天转换成秒为单位的时间段 00:00:00 - 23:59:59
     *
     * @param statDate
     * @return
     */
    public static MilTimeRange getDateInSecRange(Date statDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(statDate);
        // 计算开始时间，当天0点
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long startTime = (long) (c.getTimeInMillis() / RATE_MILLIS_TO_SECONDS);
//        long startTime = c.getTimeInMillis();

        // 计算结束时间，第二天0点减去1秒
        c.add(Calendar.DATE, 1);
        long endTime = (long) (c.getTimeInMillis() / RATE_MILLIS_TO_SECONDS) - 1;
//        long endTime = c.getTimeInMillis() - 1;

        return new MilTimeRange(startTime, endTime);
    }


    /**
     * 获取特殊格式的时间间隔
     *
     * @param date
     * @return
     */
    public static String[] getTimeRangeFormat(Date date, String pattern) {
        String[] timeRange = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 计算开始时间，当天0点
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeRange[0] = sdf.format(c.getTime());
        // 计算结束时间，第二天0点
        c.add(Calendar.DATE, 1);
        timeRange[1] = sdf.format(c.getTime());
        return timeRange;
    }

    public static Date getCurrentDayEndTime() {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(DATETIME_STR);
        SimpleDateFormat format = new SimpleDateFormat(DATE_STR);
        Date date1 = getDate(0);
        String s = format.format(date1);
        s = s + " 23:59:59";
        Date d = null;
        try {
            d = formatDateTime.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static class MilTimeRange {
        long startTime;
        long endTime;

        MilTimeRange(long startTime, long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        /**
         * 对于给定的年和周信息进行转换
         * 例：2012-01-01属于2011年的第52周
         *
         * @param year 年
         * @param week 周次
         * @return [年， 周次]
         */
        public static int[] transformYearWeek(int year, int week) {
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setMinimalDaysInFirstWeek(MINIMAL_DAYS_IN_FIRST_WEEK);
            c.set(Calendar.YEAR, year);
            c.set(Calendar.WEEK_OF_YEAR, week);
            return getYearWeek(c.getTimeInMillis());
        }

        /**
         * 获取指定日期属于哪一年的哪一周
         * 例：2012-01-01属于2011年的第52周
         *
         * @param millis 毫秒数
         * @return [年， 周次]
         */
        public static int[] getYearWeek(long millis) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setMinimalDaysInFirstWeek(MINIMAL_DAYS_IN_FIRST_WEEK);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return new int[]{
                    c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR)
            };
        }

        /**
         * 获取指定年指定周次的首末日
         *
         * @param year 年
         * @param week 周次，以周一为起始
         * @return [首日，末日]
         */
        public static Date[] getWeekStartAndEndDate(int year, int week) {
            Preconditions.checkArgument(week > 0, "illegal week %s", week);
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.setMinimalDaysInFirstWeek(MINIMAL_DAYS_IN_FIRST_WEEK);
            c.set(Calendar.YEAR, year);
            c.set(Calendar.WEEK_OF_YEAR, week);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date start = new Date(c.getTimeInMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date end = new Date(c.getTimeInMillis());
            return new Date[]{start, end};
        }

        /**
         * 获取指定天的最大秒数
         *
         * @param date 指定日期
         * @return
         */
        public static int getLastSecondOfDay(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.add(Calendar.DATE, 1);
            int startSecond = (int) (c.getTimeInMillis() / MILLIS_TO_SECOND_RATE);
            return startSecond - 1;
        }

        /**
         * 获取指定天的最小秒数
         *
         * @param date 指定日期
         * @return
         */
        public static int getFirstSecondOfDay(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return (int) (c.getTimeInMillis() / MILLIS_TO_SECOND_RATE);
        }

        /**
         * 获取指定天的最小毫秒数
         *
         * @param date 指定日期
         * @return
         */
        public static long getFirstMillisOfDay(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTimeInMillis();
        }

        /**
         * 获取指定天的最大毫秒数
         *
         * @param date 指定日期
         * @return
         */
        public static long getLastMillisOfDay(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.add(Calendar.DATE, 1);
            return c.getTimeInMillis() - 1;
        }

        /**
         * 增加天数
         *
         * @param date 原始日期
         * @param day  增加天数
         * @return
         */
        public static Date addDay(Date date, int day) {
            return new Date(date.getTime() + day * DAY_IN_MILLIS);
        }

        /**
         * 获取指定年指定月份的首末日
         *
         * @param year
         * @param month
         * @return
         */
        public static Date[] getMonthStartAndEndDate(int year, int month) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            Date start = new Date(c.getTimeInMillis());

            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date end = new Date(c.getTimeInMillis());
            return new Date[]{start, end};
        }

        /**
         * XMLCalender转Date
         *
         * @param cal
         * @return
         * @throws Exception
         */
        public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
            GregorianCalendar ca = cal.toGregorianCalendar();
            return ca.getTime();
        }

        /**
         * Date转XMLCalender转Date
         *
         * @param date
         * @return
         */
        public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar gc = null;
            try {
                gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            } catch (Exception e) {
                log.info("convertToXMLGregorianCalendar fail", e);
            }
            return gc;
        }

    }


    public static String formatBeforeDate(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 60L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }


    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }



}
