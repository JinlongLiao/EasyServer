package io.github.jinlongliao.easy.server.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 日期相关，需要依赖外部的定时任务 来触发时间更新
 *
 * @author: liaojinlong
 * @date: 2022-04-28 20:51
 */

public class DateHelper {
    private static final Logger log = LoggerFactory.getLogger(DateHelper.class);
    public static final TimeZone TIME_ZONE = DateUtil.TIME_ZONE;
    public static final ZoneId ZONE_ID = DateUtil.ZONE_ID;
    public static final ZoneOffset ZONE_OFFSET = DateUtil.ZONE_OFFSET;
    /**
     * 进入时间戳开始时间
     */
    private long toDayStartTs;

    /**
     * 下个小时的时间戳
     */
    private long nextHourTs;
    /**
     * 下一天的时间戳
     */
    private long nextDayTs;
    /**
     * 下一周的时间戳
     */
    private long nextWeekTs;
    /**
     * 本周周一时间戳
     */
    private long thisWeekMoTs;
    /**
     * 本周周日时间戳
     */
    private long thisWeekSuTs;

    private String dateYmdFormat;

    public DateHelper() {
        this.refresh();
    }

    /**
     * 需要在整点被执行
     */
    public void refresh() {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.plusSeconds(10);
        this.dateYmdFormat = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        dateTime = dateTime.minus(dateTime.get(ChronoField.MICRO_OF_SECOND), ChronoUnit.MICROS);
        dateTime = dateTime.minus(dateTime.get(ChronoField.NANO_OF_SECOND), ChronoUnit.NANOS);
        dateTime = dateTime.minus(dateTime.getMinute(), ChronoUnit.MINUTES);
        dateTime = dateTime.minus(dateTime.getSecond(), ChronoUnit.SECONDS);
        dateTime = dateTime.plusHours(1);
        Instant instant = dateTime.toInstant(ZONE_OFFSET);
        this.nextHourTs = instant.toEpochMilli();
        dateTime = dateTime.minusHours(dateTime.getHour());
        this.toDayStartTs = dateTime.toInstant(ZONE_OFFSET).toEpochMilli();
        dateTime = dateTime.plusDays(1);
        this.nextDayTs = dateTime.toInstant(ZONE_OFFSET).toEpochMilli();

        if (dateTime.getDayOfWeek() == DayOfWeek.MONDAY) {
            dateTime = dateTime.minusDays(1);
        }
        dateTime = dateTime.minusDays(dateTime.getDayOfWeek().ordinal());

        this.thisWeekMoTs = dateTime.toInstant(ZONE_OFFSET).toEpochMilli();
        dateTime = dateTime.plusWeeks(1);
        this.nextWeekTs = dateTime.toInstant(ZONE_OFFSET).toEpochMilli();
        dateTime = dateTime.minusDays(1);
        this.thisWeekSuTs = dateTime.toInstant(ZONE_OFFSET).toEpochMilli();
        log.info("{}", this);
    }

    /**
     * 到下星期一剩余
     *
     * @return /
     */
    public long getNextMondayTtl() {
        return ((this.getNextWeekTs() - System.currentTimeMillis()));
    }

    public long getNextDayTtlSecond() {
        return Math.min(TimeUnit.DAYS.toSeconds(1), Math.abs(this.getNextDayTs() - System.currentTimeMillis()) / 1000);
    }

    public boolean isThisWeekDay(DayOfWeek dayOfWeek, long ts) {
        LocalDateTime localDateTime = getLocalDateTimeFromTs(ts);
        return this.isThisWeekDay(dayOfWeek, localDateTime);
    }

    public boolean isInThisWeek(long ts) {
        return ts >= this.thisWeekMoTs && ts <= this.thisWeekSuTs;
    }

    public boolean isInThisWeek(LocalDateTime localDateTime) {
        return this.isInThisWeek(localDateTime.toInstant(ZONE_OFFSET).toEpochMilli());
    }

    public boolean isThisWeekDay(DayOfWeek dayOfWeek, LocalDateTime localDateTime) {
        long ts = localDateTime.toInstant(ZONE_OFFSET).toEpochMilli();
        return ts >= this.thisWeekMoTs && ts <= this.thisWeekSuTs && localDateTime.getDayOfWeek() == dayOfWeek;
    }

    public boolean isThisHour(int hour, long ts) {
        LocalDateTime localDateTime = getLocalDateTimeFromTs(ts);
        return this.isThisHour(hour, localDateTime);
    }

    public boolean isThisHour(int hour, LocalDateTime localDateTime) {
        return hour == localDateTime.getHour();
    }

    public boolean isThisWeekDayAndHour(DayOfWeek dayOfWeek, int hour, LocalDateTime localDateTime) {
        return this.isThisWeekDay(dayOfWeek, localDateTime) && this.isThisHour(hour, localDateTime);
    }

    public boolean isThisWeekDayAndHour(DayOfWeek dayOfWeek, int hour, long ts) {
        LocalDateTime localDateTime = getLocalDateTimeFromTs(ts);
        return this.isThisWeekDayAndHour(dayOfWeek, hour, localDateTime);
    }

    public LocalDateTime getLocalDateTimeFromTs(long ts) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZONE_ID);
    }

    public String getDateYmdFormat() {
        return dateYmdFormat;
    }

    public long getToDayStartTs() {
        return toDayStartTs;
    }

    public long getNextHourTs() {
        return nextHourTs;
    }

    public long getNextDayTs() {
        return nextDayTs;
    }

    public long getNextWeekTs() {
        return nextWeekTs;
    }

    public long getThisWeekMoTs() {
        return thisWeekMoTs;
    }

    public long getThisWeekSuTs() {
        return thisWeekSuTs;
    }

    /**
     * 到下星期一剩余秒数
     *
     * @return /
     */
    public int getNextMondayTtlSec() {
        return (int) ((this.getNextWeekTs() - System.currentTimeMillis()) / 1000);
    }

    public int getNextWeekTtlSec() {
        return (int) ((this.getNextWeekTs() - System.currentTimeMillis()) / 1000);
    }
}
