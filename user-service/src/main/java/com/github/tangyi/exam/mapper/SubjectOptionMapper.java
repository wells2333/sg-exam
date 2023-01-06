package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectOption;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectOptionMapper extends CrudMapper<SubjectOption> {

	List<SubjectOption> getBySubjectChoicesId(SubjectOption subjectOption);

	/**
	 * 根据选择题ID批量查询
	 */
	List<SubjectOption> getBySubjectChoicesIds(@Param("ids") List<Long> ids);

	int insertBatch(List<SubjectOption> subjectOptionList);

	int deleteBySubjectChoicesId(SubjectOption subjectOption);

	int physicalDeleteAll(Long[] ids);
}
