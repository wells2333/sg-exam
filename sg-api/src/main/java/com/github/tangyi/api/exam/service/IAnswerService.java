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
