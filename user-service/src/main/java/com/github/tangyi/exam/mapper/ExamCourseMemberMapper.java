package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.ExamCourseMember;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 课程学员Mapper
 *
 * @author tangyi
 * @date 2022-11-27
 */
@Repository
public interface ExamCourseMemberMapper extends CrudMapper<ExamCourseMember> {

	Integer findMemberCountByCourseId(ExamCourseMember member);

	List<ExamCourseMember> findMembersByCourseId(ExamCourseMember member);

	ExamCourseMember findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

	Integer deleteByCourseIdAndUserId(ExamCourseMember member);
}