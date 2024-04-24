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

import com.github.pagehelper.PageInfo;
import com.github.tangyi.api.exam.dto.AnswerAnalysisDto;
import com.github.tangyi.api.exam.dto.AnswerDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Answer;
import com.github.tangyi.common.service.ICrudService;

public interface IAnswerService extends ICrudService<Answer> {

    SubjectDto saveAndNext(AnswerDto answerDto, Integer type, Integer nextSubjectSortNo);

    int save(AnswerDto answerDto, Long userId, String userCode, String tenantCode);

    int updateScore(Answer answer);

    int markOk(Long recordId);

    PageInfo<AnswerDto> answerListInfo(String pageNum, String pageSize, Long recordId, Answer answer);

    AnswerDto answerInfo(Long recordId, Long currentSubjectId, Integer nextType);

	AnswerAnalysisDto analysis(Long examinationId);

    Boolean isOK(Long recordId);
}
