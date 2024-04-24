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

package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.Knowledge;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.KnowledgeMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnowledgeService extends CrudService<KnowledgeMapper, Knowledge> {

	@Override
	@Cacheable(value = ExamCacheName.KNOWLEDGE, key = "#id")
	public Knowledge get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE, key = "#knowledge.id")
	public int update(Knowledge knowledge) {
		return super.update(knowledge);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE, key = "#knowledge.id")
	public int delete(Knowledge knowledge) {
		return super.delete(knowledge);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
