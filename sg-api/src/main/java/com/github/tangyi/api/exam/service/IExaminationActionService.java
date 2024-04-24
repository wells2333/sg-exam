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

package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.ExamRecordDetailsDto;
import com.github.tangyi.api.exam.dto.StartExamDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.api.exam.model.ExaminationRecord;

import java.util.List;

public interface IExaminationActionService {

	StartExamDto start(ExaminationRecord examRecord);

	StartExamDto start(Long userId, String identifier, Long examinationId, String tenantCode);

	StartExamDto anonymousUserStart(Long examinationId, String identifier);

	/**
	 * 提交答卷，自动统计选择题得分
	 */
	Boolean submit(Long recordId, String operator, String tenantCode);

	boolean submitAsync(Answer answer);

	Boolean submitAll(List<AnswerDto> answers);

	/**
	 * 移动端提交答题
	 */
	boolean anonymousUserSubmit(Long examinationId, String identifier, List<SubjectDto> dtos);

	/**
	 * 成绩详情
	 */
	ExamRecordDetailsDto details(Long id);
}
