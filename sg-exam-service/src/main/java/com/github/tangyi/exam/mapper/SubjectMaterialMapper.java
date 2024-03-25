package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectMaterial;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectMaterialMapper extends CrudMapper<SubjectMaterial> {

	int physicalDelete(SubjectMaterial subjectFillBlank);

	int physicalDeleteAll(Long[] ids);
}
