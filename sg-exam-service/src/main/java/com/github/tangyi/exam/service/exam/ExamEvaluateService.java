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

package com.github.tangyi.exam.service.exam;

import com.github.tangyi.api.exam.model.ExamEvaluate;
import com.github.tangyi.api.exam.service.IExamEvaluateService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamEvaluateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ExamEvaluateService extends CrudService<ExamEvaluateMapper, ExamEvaluate>
		implements IExamEvaluateService, ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.EXAM_EVALUATE, key = "#id")
	public ExamEvaluate get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamEvaluate evaluate) {
		evaluate.setCommonValue();
		return super.insert(evaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_EVALUATE, key = "#evaluate.id")
	public int update(ExamEvaluate evaluate) {
		evaluate.setCommonValue();
		return super.update(evaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_EVALUATE, key = "#evaluate.id")
	public int delete(ExamEvaluate evaluate) {
		return super.delete(evaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_EVALUATE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
