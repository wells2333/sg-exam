package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.api.user.service.IUserAuthsService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.UserAuthsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class UserAuthsService extends CrudService<UserAuthsMapper, UserAuths> implements IUserAuthsService {

	@Cacheable(value = UserCacheName.USER_AUTHS, key = "#userAuths.identifier")
	public UserAuths getByIdentifier(UserAuths userAuths) {
		return this.dao.getByIdentifier(userAuths);
	}

	public List<UserAuths> getListByUsers(List<User> userList) {
		return this.dao.getListByUserIds(userList.stream().map(User::getId).distinct().toArray(Long[]::new));
	}

	@Transactional
	@CacheEvict(value = UserCacheName.USER_AUTHS, key = "#userAuths.identifier")
	public int deleteByIdentifier(UserAuths userAuths) {
		return this.dao.deleteByIdentifier(userAuths);
	}

	@Transactional
	@CacheEvict(value = UserCacheName.USER_AUTHS, allEntries = true)
	public int deleteByUserId(UserAuths userAuths) {
		return this.dao.deleteByUserId(userAuths);
	}

	@Transactional
	@CacheEvict(value = UserCacheName.USER_AUTHS, allEntries = true)
	public int insertBatch(List<UserAuths> userAuths) {
		return dao.insertBatch(userAuths);
	}
}
