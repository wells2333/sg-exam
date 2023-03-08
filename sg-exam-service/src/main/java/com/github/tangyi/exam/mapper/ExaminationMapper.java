package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExaminationMapper extends CrudMapper<Examination> {

	List<Long> findAllIds();

	List<Examination> findUserExaminations(@Param("params") Map<String, Object> params);

	int findExaminationCount(Examination examination);

	int findExamUserCount(Examination examination);

}
