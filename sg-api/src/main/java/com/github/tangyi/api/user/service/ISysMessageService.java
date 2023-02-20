package com.github.tangyi.api.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.user.model.SysMessage;
import com.github.tangyi.common.service.ICrudService;

import java.util.Map;

public interface ISysMessageService extends ICrudService<SysMessage> {

	SysMessage getByMessageType(Integer type);

	PageInfo<SysMessage> getPublishedMessage(Map<String, Object> params, int pageNum, int pageSize);

}
