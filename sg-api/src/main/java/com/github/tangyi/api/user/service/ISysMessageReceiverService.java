package com.github.tangyi.api.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessageReceiver;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Map;

public interface ISysMessageReceiverService extends ICrudService<SysMessageReceiver> {

	List<SysMessageReceiver> getByMessageId(Long messageId);

	List<SysMessageReceiver> getPublishedMessage(Long receiverId);

	PageInfo<SysMessageReceiver> getPublishedMessage(Map<String, Object> params, int pageNum, int pageSize);

	int insertBatch(List<SysMessageReceiver> receivers);

	int deleteByMessageId(Long messageId);
}
