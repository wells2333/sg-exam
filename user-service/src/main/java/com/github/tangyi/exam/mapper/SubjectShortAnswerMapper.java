package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectShortAnswer;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectShortAnswerMapper extends CrudMapper<SubjectShortAnswer> {

    int physicalDelete(SubjectShortAnswer subjectShortAnswer);

    int physicalDeleteAll(Long[] ids);
}
