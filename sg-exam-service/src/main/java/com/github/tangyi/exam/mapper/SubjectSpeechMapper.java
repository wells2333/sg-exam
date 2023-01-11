package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectSpeech;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectSpeechMapper extends CrudMapper<SubjectSpeech> {

	int physicalDelete(SubjectSpeech subjectSpeech);

	int physicalDeleteAll(Long[] ids);
}
