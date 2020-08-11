package com.github.tangyi.user.mapper;

import com.github.tangyi.common.model.Log;
import com.github.tangyi.common.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志
 *
 * @author tangyi
 * @date 2018/10/31 20:38
 */
@Mapper
public interface LogMapper extends CrudMapper<Log> {
}
