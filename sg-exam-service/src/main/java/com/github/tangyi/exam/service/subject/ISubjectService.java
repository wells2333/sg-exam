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
import com.github.tangyi.common.base.BaseEntity;

import java.util.List;

/**
 * 题目相关接口
 */
public interface ISubjectService {

	SubjectDto getSubject(Long id);

	List<SubjectDto> getSubjects(List<Long> ids);

	/**
	 * 根据 ID 查询上一题、下一题
	 * @param nextType      -1：当前题目，0：下一题，1：上一题
	 */
	SubjectDto getNextByCurrentIdAndType(Long examinationId, Long previousId, Integer nextType);

	List<SubjectDto> findSubjectList(SubjectDto subjectDto);

	PageInfo<SubjectDto> findSubjectPage(PageInfo pageInfo, SubjectDto subjectDto);

	List<SubjectDto> findSubjectListById(Long[] ids);

	BaseEntity<?> insertSubject(SubjectDto subjectDto);

	int updateSubject(SubjectDto subjectDto);

	int updateSubjectSort(Long subjectId, Integer sort);

	int deleteSubject(SubjectDto subjectDto);

	int deleteAllSubject(Long[] ids);

	int physicalDeleteSubject(SubjectDto subjectDto);

	int physicalDeleteAllSubject(Long[] ids);
}
