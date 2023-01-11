package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.SysFeedback;
import com.github.tangyi.api.user.service.ISysFeedbackService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.SysFeedbackMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysFeedbackService extends CrudService<SysFeedbackMapper, SysFeedback>
		implements ISysFeedbackService, UserCacheName {

	@Override
	@Cacheable(value = FEEDBACK, key = "#id")
	public SysFeedback get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SysFeedback sysFeedback) {
		sysFeedback.setCommonValue();
		return super.insert(sysFeedback);
	}

	@Override
	@Transactional
	@CacheEvict(value = FEEDBACK, key = "#sysFeedback.id")
	public int update(SysFeedback sysFeedback) {
		sysFeedback.setCommonValue();
		return super.update(sysFeedback);
	}

	@Override
	@Transactional
	@CacheEvict(value = FEEDBACK, key = "#sysFeedback.id")
	public int delete(SysFeedback sysFeedback) {
		return super.delete(sysFeedback);
	}

	@Override
	@Transactional
	@CacheEvict(value = FEEDBACK, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
