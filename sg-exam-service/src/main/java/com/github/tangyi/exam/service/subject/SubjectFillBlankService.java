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

package com.github.tangyi.exam.service.subject;

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectFillBlank;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectFillBlankMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectFillBlankConverter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 填空题
 */
@AllArgsConstructor
@Service
public class SubjectFillBlankService extends CrudService<SubjectFillBlankMapper, SubjectFillBlank>
		implements ISubjectService {

	private final SubjectFillBlankConverter converter;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#id")
	public SubjectFillBlank get(Long id) {
		return super.get(id);
	}

	@Override
	public SubjectDto getSubject(Long id) {
		return converter.convert(this.get(id));
	}

	@Override
	public List<SubjectDto> getSubjects(List<Long> ids) {
		List<SubjectDto> list = Lists.newArrayListWithExpectedSize(ids.size());
		for (Long id : ids) {
			SubjectDto dto = getSubject(id);
			if (dto != null) {
				list.add(dto);
			}
		}
		return list;
	}

	@Override
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		return null;
	}

	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		return null;
	}

	@Override
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		return null;
	}

	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return converter.convert(this.findListById(ids), true);
	}

	@Override
	@Transactional
	public BaseEntity<?> insertSubject(SubjectDto subjectDto) {
		SubjectFillBlank s = new SubjectFillBlank();
		BeanUtils.copyProperties(subjectDto, s);
		s.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(s);
		return s;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectFillBlank s = new SubjectFillBlank();
		BeanUtils.copyProperties(subjectDto, s);
		s.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectId")
	public int updateSubjectSort(Long subjectId, Integer sort) {
		SubjectFillBlank s = new SubjectFillBlank();
		s.setId(subjectId);
		s.setSort(sort);
		return this.update(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectFillBlank s = new SubjectFillBlank();
		BeanUtils.copyProperties(subjectDto, s);
		return this.delete(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectFillBlank s = new SubjectFillBlank();
		BeanUtils.copyProperties(subjectDto, s);
		return this.physicalDelete(s);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_FILL_BLANK, key = "#s.id")
	public int physicalDelete(SubjectFillBlank s) {
		return this.dao.physicalDelete(s);
	}
}
