package com.github.tangyi.common.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 统计耗时工具类
 * @author tangyi
 * @date 2022/6/2 9:12 上午
 */
public class StopWatchUtil {

	public static StopWatch start() {
		StopWatch watch = new StopWatch();
		watch.start();
		return watch;
	}

	public static String stop(StopWatch watch) {
		watch.stop();
		Duration duration = Duration.ofMillis(watch.getTime());
		return DateUtils.formatDuration(duration);
	}

	public static String duration(long d) {
		Duration duration = Duration.ofMillis(d);
		return DateUtils.formatDuration(duration, false);
	}

	public static String duration(String startTime) {
		LocalDateTime localDateTime = LocalDateTime.parse(startTime, DateUtils.FORMATTER);
		long duration = System.currentTimeMillis() - DateUtils.asDate(localDateTime).getTime();
		return duration(duration);
	}
}
