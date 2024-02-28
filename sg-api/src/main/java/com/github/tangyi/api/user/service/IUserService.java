package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.common.service.ICrudService;
import com.github.tangyi.common.vo.UserVo;

import java.util.List;

public interface IUserService extends ICrudService<User> {

	List<UserVo> findUserVoListById(Long[] ids);

	Long findAllUserCount();

	UserVo getUserInfo(Long id);

	int createUser(UserDto userDto);

	UserAuths createUserAuths(UserDto userDto, User user);

	void createUserRole(UserDto userDto, User user);

	void createUserRole(Long userId, List<Long> roleIds);

	UserVo findUserByIdentifier(Integer identityType, String identifier, String tenantCode);

	User findUserByPhone(String phone, String tenantCode);

	UserAuths findUserAuthsByIdentifier(String identifier);

	UserAuths findUserAuthsByIdentifier(String identifier, String tenantCode);

	UserAuths findUserAuthsByIdentifier(Integer identityType, String identifier, String tenantCode);

	UserAuths findUserAuthsByUserId(Integer identityType, Long userId, String tenantCode);

}
