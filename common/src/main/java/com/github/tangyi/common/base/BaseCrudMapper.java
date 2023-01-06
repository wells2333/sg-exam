package com.github.tangyi.common.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseCrudMapper<T> extends Mapper<T>, MySqlMapper<T> {
}

