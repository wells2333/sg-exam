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

package com.github.tangyi.exam.service.course;

import com.github.tangyi.api.exam.model.ExamCourseEvaluate;
import com.github.tangyi.api.exam.service.IExamCourseEvaluateService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseEvaluateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseEvaluateService extends CrudService<ExamCourseEvaluateMapper, ExamCourseEvaluate>
		implements IExamCourseEvaluateService, ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.EVALUATE, key = "#id")
	public ExamCourseEvaluate get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamCourseEvaluate examCourseEvaluate) {
		examCourseEvaluate.setCommonValue();
		return super.insert(examCourseEvaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EVALUATE, key = "#examCourseEvaluate.id")
	public int update(ExamCourseEvaluate examCourseEvaluate) {
		examCourseEvaluate.setCommonValue();
		return super.update(examCourseEvaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EVALUATE, key = "#examCourseEvaluate.id")
	public int delete(ExamCourseEvaluate examCourseEvaluate) {
		return super.delete(examCourseEvaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EVALUATE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
