package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationMapper extends CrudMapper<Examination> {

	List<Long> findAllIds();

	int findExaminationCount(Examination examination);

	/**
	 * 查询参与考试人数
	 */
	int findExamUserCount(Examination examination);
}
