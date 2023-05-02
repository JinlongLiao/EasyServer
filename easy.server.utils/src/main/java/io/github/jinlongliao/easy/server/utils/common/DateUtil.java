package io.github.jinlongliao.easy.server.utils.common;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


/**
 * 日期Util
 *
 * @author: liaojinlong
 * @date: 2022-08-24 15:11
 */
public class DateUtil {
    private static long oldTs = 1671465599000L;
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("CTT");
    public static final ZoneId ZONE_ID = TIME_ZONE.toZoneId();
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+08:00");

    /**
     * yyyy-MM-dd
     */
    public static final String STRING_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String STRING_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(STRING_DATE_TIME_FORMAT, Locale.CHINA);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(STRING_DATE_FORMAT, Locale.CHINA);


    private DateUtil() {
    }


    /**
     * 格式2010-08-03
     *
     * @return
     */
    public static String getStringDate() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    /**
     * 格式2010-08-03
     *
     * @param date
     * @return
     */
    public static String getStringDate(Date date) {
        if (Objects.isNull(date)) {
            return LocalDate.now().format(DATE_FORMAT);
        }
        return date.toInstant().atZone(ZONE_ID).toLocalDate().format(DATE_FORMAT);
    }


    /**
     * 格式2010-08-03 14:10:04
     *
     * @return
     */
    public static String getStringDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMAT);
    }

    /**
     * 格式2010-08-03 14:10:04
     *
     * @param date
     * @return
     */
    public static String getStringDateTime(Date date) {
        if (Objects.isNull(date)) {
            return LocalDateTime.now().format(DATE_TIME_FORMAT);
        }
        return date.toInstant().atZone(ZONE_ID).toLocalDateTime().format(DATE_FORMAT);
    }


    /**
     * 获取下一日开始时间
     *
     * @param date /
     * @return /
     */
    public static Date getNextDayStartDate(Date date) {
        return getNextDaysByStartDate(date, 1);
    }

    /**
     * 几天后的时间
     *
     * @param date /
     * @param day  /
     * @return /
     */
    public static Date getNextDaysByStartDate(Date date, int day) {
        LocalDate start;
        if (Objects.isNull(date)) {
            start = LocalDate.now();
        } else {
            start = date.toInstant().atZone(ZONE_ID).toLocalDate();
        }
        return Date.from(start.plusDays(day).atStartOfDay(ZONE_ID).toInstant());
    }

    public static Date getNextDays(Date date, int day) {
        LocalDateTime start;
        if (Objects.isNull(date)) {
            start = LocalDateTime.now();
        } else {
            start = date.toInstant().atZone(ZONE_ID).toLocalDateTime();
        }
        return Date.from(start.plusDays(day).toInstant(ZONE_OFFSET));
    }


    /**
     * 获取两个相隔天数
     *
     * @param endDate /
     * @return /
     */
    public static int getTodayApartDate(Date endDate) {
        LocalDate now = LocalDate.now();
        LocalDate end = endDate.toInstant().atZone(ZONE_ID).toLocalDate();
        return (int) (now.toEpochDay() - end.toEpochDay());
    }

    /**
     * 当前日期＋分钟
     *
     * @param minute /
     * @return /
     */
    public static Date addMinAtToday(int minute) {
        LocalDateTime now = LocalDateTime.now();
        now = now.withMinute(minute);
        return Date.from(now.toInstant(ZONE_OFFSET));
    }

    /**
     * 获取两个相隔天数
     *
     * @param ts /
     * @return /
     */
    public static int getTodayApartDate(long ts) {
        LocalDate now = LocalDate.now();
        LocalDate end = Instant.ofEpochMilli(ts).atZone(ZONE_ID).toLocalDate();
        return (int) (now.toEpochDay() - end.toEpochDay());
    }

    /**
     * 获取两个相隔天数
     *
     * @param startDate /
     * @param endDate   /
     * @return /
     */
    public static int getApartDate(Date startDate, Date endDate) {
        if (startDate == null) {
            throw new NullPointerException("startDate is null");
        }
        if (endDate == null) {
            throw new NullPointerException("endDate is null");
        }
        LocalDate start = startDate.toInstant().atZone(ZONE_ID).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZONE_ID).toLocalDate();
        return (int) (end.toEpochDay() - start.toEpochDay());
    }

    /**
     * 获取两个相隔秒
     *
     * @param startDate /
     * @param endDate   /
     * @return /
     */
    public static int getApartSecond(Date startDate, Date endDate) {
        long n1 = startDate.getTime();
        long n2 = endDate.getTime();
        long diff = Math.abs(n2 - n1);

        diff /= 1000;
        return (int) diff;
    }


    /**
     * 获取某天往前或者往后天数后的日期
     *
     * @param date
     * @param dayNum
     * @return /
     */
    public static Date addDay(Date date, int dayNum) {
        return getNextDays(date, dayNum);
    }


    /**
     * 比较两个日期是否是同一天
     *
     * @param date1 /
     * @param date2 /
     * @return /
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.toInstant().atZone(ZONE_ID).toLocalDate().equals(date2.toInstant().atZone(ZONE_ID).toLocalDate());
    }


    public static Date getTimesWeekMonday() {
        LocalDate now = LocalDate.now(ZONE_ID);
        return Date.from(now.minusDays(now.getDayOfWeek().ordinal()).atStartOfDay(ZONE_ID).toInstant());
    }

    public static boolean isToDay(Date date) {
        if (Objects.isNull(date)) {
            return false;
        }
        return date.toInstant().atZone(ZONE_ID).toLocalDate().equals(LocalDate.now());
    }

    public static boolean isToDay(long ts) {
        if (ts < oldTs) {
            return false;
        }
        boolean equals = Instant.ofEpochMilli(ts).atZone(ZONE_ID).toLocalDate().equals(LocalDate.now());
        if (!equals) {
            oldTs = Math.max(ts, oldTs);
        }
        return equals;
    }

}
