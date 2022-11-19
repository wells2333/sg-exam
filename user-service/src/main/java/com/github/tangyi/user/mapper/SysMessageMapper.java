package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 消息Mapper
 *
 * @author tangyi
 * @date 2022-08-16
 */
@Repository
public interface SysMessageMapper extends CrudMapper<SysMessage> {
}