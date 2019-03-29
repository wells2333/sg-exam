package com.github.tangyi.common.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service基类
 *
 * @author tangyi
 * @date 2018-08-24 18:54
 */
public abstract class BaseService {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
}