package com.github.tangyi.exam.service.exam;

import com.github.tangyi.api.exam.model.ExamEvaluate;
import com.github.tangyi.api.exam.service.IExamEvaluateService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamEvaluateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ExamEvaluateService extends CrudService<ExamEvaluateMapper, ExamEvaluate>
		implements IExamEvaluateService, ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.EXAM_EVALUATE, key = "#id")
	public ExamEvaluate get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamEvaluate evaluate) {
		evaluate.setCommonValue();
		return super.insert(evaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_EVALUATE, key = "#evaluate.id")
	public int update(ExamEvaluate evaluate) {
		evaluate.setCommonValue();
		return super.update(evaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_EVALUATE, key = "#evaluate.id")
	public int delete(ExamEvaluate evaluate) {
		return super.delete(evaluate);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.EXAM_EVALUATE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
