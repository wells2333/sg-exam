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

package com.github.tangyi.exam.service.fav;

import com.github.tangyi.api.exam.model.ExamFavStartCount;
import com.github.tangyi.api.exam.service.IFavStartCountService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamFavStartCountMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class FavStartCountService extends CrudService<ExamFavStartCountMapper, ExamFavStartCount>
		implements IFavStartCountService, ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.FAV_START_COUNT, key = "#id")
	public ExamFavStartCount get(Long id) {
		return super.get(id);
	}

	public ExamFavStartCount findByTarget(Long id, Integer targetType) {
		return this.dao.findByTarget(id, targetType);
	}

	@Override
	@Transactional
	public int insert(ExamFavStartCount examFavStartCount) {
		examFavStartCount.setCommonValue();
		return super.insert(examFavStartCount);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.FAV_START_COUNT, key = "#examFavStartCount.id")
	public int update(ExamFavStartCount examFavStartCount) {
		examFavStartCount.setCommonValue();
		return super.update(examFavStartCount);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.FAV_START_COUNT, key = "#examFavStartCount.id")
	public int delete(ExamFavStartCount examFavStartCount) {
		return super.delete(examFavStartCount);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.FAV_START_COUNT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
