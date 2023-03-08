package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.model.ExamExaminationMember;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;

public interface IExamExaminationMemberService extends ICrudService<ExamExaminationMember> {

	List<ExamExaminationMember> findListByExamId(Long examId);

	int insertBatch(List<ExamExaminationMember> members);

	int deleteByExamId(Long memberId);

}
