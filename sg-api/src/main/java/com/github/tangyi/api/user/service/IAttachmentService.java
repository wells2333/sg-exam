package com.github.tangyi.api.user.service;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.service.ICrudService;

import java.util.Map;

public interface IAttachmentService extends ICrudService<Attachment> {

	Attachment getNotNullAttachment(Long id);

	Attachment findByHash(String hash, String tenantCode);

	PageInfo<Attachment> attachmentList(Map<String, Object> params, int pageNum, int pageSize);
}
