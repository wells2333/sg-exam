package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.SysMessageRead;
import com.github.tangyi.common.service.ICrudService;

public interface ISysMessageReadService extends ICrudService<SysMessageRead> {

	SysMessageRead findByMessageIdAndReceiverId(Long messageId, Long receiverId);

}
