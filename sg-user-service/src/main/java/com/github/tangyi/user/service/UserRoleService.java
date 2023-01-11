package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.UserRole;
import com.github.tangyi.api.user.service.IUserRoleService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserRoleService extends CrudService<UserRoleMapper, UserRole> implements IUserRoleService {

	private final UserRoleMapper userRoleMapper;

	public List<UserRole> getByUserId(Long userId) {
		return userRoleMapper.getByUserId(userId);
	}

	public List<UserRole> getByUserIds(List<Long> userIds) {
		return userRoleMapper.getByUserIds(userIds);
	}

	public int insertBatch(List<UserRole> userRoles) {
		return userRoleMapper.insertBatch(userRoles);
	}
}
