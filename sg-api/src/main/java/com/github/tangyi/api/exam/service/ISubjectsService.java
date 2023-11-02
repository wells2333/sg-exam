package com.github.tangyi.api.exam.service;

import com.github.tangyi.api.exam.model.Subjects;
import com.github.tangyi.common.service.ICrudService;

public interface ISubjectsService extends ICrudService<Subjects> {

	void resetSortByExaminationId(Long examinationId, Integer maxSort);

	void resetSortByCategoryId(Long categoryId, Integer maxSort);
}
