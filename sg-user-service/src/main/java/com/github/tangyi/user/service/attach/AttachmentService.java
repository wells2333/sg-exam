package com.github.tangyi.user.service.attach;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.service.IAttachmentService;
import com.github.tangyi.common.exceptions.AttachNotExistException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.attach.AttachmentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class AttachmentService extends CrudService<AttachmentMapper, Attachment> implements IAttachmentService {

	@Override
	public Attachment getNotNullAttachment(Long id) {
		Attachment attachment = this.get(id);
		if (attachment == null) {
			throw new AttachNotExistException("attachment does not exist, id: " + id);
		}

		return attachment;
	}

	@Override
	public Attachment findByHash(String hash, String tenantCode) {
		return this.dao.findByHash(hash, tenantCode);
	}

	@Override
	@Cacheable(value = UserCacheName.ATTACHMENT, key = "#id")
	public Attachment get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.ATTACHMENT, key = "#attachment.id")
	public int update(Attachment attachment) {
		return super.update(attachment);
	}

	@Override
	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
	public int delete(Attachment attachment) {
		return super.delete(attachment);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.ATTACHMENT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
	@Override
	public PageInfo<Attachment> attachmentList(Map<String, Object> params, int pageNum, int pageSize) {
		PageInfo<Attachment> page = findPage(params, pageNum, pageSize);
		List<Attachment> list = page.getList();
		if (CollectionUtils.isNotEmpty(list)) {
			return new PageInfo<>(list);
		}
		return new PageInfo<>();
	}

}
