package com.github.tangyi.operation.service;

import com.github.tangyi.api.other.model.OperationBanner;
import com.github.tangyi.api.other.service.IOperationBannerService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.operation.mapper.OperationBannerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class OperationBannerService extends CrudService<OperationBannerMapper, OperationBanner>
		implements IOperationBannerService, ExamCacheName {

	@Override
	@Cacheable(value = UserCacheName.BANNER, key = "#id")
	public OperationBanner get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(OperationBanner operationBanner) {
		operationBanner.setCommonValue();
		return super.insert(operationBanner);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.BANNER, key = "#operationBanner.id")
	public int update(OperationBanner operationBanner) {
		operationBanner.setCommonValue();
		return super.update(operationBanner);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.BANNER, key = "#operationBanner.id")
	public int delete(OperationBanner operationBanner) {
		return super.delete(operationBanner);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.BANNER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
