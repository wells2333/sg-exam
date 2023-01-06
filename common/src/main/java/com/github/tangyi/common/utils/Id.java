package com.github.tangyi.common.utils;

public class Id {

	public static long nextId() {
		return SpringContextHolder.getApplicationContext().getBean(SnowflakeIdWorker.class).nextId();
	}
}
