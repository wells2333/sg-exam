package com.github.tangyi.user.mapper.massage;

import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysMessageReceiverMapper extends CrudMapper<SysMessageReceiver> {

	List<SysMessageReceiver> getByMessageId(@Param("messageId") Long messageId);

	List<SysMessageReceiver> getPublishedMessage(@Param("params") Map<String, Object> params);

	int insertBatch(List<SysMessageReceiver> receivers);

	int deleteByMessageId(@Param("messageId") Long messageId);
}
