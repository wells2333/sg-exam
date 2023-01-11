package com.github.tangyi.common.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.time.Duration;
import java.time.LocalDateTime;

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
