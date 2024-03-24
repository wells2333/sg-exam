package com.github.tangyi.api.user.service;

import com.github.tangyi.api.user.model.Role;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.api.user.model.UserAuths;
import com.github.tangyi.api.user.model.UserRole;
import com.github.tangyi.common.service.ICrudService;

import java.util.List;
import java.util.Set;

public interface IUserRoleService extends ICrudService<UserRole> {

	List<Role> getUserRoles(UserAuths userAuths);

	Set<String> getUserPermissions(User user, String identifier, List<Role> roles);
}
