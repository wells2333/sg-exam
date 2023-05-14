package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.service.ICrudService;

public interface IAttachmentService extends ICrudService<Attachment> {

	Attachment getNotNullAttachment(Long id);

	Attachment findByHash(String hash, String tenantCode);
}
