package com.github.tangyi.common.utils;

import java.util.UUID;

/**
 * id生成工具类
 *
 * @author tangyi
 * @date 2018-08-23 12:03
 */
public class IdGen {

    /**
     * 封装JDK自带的UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 基于snowflake算法生成ID
     *
     * @return String
     * @author tangyi
     * @date 2019/04/26 11:24
     */
    public static long snowflakeId() {
        return SpringContextHolder.getApplicationContext().getBean(SnowflakeIdWorker.class).nextId();
    }
}