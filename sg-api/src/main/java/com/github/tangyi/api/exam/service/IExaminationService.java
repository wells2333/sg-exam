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

	List<Long> findAllIds();

	PageInfo<ExaminationDto> examinationList(Map<String, Object> params, int pageNum, int pageSize);

	PageInfo<SubjectDto> findSubjectPageById(SubjectDto subjectDto, Map<String, Object> params, int pageNum,
			int pageSize);

	ExaminationDto getDetail(Long id);

	Integer nextSubjectNo(Long examinationId);

	Boolean batchAddSubjects(Long id, List<SubjectDto> subjects);

	Boolean randomAddSubjects(Long id, RandomSubjectDto params);
}
