package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectFillBlank;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectFillBlankMapper extends CrudMapper<SubjectFillBlank> {

	int physicalDelete(SubjectFillBlank subjectFillBlank);

	int physicalDeleteAll(Long[] ids);
}
