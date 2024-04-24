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

package com.github.tangyi.user.service.message;

import com.github.tangyi.api.user.model.SysMessageRead;
import com.github.tangyi.api.user.service.ISysMessageReadService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.user.mapper.massage.SysMessageReadMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysMessageReadService extends CrudService<SysMessageReadMapper, SysMessageRead>
		implements ISysMessageReadService, ExamCacheName {

	@Override
	public SysMessageRead get(Long id) {
		return super.get(id);
	}

	@Override
	public SysMessageRead findByMessageIdAndReceiverId(Long messageId, Long receiverId) {
		return this.dao.findByMessageIdAndReceiverId(messageId, receiverId);
	}

	@Override
	@Transactional
	public int insert(SysMessageRead sysMessageRead) {
		sysMessageRead.setCommonValue();
		return super.insert(sysMessageRead);
	}

	@Override
	@Transactional
	public int update(SysMessageRead sysMessageRead) {
		sysMessageRead.setCommonValue();
		return super.update(sysMessageRead);
	}

	@Override
	@Transactional
	public int delete(SysMessageRead sysMessageRead) {
		return super.delete(sysMessageRead);
	}

	@Override
	@Transactional
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
