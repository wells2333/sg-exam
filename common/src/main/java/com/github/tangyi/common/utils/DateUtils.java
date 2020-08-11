package com.github.tangyi.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author tangyi
 * @date 2019/4/28 16:03
 */
@Slf4j
public class DateUtils {

	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final DateTimeFormatter FORMATTER_DAY = DateTimeFormatter.ofPattern("MM-dd");

	public static final DateTimeFormatter FORMATTER_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS");

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String localDateToString(LocalDateTime date) {
        return date != null ? date.format(FORMATTER) : "";
    }

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String localDateMillisToString(LocalDateTime date) {
        return date != null ? date.format(FORMATTER_MILLIS) : "";
    }

    /**
     * LocalDate转Date
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime localDateTime
     * @return Date
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date转LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 两个时间之差
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return 分钟
     */
    public static Integer getBetweenMinutes(Date startDate, Date endDate) {
        int minutes = 0;
        try {
            if (startDate != null && endDate != null) {
                long ss;
                if (startDate.before(endDate)) {
                    ss = endDate.getTime() - startDate.getTime();
                } else {
                    ss = startDate.getTime() - endDate.getTime();
                }
                minutes = (int) (ss / (60 * 1000));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return minutes;
    }

    /**
     * 两个时间只差
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return 秒数
     */
    public static Integer getBetweenSecond(Date startDate, Date endDate) {
        int seconds = 0;
        try {
            if (startDate != null && endDate != null) {
                long ss;
                if (startDate.before(endDate)) {
                    ss = endDate.getTime() - startDate.getTime();
                } else {
                    ss = startDate.getTime() - endDate.getTime();
                }
                seconds = (int) (ss / (1000));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return seconds;
    }

	/**
	 * 获得周几日期，上一周或下一周，依此类推
	 * @param week 指定周几
	 * @param whichWeek 那一周
	 * @return string 日期 年-月-日
	 */
	public static String getDayOfWhichWeek(DayOfWeek week, int whichWeek) {
		LocalDate day = LocalDate.now().with(TemporalAdjusters.previous(week)).minusWeeks(whichWeek -1);
		return day.format(FORMATTER_DAY);
	}

	/**
	 * 天数累加
	 * @param plusDay plusDay
	 * @return LocalDateTime
	 */
	public static LocalDateTime plusDay(int plusDay) {
		return LocalDateTime.now().plusDays(plusDay);
	}
}
