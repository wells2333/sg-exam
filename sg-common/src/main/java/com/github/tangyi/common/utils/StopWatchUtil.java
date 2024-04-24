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

import org.apache.commons.lang3.time.StopWatch;

import java.time.Duration;
import java.time.LocalDateTime;

public class StopWatchUtil {

	private StopWatchUtil() {
	}

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
