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

import com.github.tangyi.api.user.model.SysFeedback;
import com.github.tangyi.api.user.service.ISysFeedbackService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.massage.SysFeedbackMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysFeedbackService extends CrudService<SysFeedbackMapper, SysFeedback>
		implements ISysFeedbackService, UserCacheName {

	@Override
	@Cacheable(value = FEEDBACK, key = "#id")
	public SysFeedback get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SysFeedback sysFeedback) {
		sysFeedback.setCommonValue();
		return super.insert(sysFeedback);
	}

	@Override
	@Transactional
	@CacheEvict(value = FEEDBACK, key = "#sysFeedback.id")
	public int update(SysFeedback sysFeedback) {
		sysFeedback.setCommonValue();
		return super.update(sysFeedback);
	}

	@Override
	@Transactional
	@CacheEvict(value = FEEDBACK, key = "#sysFeedback.id")
	public int delete(SysFeedback sysFeedback) {
		return super.delete(sysFeedback);
	}

	@Override
	@Transactional
	@CacheEvict(value = FEEDBACK, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
