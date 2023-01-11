package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.Knowledge;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.KnowledgeMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnowledgeService extends CrudService<KnowledgeMapper, Knowledge> {

	@Override
	@Cacheable(value = ExamCacheName.KNOWLEDGE, key = "#id")
	public Knowledge get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE, key = "#knowledge.id")
	public int update(Knowledge knowledge) {
		return super.update(knowledge);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE, key = "#knowledge.id")
	public int delete(Knowledge knowledge) {
		return super.delete(knowledge);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.KNOWLEDGE, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
