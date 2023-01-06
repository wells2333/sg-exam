package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectChoices;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectChoicesMapper extends CrudMapper<SubjectChoices> {

    int physicalDelete(SubjectChoices subjectChoices);

    int physicalDeleteAll(Long[] ids);
}
