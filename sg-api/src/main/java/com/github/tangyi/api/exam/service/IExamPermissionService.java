package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.dto.MemberDto;
import com.github.tangyi.api.exam.model.ExamPermission;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;

public interface IExamPermissionService extends ICrudService<ExamPermission> {

	List<ExamPermission> findPermissionList(Integer examType, Long examId);

	Integer findCount(Integer examType, Long examId);

	MemberDto getMembers(Integer examType, Long examId);

	int insertBatch(List<ExamPermission> members);

	void deletePermission(Integer examType, Long memberId);

}
