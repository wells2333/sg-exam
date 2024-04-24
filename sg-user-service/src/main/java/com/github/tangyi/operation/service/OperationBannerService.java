/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
