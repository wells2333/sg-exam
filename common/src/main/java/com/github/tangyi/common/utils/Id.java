package com.github.tangyi.common.utils;

/**
 *
 * @author tangyi
 * @date 2022/11/19 10:28 上午
 */
public class Id {

	public static long nextId() {
		return SpringContextHolder.getApplicationContext().getBean(SnowflakeIdWorker.class).nextId();
	}
}
