package com.github.tangyi.operation.service;

import com.github.tangyi.api.operation.model.OperationBanner;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.constants.OperationCacheName;
import com.github.tangyi.operation.mapper.OperationBannerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 首页运营位Service
 * @author tangyi
 * @date 2022-11-12
 */
@Slf4j
@Service
@AllArgsConstructor
public class OperationBannerService extends CrudService<OperationBannerMapper, OperationBanner>
		implements ExamCacheName {

	@Override
	@Cacheable(value = OperationCacheName.BANNER, key = "#id")
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
	@CacheEvict(value = OperationCacheName.BANNER, key = "#operationBanner.id")
	public int update(OperationBanner operationBanner) {
		operationBanner.setCommonValue();
		return super.update(operationBanner);
	}

	@Override
	@Transactional
	@CacheEvict(value = OperationCacheName.BANNER, key = "#operationBanner.id")
	public int delete(OperationBanner operationBanner) {
		return super.delete(operationBanner);
	}

	@Override
	@Transactional
	@CacheEvict(value = OperationCacheName.BANNER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
