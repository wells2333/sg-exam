/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
			throw new AttachNotExistException("Attachment does not exist, id: " + id);
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
