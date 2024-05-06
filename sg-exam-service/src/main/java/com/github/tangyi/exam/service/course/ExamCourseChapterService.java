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

import com.github.tangyi.api.exam.model.ExamCourseChapter;
import com.github.tangyi.api.exam.service.IExamCourseChapterService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseChapterMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程章
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseChapterService extends CrudService<ExamCourseChapterMapper, ExamCourseChapter>
		implements IExamCourseChapterService, ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.CHAPTER, key = "#id")
	public ExamCourseChapter get(Long id) {
		return super.get(id);
	}

	@Override
	public Integer findMaxSortByCourseId(Long courseId) {
		return this.dao.findMaxSortByCourseId(courseId);
	}

	@Override
	@Transactional
	public int insert(ExamCourseChapter examCourseChapter) {
		examCourseChapter.setCommonValue();
		return super.insert(examCourseChapter);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.CHAPTER, key = "#examCourseChapter.id")
	public int update(ExamCourseChapter examCourseChapter) {
		examCourseChapter.setCommonValue();
		return super.update(examCourseChapter);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.CHAPTER, key = "#examCourseChapter.id")
	public int delete(ExamCourseChapter examCourseChapter) {
		return super.delete(examCourseChapter);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.CHAPTER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	public List<ExamCourseChapter> findChaptersByCourseId(Long courseId) {
		return this.dao.findChaptersByCourseId(courseId);
	}
}
