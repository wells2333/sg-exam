/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DateUtils {

	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter FORMATTER_DAY = DateTimeFormatter.ofPattern("MM-dd");
	public static final DateTimeFormatter FORMATTER_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS");

	private DateUtils() {
	}

	/**
	 * 日期转 string
	 */
	public static String localDateToString(LocalDateTime date) {
		return date != null ? date.format(FORMATTER) : "";
	}

	/**
	 * 日期转 string
	 */
	public static String localDateMillisToString(LocalDateTime date) {
		return date != null ? date.format(FORMATTER_MILLIS) : "";
	}

	/**
	 * LocalDate 转 Date
	 */
	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * LocalDateTime 转 Date
	 */
	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Date 转 LocalDate
	 */
	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Date 转 LocalDateTime
	 */
	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * 两个时间之差
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
	 * @return string 日期 年 - 月-日
	 */
	public static String getDayOfWhichWeek(DayOfWeek week, int whichWeek) {
		LocalDate day = LocalDate.now().with(TemporalAdjusters.previous(week)).minusWeeks(whichWeek - 1);
		return day.format(FORMATTER_DAY);
	}

	/**
	 * 天数累加
	 */
	public static LocalDateTime plusDay(int plusDay) {
		return LocalDateTime.now().plusDays(plusDay);
	}

	public static String duration(Date startTime, Date endTime) {
		return formatDuration(calculateDuration(startTime, endTime));
	}

	/**
	 * 统计时间
	 */
	public static String duration(Date startTime) {
		return formatDuration(calculateDuration(startTime, new Date()));
	}

	/**
	 * 统计时间，不需要毫秒
	 */
	public static String durationNoNeedMillis(Date startTime, Date endTime) {
		return formatDuration(calculateDuration(startTime, endTime), false, true);
	}

	/**
	 * 统计时间
	 */
	public static Duration calculateDuration(Date startTime, Date endTime) {
		if (startTime == null || endTime == null) {
			return null;
		}
		return Duration.between(startTime.toInstant(), endTime.toInstant());
	}

	public static String formatDuration(Duration duration) {
		return formatDuration(duration, true);
	}

	public static String formatDuration(Duration duration, boolean needMillis) {
		return formatDuration(duration, needMillis, false);
	}

	/**
	 * 格式化日期
	 * @return A human readable duration
	 */
	public static String formatDuration(Duration duration, boolean needMillis, boolean chinese) {
		if (duration == null || duration.isZero() || duration.isNegative()) {
			return "";
		}
		StringBuilder f = new StringBuilder();
		long hours = duration.toHours();
		long minutes = duration.toMinutes();
		long seconds = duration.getSeconds();
		long millis = duration.toMillis();

		String hMsg = "h";
		String mMsg = "m";
		String sMsg = "s";
		String msMsg = "ms";
		if (chinese) {
			hMsg = "小时";
			mMsg = "分钟";
			sMsg = "秒";
			msMsg = "毫秒";
		}
		if (hours != 0) {
			f.append(hours).append(hMsg);
		}
		if (minutes != 0) {
			f.append(minutes - TimeUnit.HOURS.toMinutes(hours)).append(mMsg);
		}
		if (seconds != 0) {
			f.append(seconds - TimeUnit.MINUTES.toSeconds(minutes)).append(sMsg);
		}
		if (needMillis && millis != 0) {
			f.append(millis - TimeUnit.SECONDS.toMillis(seconds)).append(msMsg);
		}
		return f.toString();
	}

	public static String formatDurationV2(Duration duration, boolean needMillis) {
		if (duration == null || duration.isZero() || duration.isNegative()) {
			return "";
		}

		StringBuilder f = new StringBuilder();
		long hours = duration.toHours();
		long minutes = duration.toMinutes();
		long seconds = duration.getSeconds();
		long millis = duration.toMillis();
		if (hours != 0) {
			if (hours < 10) {
				f.append("0").append(hours).append(":");
			} else {
				f.append(hours).append(":");
			}
		} else {
			f.append("00:");
		}
		if (minutes != 0) {
			long m = minutes - TimeUnit.HOURS.toMinutes(hours);
			if (m < 10) {
				f.append("0").append(m).append(":");
			} else {
				f.append(m).append(":");
			}
		} else {
			f.append("00:");
		}
		if (seconds != 0) {
			long s = seconds - TimeUnit.MINUTES.toSeconds(minutes);
			if (s < 10) {
				f.append("0").append(s);
			} else {
				f.append(s);
			}
		} else {
			f.append("00");
		}
		if (needMillis) {
			if (millis != 0) {
				f.append(":").append(millis - TimeUnit.SECONDS.toMillis(seconds));
			} else {
				f.append(":00");
			}
		}
		return f.toString();
	}

	/**
	 * 获取当前日期，默认格式为 yyyy-MM-dd
	 */
	public static String getDate() {
		return dateTimeNow(YYYY_MM_DD);
	}

	public static String dateTimeNow(final String format) {
		return parseDateToStr(format, new Date());
	}

	public static String parseDateToStr(final String format, final Date date) {
		return new SimpleDateFormat(format).format(date);
	}
}
