package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.common.vo.UserVo;

import java.util.List;

public interface IIdentifyService {

	UserVo findUserByIdentifier(Integer identityType, String identifier, String tenantCode);

	User findUserByPhone(String phone, String tenantCode);

	UserAuths findUserAuthsByIdentifier(String identifier);

	UserAuths findUserAuthsByIdentifier(String identifier, String tenantCode);

	UserAuths findUserAuthsByIdentifier(Integer identityType, String identifier, String tenantCode);

	UserAuths findUserAuthsByUserId(Integer identityType, Long userId, String tenantCode);

	void createUserRole(UserDto userDto, User user);

	int createUser(UserDto userDto);

	UserAuths createUserAuths(UserDto userDto, User user);

	void createUserRole(Long userId, List<Long> roleIds);

	boolean defaultRole(User user, String tenantCode, String identifier);

	boolean register(UserDto userDto);

	void registerUserAuths(User user, String identifier, Integer identityType, String password);

	boolean updateUser(Long id, UserDto userDto);

	int updatePassword(UserDto userDto);

	boolean updateLoginInfo(UserDto userDto);

	boolean resetPassword(UserDto userDto);
}
