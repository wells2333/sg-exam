package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectVideo;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectVideoMapper extends CrudMapper<SubjectVideo> {

	int physicalDelete(SubjectVideo subjectVideo);

	int physicalDeleteAll(Long[] ids);
}
