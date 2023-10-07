package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamPermission;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamPermissionMapper extends CrudMapper<ExamPermission> {

	List<ExamPermission> findPermissionList(@Param("examType") Integer examType, @Param("examId") Long examId);

	Integer findCount(@Param("examType") Integer examType, @Param("examId") Long examId);

	int insertBatch(List<ExamPermission> members);

	int deletePermission(@Param("examType") Integer examType, @Param("examId") Long examId);
}