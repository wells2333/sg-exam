package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectJudgement;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectJudgementMapper extends CrudMapper<SubjectJudgement> {

    int physicalDelete(SubjectJudgement subjectJudgement);

    int physicalDeleteAll(Long[] ids);
}
