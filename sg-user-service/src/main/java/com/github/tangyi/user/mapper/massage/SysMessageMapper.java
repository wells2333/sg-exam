package com.github.tangyi.user.mapper.massage;

import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMessageMapper extends CrudMapper<SysMessage> {

	SysMessage getByMessageType(Integer type);
}
