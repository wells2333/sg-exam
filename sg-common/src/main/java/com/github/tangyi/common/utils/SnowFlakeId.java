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

import cn.hutool.core.date.SystemClock;

public class SnowFlakeId {

	private static final int SNOW_FLAKE_WORK_ID = EnvUtils.getInt("SNOW_FLAKE_WORK_ID", 1);
	private static final int SNOW_FLAKE_DATA_CENTER_ID = EnvUtils.getInt("SNOW_FLAKE_DATA_CENTER_ID", 1);
	private static final Snowflake SNOW_FLAKE = new Snowflake(SNOW_FLAKE_WORK_ID, SNOW_FLAKE_DATA_CENTER_ID);

	public static long newId() {
		return SNOW_FLAKE.nextId();
	}

	/**
	 * Twitter_Snowflake<br>
	 * SnowFlake 的结构如下 (每部分用 - 分开):<br>
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
	 * 1 位标识，由于 long 基本类型在 Java 中是带符号的，最高位是符号位，正数是 0，负数是 1，所以 id 一般是正数，最高位是 0<br>
	 * 41 位时间截 (毫秒级)，注意，41 位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
	 * 得到的值），这里的的开始时间截，一般是我们的 id 生成器开始使用的时间，由我们程序来指定的（如下下面程序 IdWorker 类的 startTime 属性）。41 位的时间截，可以使用 69 年，年 T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
	 * 10 位的数据机器位，可以部署在 1024 个节点，包括 5 位 datacenterId 和 5 位 workerId<br>
	 * 12 位序列，毫秒内的计数，12 位的计数顺序号支持每个节点每毫秒 (同一机器，同一时间截) 产生 4096 个 ID 序号<br>
	 * 加起来刚好 64 位，为一个 Long 型。<br>
	 * SnowFlake 的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生 ID 碰撞 (由数据中心 ID 和机器 ID 作区分)，并且效率较高，经测试，SnowFlake 每秒能够产生 26 万 ID 左右。
	 */
	private static class Snowflake {

		/**
		 * 机器 id 所占的位数
		 */
		private final long workerIdBits = 5L;

		/**
		 * 数据标识 id 所占的位数
		 */
		private final long datacenterIdBits = 5L;

		/**
		 * 工作机器 ID(0~31)
		 */
		private final long workerId;

		/**
		 * 数据中心 ID(0~31)
		 */
		private final long datacenterId;

		/**
		 * 毫秒内序列 (0~4095)
		 */
		private long sequence = 0L;

		/**
		 * 上次生成 ID 的时间截
		 */
		private long lastTimestamp = -1L;

		/**
		 * 构造函数
		 *
		 * @param workerId     工作 ID (0~31)
		 * @param datacenterId 数据中心 ID (0~31)
		 */
		public Snowflake(long workerId, long datacenterId) {
			// 支持的最大机器 id，结果是 31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
			long maxWorkerId = ~(-1L << workerIdBits);
			if (workerId > maxWorkerId || workerId < 0) {
				throw new IllegalArgumentException(
						String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
			}
			// 支持的最大数据标识 id，结果是 31
			long maxDatacenterId = ~(-1L << datacenterIdBits);
			if (datacenterId > maxDatacenterId || datacenterId < 0) {
				throw new IllegalArgumentException(
						String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
			}
			this.workerId = workerId;
			this.datacenterId = datacenterId;
		}

		/**
		 * 获得下一个 ID (该方法是线程安全的)
		 *
		 * @return SnowflakeId
		 */
		public synchronized long nextId() {
			long timestamp = timeGen();
			// 如果当前时间小于上一次 ID 生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
			if (timestamp < lastTimestamp) {
				throw new RuntimeException(
						String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
								lastTimestamp - timestamp));
			}
			// 如果是同一时间生成的，则进行毫秒内序列
			// 序列在 id 中占的位数
			long sequenceBits = 12L;
			if (lastTimestamp == timestamp) {
				// 生成序列的掩码，这里为 4095 (0b111111111111=0xfff=4095)
				long sequenceMask = ~(-1L << sequenceBits);
				sequence = (sequence + 1) & sequenceMask;
				// 毫秒内序列溢出
				if (sequence == 0) {
					// 阻塞到下一个毫秒，获得新的时间戳
					timestamp = tilNextMillis(lastTimestamp);
				}
			}
			// 时间戳改变，毫秒内序列重置
			else {
				sequence = 0L;
			}

			// 上次生成 ID 的时间截
			lastTimestamp = timestamp;
			long datacenterIdShift = sequenceBits + workerIdBits;
			// 时间截向左移 22 位 (5+5+12)
			long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
			// 开始时间截 (2015-01-01)
			long twepoch = 1420041600000L;
			return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId
					<< sequenceBits) | sequence;
		}

		/**
		 * 阻塞到下一个毫秒，直到获得新的时间戳
		 *
		 * @param lastTimestamp 上次生成 ID 的时间截
		 * @return 当前时间戳
		 */
		private long tilNextMillis(long lastTimestamp) {
			long timestamp = timeGen();
			while (timestamp <= lastTimestamp) {
				timestamp = timeGen();
			}
			return timestamp;
		}

		/**
		 * 返回以毫秒为单位的当前时间
		 *
		 * @return 当前时间 (毫秒)
		 */
		private long timeGen() {
			return SystemClock.now();
		}
	}
}
