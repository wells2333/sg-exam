package com.github.tangyi.user.mapper;

import com.github.tangyi.api.user.model.SysMessageRead;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMessageReadMapper extends CrudMapper<SysMessageRead> {

	SysMessageRead findByMessageIdAndReceiverId(@Param("messageId") Long messageId,
			@Param("receiverId") Long receiverId);
}