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

package com.github.tangyi.user.service.message;

import com.github.tangyi.api.user.model.SysNews;
import com.github.tangyi.api.user.service.ISysNewsService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.massage.SysNewsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysNewsService extends CrudService<SysNewsMapper, SysNews> implements ISysNewsService, UserCacheName {

	@Override
	@Cacheable(value = NEWS, key = "#id")
	public SysNews get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(SysNews sysNews) {
		sysNews.setCommonValue();
		return super.insert(sysNews);
	}

	@Override
	@Transactional
	@CacheEvict(value = NEWS, key = "#sysNews.id")
	public int update(SysNews sysNews) {
		sysNews.setCommonValue();
		return super.update(sysNews);
	}

	@Override
	@Transactional
	@CacheEvict(value = NEWS, key = "#sysNews.id")
	public int delete(SysNews sysNews) {
		return super.delete(sysNews);
	}

	@Override
	@Transactional
	@CacheEvict(value = NEWS, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
