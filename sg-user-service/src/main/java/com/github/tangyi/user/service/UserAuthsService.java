package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.api.user.service.IUserAuthsService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SgPreCondition;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.UserAuthsMapper;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserAuthsService extends CrudService<UserAuthsMapper, UserAuths> implements IUserAuthsService {

	@Cacheable(value = UserCacheName.USER_AUTHS, key = "#userAuths.tenantCode + '_' + #userAuths.identifier", unless = "#result == null")
	public UserAuths getByIdentifier(UserAuths userAuths) {
		SgPreCondition.checkIdentifierAndTenantCode(userAuths.getIdentifier(), userAuths.getTenantCode());
		return this.dao.getByIdentifier(userAuths);
	}

	public List<UserAuths> getListByUsers(List<User> userList) {
		return this.dao.getListByUserIds(userList.stream().map(User::getId).distinct().toArray(Long[]::new));
	}

	@Transactional
	@CacheEvict(value = UserCacheName.USER_AUTHS, key = "#userAuths.tenantCode + '_' + #userAuths.identifier")
	public int deleteByIdentifier(UserAuths userAuths) {
		SgPreCondition.checkIdentifierAndTenantCode(userAuths.getIdentifier(), userAuths.getTenantCode());
		return this.dao.deleteByIdentifier(userAuths);
	}

	@Transactional
	@CacheEvict(value = UserCacheName.USER_AUTHS, allEntries = true)
	public int deleteByUserId(UserAuths userAuths) {
		Preconditions.checkState(userAuths.getUserId() != null, "userId must not be null");
		return this.dao.deleteByUserId(userAuths);
	}

	@Transactional
	@CacheEvict(value = UserCacheName.USER_AUTHS, allEntries = true)
	public int insertBatch(List<UserAuths> userAuths) {
		return dao.insertBatch(userAuths);
	}
}
