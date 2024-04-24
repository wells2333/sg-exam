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

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.SubjectShortAnswer;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.PageUtil;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.SubjectShortAnswerMapper;
import com.github.tangyi.exam.service.subject.converter.SubjectShortAnswerConverter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SubjectShortAnswerService extends CrudService<SubjectShortAnswerMapper, SubjectShortAnswer>
		implements ISubjectService {

	private final SubjectShortAnswerConverter converter;

	@Override
	@Cacheable(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#id")
	public SubjectShortAnswer get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SubjectShortAnswer subjectShortAnswer) {
		return super.insert(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int update(SubjectShortAnswer subjectShortAnswer) {
		return super.update(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int delete(SubjectShortAnswer subjectShortAnswer) {
		return super.delete(subjectShortAnswer);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectShortAnswer.id")
	public int physicalDelete(SubjectShortAnswer subjectShortAnswer) {
		return this.dao.physicalDelete(subjectShortAnswer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int physicalDeleteAll(Long[] ids) {
		return this.dao.physicalDeleteAll(ids);
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

	/**
	 * 根据上一题 ID 查询下一题
	 * @param nextType      0：下一题，1：上一题
	 */
	@Override
	public SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType) {
		return null;
	}

	@Override
	@Transactional
	public BaseEntity<SubjectShortAnswer> insertSubject(SubjectDto subjectDto) {
		SubjectShortAnswer answer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, answer);
		answer.setAnswer(subjectDto.getAnswer().getAnswer());
		this.insert(answer);
		return answer;
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int updateSubject(SubjectDto subjectDto) {
		SubjectShortAnswer subject = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, subject);
		// 参考答案
		subject.setAnswer(subjectDto.getAnswer().getAnswer());
		return this.update(subject);
	}

	@Override
	public int updateSubjectSort(Long subjectId, Integer sort) {
		SubjectShortAnswer subject = new SubjectShortAnswer();
		subject.setId(subjectId);
		subject.setSort(sort);
		return this.update(subject);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int deleteSubject(SubjectDto subjectDto) {
		SubjectShortAnswer answer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, answer);
		return this.delete(answer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, key = "#subjectDto.id")
	public int physicalDeleteSubject(SubjectDto subjectDto) {
		SubjectShortAnswer answer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, answer);
		return this.physicalDelete(answer);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int deleteAllSubject(Long[] ids) {
		return this.deleteAll(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.SUBJECT_SHORT_ANSWER, allEntries = true)
	public int physicalDeleteAllSubject(Long[] ids) {
		return this.physicalDeleteAll(ids);
	}

	@Override
	public List<SubjectDto> findSubjectList(SubjectDto subjectDto) {
		SubjectShortAnswer answer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, answer);
		return converter.convert(this.findList(answer), true);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto) {
		SubjectShortAnswer answer = new SubjectShortAnswer();
		BeanUtils.copyProperties(subjectDto, answer);
		Map<String, Object> condition = Maps.newHashMap();
		PageInfo page = this.findPage(condition, 0, 10);
		PageInfo<SubjectDto> dtoPage = new PageInfo<>();
		PageUtil.copyProperties(page, dtoPage);
		dtoPage.setList(converter.convert(page.getList(), true));
		return dtoPage;
	}

	@Override
	public List<SubjectDto> findSubjectListById(Long[] ids) {
		return converter.convert(this.findListById(ids), true);
	}
}
