package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamExaminationMember;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamExaminationMemberMapper extends CrudMapper<ExamExaminationMember> {

	List<ExamExaminationMember> findListByExamId(Long examId);

	int insertBatch(List<ExamExaminationMember> members);

	int deleteByExamId(Long memberId);
}