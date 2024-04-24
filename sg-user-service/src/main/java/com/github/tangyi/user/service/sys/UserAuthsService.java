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

package com.github.tangyi.user.service.sys;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.api.user.service.IUserAuthsService;
import com.github.tangyi.common.base.BaseEntity;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SgPreCondition;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.UserAuthsMapper;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserAuthsService extends CrudService<UserAuthsMapper, UserAuths>
		implements IUserAuthsService, UserCacheName {

	@Override
	@Cacheable(value = USER_AUTHS, key = "#userAuths.tenantCode + ':' + #userAuths.identifier", unless = "#result == null")
	public UserAuths getByIdentifier(UserAuths userAuths) {
		SgPreCondition.checkIdentifierAndTenantCode(userAuths.getIdentifier(), userAuths.getTenantCode());
		return this.dao.getByIdentifier(userAuths);
	}

	@Override
	public UserAuths getByUserId(UserAuths userAuths) {
		SgPreCondition.checkUserIdAndTenantCode(userAuths.getUserId(), userAuths.getTenantCode());
		Preconditions.checkState(userAuths.getIdentityType() != null, "identityType must not be null");
		return this.dao.getByUserId(userAuths);
	}

	@Override
	public List<UserAuths> getListByUsers(List<User> userList) {
		if (CollectionUtils.isEmpty(userList)) {
			return Collections.emptyList();
		}
		return this.dao.getListByUserIds(BaseEntity.ids(userList));
	}

	@Override
	@Transactional
	@CacheEvict(value = USER_AUTHS, key = "#userAuths.tenantCode + ':' + #userAuths.identifier")
	public int deleteByIdentifier(UserAuths userAuths) {
		SgPreCondition.checkIdentifierAndTenantCode(userAuths.getIdentifier(), userAuths.getTenantCode());
		return this.dao.deleteByIdentifier(userAuths);
	}

	@Override
	@Transactional
	@CacheEvict(value = USER_AUTHS, allEntries = true)
	public int deleteByUserId(UserAuths userAuths) {
		Preconditions.checkState(userAuths.getUserId() != null, "userId must not be null");
		return this.dao.deleteByUserId(userAuths);
	}

	@Override
	@Transactional
	@CacheEvict(value = USER_AUTHS, allEntries = true)
	public int insertBatch(List<UserAuths> userAuths) {
		return dao.insertBatch(userAuths);
	}
}
