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
import com.github.tangyi.api.exam.dto.ExaminationDto;
import com.github.tangyi.api.exam.dto.RandomSubjectDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Map;

public interface IExaminationService extends ICrudService<Examination> {

	Long findAllExaminationCount();

	List<Long> findAllIds();

	PageInfo<ExaminationDto> examinationList(Map<String, Object> params, int pageNum, int pageSize);

	PageInfo<ExaminationDto> userExaminationList(Map<String, Object> params, int pageNum, int pageSize);

	int findExaminationRecordCountByExaminationId(String examinationId);

	PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, Map<String, Object> params, int pageNum,
			int pageSize);

	PageInfo<Examination> findUserExaminations(Map<String, Object> params, int pageNum, int pageSize);

	ExaminationDto getDetail(Long id);

	int insertExamination(ExaminationDto dto);

	int updateExamination(ExaminationDto dto);

	Integer nextSubjectNo(Long examinationId);

	Boolean batchAddSubjects(Long id, List<SubjectDto> subjects);

	Boolean randomAddSubjects(Long id, RandomSubjectDto params);

	void addIndex(Examination examination, long clickCnt, long joinCnt);

	void updateIndex(Examination examination);

	String generateQrCodeMessage(Long examinationId);

	byte[] generateQrCode(Long examinationId);

}
