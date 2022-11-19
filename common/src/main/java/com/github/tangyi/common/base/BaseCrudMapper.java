package com.github.tangyi.common.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Dao接口
 *
 * @author tangyi
 * @date 2018-08-24 18:59
 */
public interface BaseCrudMapper<T> extends Mapper<T>, MySqlMapper<T> {
}

