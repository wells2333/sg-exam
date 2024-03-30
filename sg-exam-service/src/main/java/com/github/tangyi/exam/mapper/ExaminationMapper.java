package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExaminationMapper extends CrudMapper<Examination> {

	Long findAllExaminationCount();

	List<Long> findIdsOrderByIdAsc(@Param("minId") long minId, @Param("pageSize") int pageSize, @Param("params") Map<String, Object> params);

	List<Examination> findUserExaminations(@Param("params") Map<String, Object> params);

	List<Examination> findExaminationByCourseId(@Param("courseId") Long courseId);

	int findExaminationCount(Examination examination);

	int findExamUserCount(Examination examination);

	/**
	 * 根据id查询考试记录数量
	 */
	int findExaminationRecordCountByExaminationId(String examinationId);

}
