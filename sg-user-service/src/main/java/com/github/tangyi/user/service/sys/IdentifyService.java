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

import com.github.tangyi.api.user.constant.TenantConstant;
import com.github.tangyi.api.user.dto.UserDto;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.enums.IdentityType;
import com.github.tangyi.api.user.model.*;
import com.github.tangyi.api.user.service.IIdentifyService;
import com.github.tangyi.api.user.service.IUserAuthsService;
import com.github.tangyi.api.user.service.IUserRoleService;
import com.github.tangyi.api.user.service.IUserService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.CommonConstant;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.AesUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.TxUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.sys.RoleMapper;
import com.github.tangyi.user.mapper.sys.UserMapper;
import com.github.tangyi.user.mapper.sys.UserRoleMapper;
import com.github.tangyi.user.service.attach.AttachmentService;
import com.github.tangyi.user.utils.UserUtils;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IdentifyService implements IIdentifyService, UserCacheName {

	private final PlatformTransactionManager txManager;
	private final IUserRoleService userRoleService;
	private final IUserAuthsService userAuthsService;
	private final IUserService userService;
	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;
	private final RoleMapper roleMapper;
	private final SysProperties sysProperties;
	private final AttachmentService attachmentService;

	/**
	 * 根据用户唯一标识获取用户详细信息
	 */
	@Override
	public UserVo findUserByIdentifier(Integer identityType, String identifier, String tenantCode) {
		UserAuths userAuths = findUserAuthsByIdentifier(identityType, identifier, tenantCode);
		if (userAuths == null) {
			return null;
		}

		User user = this.userService.get(userAuths.getUserId());
		if (user == null) {
			return null;
		}

		List<Role> roles = this.userRoleService.getUserRoles(userAuths);
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		BeanUtils.copyProperties(userAuths, userVo);
		userVo.setRoleList(UserUtils.rolesToVo(roles));
		userVo.setUserId(user.getId());
		return userVo;
	}

	@Override
	public User findUserByPhone(String phone, String tenantCode) {
		Preconditions.checkNotNull(phone);
		Preconditions.checkNotNull(tenantCode);
		return this.userMapper.findUserByPhone(phone, tenantCode);
	}

	@Override
	public UserAuths findUserAuthsByIdentifier(String identifier) {
		return findUserAuthsByIdentifier(null, identifier, SysUtil.getTenantCode());
	}

	@Override
	public UserAuths findUserAuthsByIdentifier(String identifier, String tenantCode) {
		return findUserAuthsByIdentifier(null, identifier, tenantCode);
	}

	@Override
	public UserAuths findUserAuthsByIdentifier(Integer identityType, String identifier, String tenantCode) {
		UserAuths condition = new UserAuths();
		condition.setIdentifier(identifier);
		if (identityType != null) {
			condition.setIdentityType(IdentityType.matchByType(identityType).getValue());
		}
		condition.setTenantCode(tenantCode);
		return userAuthsService.getByIdentifier(condition);
	}

	@Override
	public UserAuths findUserAuthsByUserId(Integer identityType, Long userId, String tenantCode) {
		UserAuths condition = new UserAuths();
		condition.setUserId(userId);
		condition.setTenantCode(tenantCode);
		if (identityType != null) {
			condition.setIdentityType(IdentityType.matchByType(identityType).getValue());
		}
		return userAuthsService.getByUserId(condition);
	}


	@Override
	@Transactional
	public int createUser(UserDto userDto) {
		String tenantCode = SysUtil.getTenantCode();
		userDto.setTenantCode(tenantCode);
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setCommonValue();
		// 保存父子账号关系
		UserAuths current = findUserAuthsByIdentifier(userDto.getIdentityType(), SysUtil.getUser(), tenantCode);
		user.setParentUid(current.getUserId());
		int update = this.userMapper.insert(user);
		if (user.getId() != null) {
			createUserAuths(userDto, user);
			createUserRole(userDto, user);
		}
		return update;
	}

	@Override
	@Transactional
	public void createUserRole(UserDto userDto, User user) {
		if (CollectionUtils.isEmpty(user.getRole())) {
			defaultRole(user, user.getTenantCode(), userDto.getIdentifier());
			return;
		}
		createUserRole(user.getId(), user.getRole());
	}

	@Override
	@Transactional
	public void createUserRole(Long userId, List<Long> roleIds) {
		roleIds.forEach(roleId -> {
			UserRole ur = new UserRole();
			ur.setNewRecord(true);
			ur.setCommonValue();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			userRoleMapper.insert(ur);
		});
	}

	@Override
	@Transactional
	public UserAuths createUserAuths(UserDto userDto, User user) {
		UserAuths userAuths = new UserAuths();
		userAuths.setCommonValue(user.getCreator(), user.getTenantCode());
		userAuths.setUserId(user.getId());
		userAuths.setIdentifier(userDto.getIdentifier());
		// 默认为账号密码类型
		if (userDto.getIdentityType() == null) {
			userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
		}
		// 设置默认密码
		if (StringUtils.isBlank(userDto.getCredential())) {
			userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
		}
		userAuths.setCredential(UserService.ENCODER.encode(userDto.getCredential()));
		userAuthsService.insert(userAuths);
		return userAuths;
	}

	@Override
	public boolean defaultRole(User user, String tenantCode, String identifier) {
		if (StringUtils.isEmpty(tenantCode)) {
			tenantCode = TenantConstant.DEFAULT_TENANT_CODE;
		}
		Role defaultRole = roleMapper.findDefaultRole(tenantCode);
		if (defaultRole != null) {
			UserRole userRole = new UserRole();
			userRole.setCommonValue(identifier, tenantCode);
			userRole.setUserId(user.getId());
			userRole.setRoleId(defaultRole.getId());
			return userRoleMapper.insert(userRole) > 0;
		}
		return false;
	}

	@Override
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean register(UserDto userDto) {
		Preconditions.checkNotNull(userDto.getIdentityType());
		String password = decryptCredential(userDto.getCredential(), userDto.getIdentityType());
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		// 初始化用户名，系统编号，租户编号
		user.setCommonValue(userDto.getIdentifier(), SysUtil.getTenantCode());
		user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
		TransactionStatus status = TxUtil.startTransaction(txManager);
		try {
			// 初始化头像
			if (StringUtils.isNotBlank(userDto.getAvatarUrl())) {
				Attachment attachment = new Attachment();
				attachment.setCommonValue(userDto.getIdentifier(), SysUtil.getTenantCode());
				attachment.setUrl(userDto.getAvatarUrl());
				attachment.setGroupCode(AttachTypeEnum.AVATAR.getValue());
				if (attachmentService.insert(attachment) > 0) {
					user.setAvatarId(attachment.getId());
				}
			}
			if (this.userMapper.insert(user) > 0) {
				this.registerUserAuths(user, userDto.getIdentifier(), userDto.getIdentityType(), password);
				// 分配默认角色
				boolean res = this.defaultRole(user, userDto.getTenantCode(), userDto.getIdentifier());
				txManager.commit(status);
				return res;
			}
		} catch (DuplicateKeyException e) {
			txManager.rollback(status);
			log.error("Failed to register due to: {}", e.getMessage(), e);
			throw new CommonException("Username duplicate: " + userDto.getIdentifier());
		} catch (Exception e) {
			txManager.rollback(status);
			throw e;
		}
		return false;
	}

	@Override
	public void registerUserAuths(User user, String identifier, Integer identityType, String password) {
		UserAuths userAuths = new UserAuths();
		userAuths.setCommonValue(identifier, user.getTenantCode());
		userAuths.setUserId(user.getId());
		userAuths.setIdentifier(identifier);
		if (identityType != null) {
			userAuths.setIdentityType(identityType);
		}
		userAuths.setCredential(UserService.ENCODER.encode(password));
		userAuthsService.insert(userAuths);
	}

	@Override
	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean updateUser(Long id, UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		user.setId(id);
		user.setCommonValue();
		this.userMapper.updateByPrimaryKeySelective(user);
		UserRole sysUserRole = new UserRole();
		sysUserRole.setUserId(user.getId());
		userRoleMapper.delete(sysUserRole);
		if (CollectionUtils.isNotEmpty(userDto.getRole())) {
			this.createUserRole(user.getId(), userDto.getRole());
		}
		return Boolean.TRUE;
	}

	@Override
	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public int updatePassword(UserDto userDto) {
		SgPreconditions.checkBlank(userDto.getNewPassword(), "The password cannot be null.");
		SgPreconditions.checkBlank(userDto.getIdentifier(), "The identifier cannot be null.");
		UserAuths userAuths = this.findUserAuthsByIdentifier(null, userDto.getIdentifier(), userDto.getTenantCode());
		SgPreconditions.checkNull(userAuths, "The identifier does not exists.");
		if (!UserService.ENCODER.matches(userDto.getOldPassword(), userAuths.getCredential())) {
			throw new CommonException("The password not math.");
		} else {
			userAuths.setCredential(UserService.ENCODER.encode(userDto.getNewPassword()));
			return userAuthsService.update(userAuths);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = {USER, USER_DTO, USER_ROLE, USER_PERMISSION, USER_AUTHS, USER_MENU,
			USER_MENU_PERMISSION}, key = "#userDto.tenantCode + ':' + #userDto.identifier")
	public boolean resetPassword(UserDto userDto) {
		UserAuths userAuths = this.findUserAuthsByIdentifier(null, userDto.getIdentifier(), userDto.getTenantCode());
		SgPreconditions.checkNull(userAuths, "The identifier not found.");
		userAuths.setCredential(UserService.ENCODER.encode(CommonConstant.DEFAULT_PASSWORD));
		return userAuthsService.update(userAuths) > 0;
	}

	@Override
	public boolean updateLoginInfo(UserDto userDto) {
		if (StringUtils.isBlank(userDto.getIdentifier())) {
			return false;
		}

		UserAuths userAuths = this.findUserAuthsByIdentifier(userDto.getIdentifier(), userDto.getTenantCode());
		if (userAuths == null) {
			return false;
		}

		User user = new User();
		user.setId(userAuths.getUserId());
		user.setLoginTime(userDto.getLoginTime());
		user.setUpdateTime(userDto.getLoginTime());
		user.setOperator(userAuths.getIdentifier());
		return this.userService.update(user) > 0;
	}

	private String decryptCredential(String encoded, Integer identityType) {
		if (StringUtils.isBlank(encoded)) {
			return CommonConstant.DEFAULT_PASSWORD;
		}
		// 微信、手机号注册不需要解密
		if (IdentityType.WE_CHAT.getValue().equals(identityType) || IdentityType.PHONE_NUMBER.getValue()
				.equals(identityType)) {
			return encoded;
		}
		try {
			encoded = AesUtil.decryptAES(encoded, sysProperties.getKey()).trim();
		} catch (Exception e) {
			throw new CommonException(e, "Failed to decrypt credential");
		}
		return encoded;
	}
}
