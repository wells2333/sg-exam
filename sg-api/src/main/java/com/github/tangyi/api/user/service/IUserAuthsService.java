package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;

public interface IUserAuthsService extends ICrudService<UserAuths> {

	UserAuths getByIdentifier(UserAuths userAuths);

	UserAuths getByUserId(UserAuths userAuths);

	List<UserAuths> getListByUsers(List<User> userList);

	int deleteByIdentifier(UserAuths userAuths);

	int deleteByUserId(UserAuths userAuths);

	int insertBatch(List<UserAuths> userAuths);
}
